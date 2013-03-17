importScripts("js-aruco/cv.js");
importScripts("js-aruco/aruco.js");

Filters = {};

Filters.grayscale = function(pixels) {
  var d = pixels.data;
  for (var i = 0; i < d.length; i += 4) {
    var r = d[i];
    var g = d[i+1];
    var b = d[i+2];
    // CIE luminance for the RGB
    // The human eye is bad at seeing red and blue, so we de-emphasize them.
    var v = 0.2126*r + 0.7152*g + 0.0722*b;
    d[i] = d[i+1] = d[i+2] = v
  }
  return pixels;
};

Filters.inRange = function(pixels, width, height, low, high) {
  var result = new CV.Image(width, height);
  var o = result.data;
  var d = pixels.data;
  var lr = low[0], lg = low[1], lb = low[2];
  var hr = high[0], hg = high[1], hb = high[2];
  var i = 0;
  var v = 0;
  var j = 0;
  while (i < d.length) {
    v = (d[i] >= lr && d[i] <= hr
        && d[i+1] >= lg && d[i+1] <= hg
        && d[i+2] >= lb && d[i+2] <= hb) ? 255 : 0;
    o[j++] = v;
    i += 4;
  }
  return result;
}

Filters.toHSV = function(pixels) {
  var d = pixels.data;
  var i = 0;
  var min, max, delta;
  var r, g, b, h;
  for (var i = 0; i < d.length; i += 4) {
    r = d[i];
    g = d[i+1];
    b = d[i+2];
    min = Math.min(r, g, b);
    max = Math.max(r, g, b);
    d[i] = max;
    delta = max - min;
    if (!max) {
      d[i+1] = 0;
      d[i+2] = 0;
    } else {
      d[i+1] = (delta / max) * 255;
      if (r === max) {
        h = (g - b) / delta;
      } else if (g === max) {
        h = 2 + (b - r) / delta;
      } else {
        h = 4 + (r - g) / delta;
      }
      h *= 42.5;
      if (h < 0) {
        h += 255;
      }
      d[i+2] = h;
    }
  }
  return pixels;
}


var cvImageToPixels = function(image, pixels) {
  var r = pixels.data;
  var d = image.data;
  var o = 0;
  for (var i = 0; i < d.length; ++i) {
    o = i * 4;
    r[o] = d[i];
    r[o+1] = d[i];
    r[o+2] = d[i];
  }
}

var polygonArea = function(p) {
  var end = p.length - 1;
  var area = 0;
  for (var i = 0; i < end; ++i) {
    area += p[i].x * p[i+1].y - p[i].y * p[i+1].x;
  }
  area += p[i].x * p[0].y - p[i].y * p[0].x;
  area *= 0.5;
  return area;
}

var polygonCentroid = function(p) {
  var end = p.length - 1;
  var x = 0;
  var y = 0;
  var a = 0;
  var area = 0;
  for (var i = 0; i < end; ++i) {
    a = p[i].x * p[i+1].y - p[i].y * p[i+1].x;
    area += a;
    x += (p[i].x + p[i+1].x) * a;
    y += (p[i].y + p[i+1].y) * a;
  }
  a = p[i].x * p[0].y - p[i].y * p[0].x;
  area += a;
  x += (p[i].x + p[0].x) * a;
  y += (p[i].y + p[0].y) * a;

  area *= 0.5;
  var coeff = 1.0 / (6 * area);
  x *= coeff;
  y *= coeff;
  return {x: x, y: y, area: polygonArea(p)};
}

var getCentroids = function(contours) {
  var result = [];
  for (var i = 0; i < contours.length; ++i) {
    result.push(polygonCentroid(contours[i]));
  }
  return result;
}

// Assumes the polygon is convex
var pointInPolygon = function(p, poly) {
  var c = false;
  var j = poly.length - 1;
  var vi, vj;
  for (var i = 0; i < poly.length; j = i++) {
    vi = poly[i];
    vj = poly[j];
    if (((vi.y > p.y) != (vj.y > p.y))
        && (p.x < (vj.x - vi.x) * (p.y - vi.y) / (vj.y - vi.y) + vi.x)) {
      c = !c;
    }
  }
  return c
}

