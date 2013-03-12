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
    var lastFpsTime = 0;
  
    var frameWorker = new Worker("image_processor.js");
    frameWorker.onmessage = function() {};
  
    var offScreenContext = null;
    var context = null;
  
    var drawContours = function(contours, colour, width) {
        var all = contours;
        var ctx = context;
        var oldWidth = ctx.lineWidth;
        ctx.lineWidth = width;
        ctx.strokeStyle = colour;
        for (var i = 0; i < all.length; ++i) {
            var c = all[i];
            ctx.beginPath();
            ctx.moveTo(c[0].x, c[0].y);
            for (var j = 1; j < c.length; ++j) {
                ctx.lineTo(c[j].x, c[j].y);
            }
            ctx.lineTo(c[0].x, c[0].y);
            ctx.stroke();
        }
        ctx.lineWidth = oldWidth;
    }
  
    var drawDots = function(dots, colour) {
        context.fillStyle = colour;
        for (var i = 0; i < dots.length; ++i) {
            context.fillRect(dots[i].x, dots[i].y, 3, 3);
        }
    }
  
    var drawLine = function(line, colour, width) {
        context.strokeStyle = colour;
        var oldWidth = context.lineWidth;
        context.lineWidth = width;
        context.beginPath();
        context.moveTo(line[0].x, line[0].y);
        context.lineTo(line[1].x, line[1].y);
        context.stroke();
        context.lineWidth = oldWidth;
    }

    var image = imageDiv.append("<img src='#' crossOrigin='Anonymous'/>").children("img");
    image.load(function() {
        if (!context) {
            context = getCanvasContext(this.width, this.height, imageDiv);
            frameWorker.onmessage = function(event) {
                var data = event.data;
                context.putImageData(data.image, 0, 0);
                drawContours(data.contours, "#00ff00", 1);
                drawContours(data.candidates, "#ff0000", 1);
                drawContours(data.outerPolygons, "#0000ff", 2);
                drawDots(data.centroids, "#ff0000");
                //$("#camera_data").text(JSON.stringify(data.centroids));
                if (data.selected) {
                    var selected = data.selected;
                    drawLine(selected.leftSide, "#ff007f", 4);
                    drawLine(selected.rightSide, "#ff7f00", 4);
                }
                var now = new Date().getTime();
                frameTime.append(now, data.processing_time);
                if (now - lastFpsTime >= 1000) {
                    fps.append(now, frames);
                    frames = 0;
                    lastFpsTime = now;
                }
                frames += 1;
                lastImageTime = now;
                if (callback) {
                    data.image = "[Binary Blob]";
                    callback(data);
                }
            }
            offScreenContext = getCanvasContext(this.width, this.height, null);
        }
    
        try {
            offScreenContext.drawImage(this, 0, 0);
            
            var imageData = offScreenContext.getImageData(0, 0, this.width, this.height);
            frameWorker.postMessage({
                pixels: imageData,
                width: this.width,
                height: this.height
                });
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
