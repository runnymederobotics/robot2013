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

Filters.inRange = function(pixels, low, high) {
  var d = pixels.data;
  var lr = low[0], lg = low[1], lb = low[2];
  var hr = high[0], hg = high[1], hb = high[2];
  var i = 0;
  var v = 0;
  while (i < d.length) {
    v = (d[i] >= lr && d[i] <= hr
        && d[i+1] >= lg && d[i+1] <= hg
        && d[i+2] >= lb && d[i+2] <= hb) ? 255 : 0;
    d[i] = v;
    d[i+1] = v;
    d[i+2] = v;
    i += 4;
  }
  return pixels;
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

onmessage = function(event) {
  var start = new Date().getTime();
  var filtered = Filters.toHSV(event.data);
  filtered = Filters.inRange(filtered, [10, 120, 55], [255, 255, 115]);
  var end = new Date().getTime();
  self.postMessage({image: filtered, processing_time: end - start});
};