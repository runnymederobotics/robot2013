var startCamera = function(divId, url, callback) {
  var container = $(divId);
  container.empty();
  
  var imageDiv = container.append("<div></div>").children("div");
  var getCanvasContext = function(width, height, location) {
    var canvas = document.createElement('canvas');
    canvas.width = width;
    canvas.height = height;
    if (location) {
      location.append(canvas);
    }
    return canvas.getContext("2d");
  }
  
  var makeSmoothieChart = function() {
    var canvas = document.createElement('canvas');
    $(divId).append(canvas);
    var chart = new SmoothieChart();
    chart.streamTo(canvas, /* delay */ 1000);
    var timeSeries = new TimeSeries();
    chart.addTimeSeries(timeSeries);
    return timeSeries;
  }
  
  var frameTime = makeSmoothieChart();
  var fps = makeSmoothieChart();
  var frames = 0;
  var lastImageTime = 0;
  
  var frameWorker = new Worker("image_processor.js");
  frameWorker.onmessage = function() {};
  
  var offScreenContext = null;
  var context = null;
            
  var image = imageDiv.append("<img src='#' crossOrigin='Anonymous'/>").children("img");
  image.load(function() {
    if (!context) {
      context = getCanvasContext(this.width, this.height, imageDiv);
      frameWorker.onmessage = function(event) {
        context.putImageData(event.data.image, 0, 0);
        var now = new Date().getTime();
        frameTime.append(now, event.data.processing_time);
        if (now - lastImageTime >= 1000) {
          fps.append(now, frames);
          frames = 0;
          lastImageTime = now;
        }
        frames += 1;
        lastImageTime = now;
        if (callback) {
          event.data.image = "[Binary Blob]";
          callback(event.data);
        }
      }
      offScreenContext = getCanvasContext(this.width, this.height, null);
    }
    
    try {
      offScreenContext.drawImage(this, 0, 0);
      
      var imageData = offScreenContext.getImageData(0, 0, this.width, this.height);
      frameWorker.postMessage(imageData);
    } catch (e) {
      container.empty();
      container.append("<p>Error reading the image data. This is most likely caused by a cross origin security issue. "
                            + "Ensure the video feed has a valid Access-Control-Allow-Origin header. The ForceCORS Firefox "
                            + "extension can be used to add the header to all responses. ONLY USE THE EXTENSION IF YOU KNOW "
                            + "WHAT YOU'RE DOING. DISABLE IT WHEN YOU'RE DONE. https://addons.mozilla.org/en-US/firefox/addon/forcecors/</p>");
      throw e;
    }
  });
  
  var connectionCounter = $(divId).append("<p></p>").children("p");
  
  var connectionCount = 0;
  var connect = function() {
    var now = new Date().getTime();
    if (now - lastImageTime > 2000) {
      connectionCount += 1;
      connectionCounter.text("Connection Attempts: " + connectionCount);
      image.attr("src", "#");
      image.attr("src", url);
    } else {
      connectionCounter.text("");
    }
  }
  setInterval(connect, 1000)
  connect();
}