importScripts("js-aruco/cv.js");
importScripts("js-aruco/aruco.js");

Filters = {};

Filters.grayscale = function(pixels, args) {
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

var binary = [];

var detector = new AR.Detector();

/* thresholds!
 * [10, 120, 55], [255, 255, 115]
 */

onmessage = function(event) {
  var start = new Date().getTime();
  var pixels = event.data.pixels;
  var hsv = Filters.toHSV(pixels);
  var inRange = Filters.inRange(hsv, event.data.width, event.data.height,
      [10, 100, 115], [255,255, 175]);
  var contours = CV.findContours(inRange, binary);
  var candidates = detector.findCandidates(contours, event.data.width * 0.20,
      0.05, 10)
  cvImageToPixels(inRange, pixels);
  var end = new Date().getTime();
  self.postMessage({image: pixels,
      processing_time: end - start,
      contours: contours,
      candidates: candidates});
};