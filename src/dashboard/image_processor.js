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
      [10, 120, 55], [255, 255, 115]);
  var contours = CV.findContours(inRange, binary);
  var candidates = detector.findCandidates(contours, event.data.width * 0.20,
      0.05, 10)
  var outerPolygons = findOuterPolygons(candidates);
  cvImageToPixels(inRange, pixels);
  var end = new Date().getTime();
  self.postMessage({image: pixels,
      processing_time: end - start,
      contours: contours,
      candidates: candidates,
      centroids: getCentroids(candidates),
      outerPolygons: outerPolygons});
};