// Assumes the polygons are convex
var polygonInPolygon = function(inner, outer) {
  for (var i = 0; i < inner.length; ++i) {
    if (!pointInPolygon(inner[i], outer)) {
      return false;
    }
  }
  return true;
}

var findOuterPolygons = function(contours) {
  var results = [];
  for (var i = 0; i < contours.length; ++i) {
    for (var j = i + 1; j < contours.length; ++j) {
      if (polygonInPolygon(contours[j], contours[i])) {
        results.push(contours[i]);
      }
    }
  }
  return results;
}

var determineCorners = function(poly, centroid) {
  var result = {topLeft: null,
      topRight: null,
      bottomLeft: null,
      bottomRight: null
  };
  for (var i = 0; i < poly.length; ++i) {
    var p = poly[i]
    if (p.x < centroid.x && p.y < centroid.y) {
      result.topLeft = p;
    } else if (p.x < centroid.x && p.y > centroid.y) {
      result.bottomLeft = p;
    } else if (p.x > centroid.x && p.y < centroid.y) {
      result.topRight = p;
    } else {
      result.bottomRight = p;
    }
  }
  return result;
}

var lineLength = function(line) {
  var x = line[1].x - line[0].x;
  var y = line[1].y - line[0].y;
  return Math.sqrt(x * x + y * y);
}

var toRadians = function(degrees) {
  return degrees * (Math.PI / 180);
}

// TODO: confirm the angle of view variables
var xAngleOfView = 47; //Found in a pdf on axis.com
var yAngleOfView = 35.25; //xAngleOfView / aspectRatio;
// TODO: select real values for the variables below
var rlTargetHeight = 12; //inches
//TODO: find the actual value here
var cameraHeight = 20; //inches

var selectTarget = function(outerPolygons, centroids, imageWidth, imageHeight) {
  // Select the highest target
  var highestY = 1000000;
  var selected = -1;
  for (var i = 0; i < centroids.length; ++i) {
    if (centroids[i].y < highestY) {
      highestY = centroids[i].y;
      selected = i;
    }
  }

  var xAngle = 0;
  var zReal = 0;
  var leftSide = null;
  var rightSide = null;
  var found = selected >= 0;
  if (found) {
    var poly = outerPolygons[selected];
    
    var centroid = centroids[selected];
    var normalizedCentroid = {x: (centroid.x / imageWidth - 0.5) * 2,
        y: (centroid.y / imageHeight - 0.5) * 2
    };

    var corners = determineCorners(poly, centroid);
    leftSide = [corners.topLeft, corners.bottomLeft];
    rightSide = [corners.topRight, corners.bottomRight];
    
    xAngle = normalizedCentroid.x  * xAngleOfView * 0.5;
    
    // TODO: something better than averaging the sides?
    var objectHeight = (lineLength(leftSide) + lineLength(rightSide)) * 0.5 
    var fovInches = (rlTargetHeight * imageHeight) / objectHeight;
    zReal = (fovInches * 0.5) / Math.tan(toRadians(yAngleOfView * 0.5));
  }
  return {found: found, targetAngle: xAngle, targetDistance: zReal,
        leftSide: leftSide, rightSide: rightSide};
}

var binary = [];

var detector = new AR.Detector();

/* thresholds:
 * image.jpg:
 * [10, 120, 55], [255, 255, 115]
 * pen thing:
 * [10, 100, 115], [255,255, 175]
 */

onmessage = function(event) {
  var start = new Date().getTime();
  var pixels = event.data.pixels;
  var hsv = Filters.toHSV(pixels);
  var inRange = Filters.inRange(hsv, event.data.width, event.data.height,
      // TODO: make the colours selectable
      [30, 110, 110], [200, 255, 255]);
  var contours = CV.findContours(inRange, binary);
  var candidates = detector.findCandidates(contours, event.data.width * 0.20,
      0.05, 10)
  var outerPolygons = findOuterPolygons(candidates);
  var centroids = getCentroids(outerPolygons);
  var selected = selectTarget(outerPolygons, centroids, event.data.width,
      event.data.height);
  cvImageToPixels(inRange, pixels);
  var end = new Date().getTime();
  self.postMessage({image: pixels,
      processing_time: end - start,
      contours: contours,
      candidates: candidates,
      centroids: centroids,
      outerPolygons: outerPolygons,
      selected: selected});
};