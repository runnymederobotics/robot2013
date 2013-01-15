#!/usr/bin/python
#ubuntu:
#sudo apt-get install python-numpy python-opencv
#windows:
#python-2.7.3 x86, opencv-2.3.1 x86, numpy-1.6.2 python2.7 x86
#some urls:
#http://83.64.164.6/axis-cgi/mjpg/video.cgi?resolution=320x240
#rtsp://184.72.239.149/vod/mp4:BigBuckBunny_175k.mov

import BaseHTTPServer
import cv
import cv2
import json
import md5
import mimetypes
import numpy
import optparse
import os
import Queue
import re
import socket
import SocketServer
import sys
import threading
import time
import traceback
import urlparse

class ConsoleClientHandler(BaseHTTPServer.BaseHTTPRequestHandler):
  def do_GET(self):
    print "HTTP request for " + self.path
    if "/stream/raw" in self.path:
      self.stream_from(self.server.raw_queues)
    elif "/stream/processed" in self.path:
      self.stream_from(self.server.processed_queues)
    elif "/stream/data" in self.path:
      self.stream_from(self.server.data_queues)
    elif "/stream/data_snapshot" in self.path:
      self.send_response(200)
      self.send_header("Content-type", "application/json")
      self.send_header("Access-Control-Allow-Origin", "*")
      self.end_headers()
      self.wfile.write(self.server.data)
      f.close()
    elif self.server.root_dir:
      to_serve = os.path.join(self.server.root_dir + self.path)
      if os.path.isfile(to_serve):
        mimetype = mimetypes.guess_type(to_serve)
        f = open(to_serve, "rb")
        self.send_response(200)
        self.send_header("Content-type", mimetype)
        self.send_header("Access-Control-Allow-Origin", "*")
        self.end_headers()
        self.wfile.write(f.read())
        f.close()
        return

    self.send_response(404)
    self.end_headers()
    self.wfile.flush()

  def stream_from(self, queue_list):
    q = Queue.Queue()
    queue_list.append(q)

    headers = (
            "HTTP/1.1 200 Ok\r\n"
            "Connection: close\r\n"
            "Access-Control-Allow-Origin: *\r\n"
            "Content-Type: multipart/x-mixed-replace;boundary=%s\r\n"
            "\r\n"
            "%s\r\n"
        ) % (self.server.boundary, self.server.boundary)
    self.wfile.write(headers)
    while True:
      try:
        data = q.get()
        if not data:
          break
        self.wfile.write(data)
        self.wfile.flush()
      except:
        queue_list.remove(q)

class ConsoleServer(SocketServer.ThreadingMixIn, BaseHTTPServer.HTTPServer):
  def __init__(self, address, root_dir, module):
    self.raw_queues = []
    self.processed_queues = []
    self.data_queues = []
    self.boundary = "---" + md5.md5(str(time.time())).hexdigest()
    self.root_dir = root_dir
    self.module = module
    self.last_print = time.time()
    self.reset_data()
    BaseHTTPServer.HTTPServer.__init__(self, address, ConsoleClientHandler)

  def reset_data(self):
    self.retrieve_time = 0
    self.process_time = 0
    self.encode_time = 0
    self.fps = 0
    self.bandwidth = 0

  def new_data(self, retrieve_time, process_time, encode_time, bandwidth):
    self.retrieve_time += retrieve_time
    self.process_time += process_time
    self.encode_time += encode_time
    self.fps += 1
    self.bandwidth += bandwidth

    now = time.time()
    if now - self.last_print > 0.166:
      delta = now - self.last_print
      self.last_print = now
      info = {"retrieve_time": self.retrieve_time / delta ,
          "process_time": self.process_time / delta,
          "encode_time": self.encode_time / delta,
          "total_time": (self.process_time + self.encode_time) / delta,
          "fps": self.fps / delta,
          "bandwidth": (self.bandwidth / 1024.0) / delta}
      self.reset_data()
      self.data = str(json.dumps(info))
      #print self.data
      self.data_chunk = \
          ("Content-Type: application/json\r\nContent-Length: %s\r\n"
            "\r\n%s\r\n%s\r\n") % (len(self.data), self.data, self.boundary)
      for cur in self.data_queues:
        cur.put(self.data_chunk)

  def zero_data(self):
    self.new_data(0, 0, 0, 0)

  def new_images(self, raw_image, processed_image):
    raw = \
        "Content-Type: image/jpeg\r\nContent-Length: %s\r\n\r\n%s\r\n%s\r\n" \
        % (len(raw_image), raw_image, self.boundary)
    processed = \
        "Content-Type: image/jpeg\r\nContent-Length: %s\r\n\r\n%s\r\n%s\r\n" \
        % (len(processed_image), processed_image, self.boundary)

    for cur in self.raw_queues:
      cur.put(raw)
    for cur in self.processed_queues:
      cur.put(processed)

  def stop_streams(self):
    # sending None will stop the streams
    for cur in self.raw_queues:
      cur.put(None)
    for cur in self.processed_queues:
      cur.put(None)

class Camera:
  def __init__(self, filename):
    self.filename = filename
    self.parsed_url = None
    if isinstance(filename, str):
      self.parsed_url = urlparse.urlparse(filename)
    self.http = False
    self.sock = None
    self.bandwidth = 0
    if self.parsed_url and self.parsed_url.scheme == "http":
      self.http = True

  def connect(self):
    print "Starting video capture from " + str(self.filename)
    if not self.http:
      self.capture = cv2.VideoCapture(self.filename)
      return self.capture.isOpened()
    else:
      if self.sock:
        self.sock.close()
      port = self.parsed_url.port if self.parsed_url.port else 80
      print "Connecting to " + self.parsed_url.hostname + ":" + str(port)
      self.sock = socket.create_connection((self.parsed_url.hostname, port),
          timeout=2)
      self.sock.settimeout(2)
      path = self.parsed_url.path
      host_header = re.sub(r".*@", "", self.parsed_url.netloc)
      if self.parsed_url.query:
        path += "?"
        path += self.parsed_url.query
      request = (
              "GET %s HTTP/1.1\r\n"
              "Host: %s\r\n"
              "Connection: close\r\n"
              "\r\n"
          ) % (path, host_header)
      print "Making HTTP request:\n", request
      self.sock.send(request)
      self.sock = self.sock.makefile()
      self.boundary = None
      while True:
        line = self.sock.readline()
        if not line:
          raise IOError("Connection closed")
        if line == "\r\n":
          break
        if "boundary=" in line:
          self.boundary = re.sub(r".*boundary=", "", line)
      if not self.boundary:
        raise IOError("Could not find multipart streaming boundary")
      print "Streaming boundary is " + self.boundary
      return True

  def read(self):
    bandwidth = 0
    if not self.http:
      ret = self.capture.read()
      return ret[0], ret[1], bandwidth
    else:
      line = None
      while True:
        line = self.sock.readline()
        if not line:
          raise IOError("Connection closed")
        bandwidth += len(line)
        if line == "\r\n":
          break
      data = str()
      while True:
        line = self.sock.readline()
        if not line:
          raise IOError("Connection closed")
        bandwidth += len(line)
        if line == self.boundary:
          break
        else:
          data += line
      if len(data) == 2:
        return False, None, bandwidth
      mat = cv.CreateMatHeader(1, len(data), cv.CV_8UC1)
      cv.SetData(mat, data, len(data))
      img = cv.DecodeImageM(mat, cv.CV_LOAD_IMAGE_COLOR)
      return True, numpy.asarray(img), bandwidth

class CameraProxy:
  def __init__(self, filename, interface, proxy_port, stream_port, display,
      root_dir, module):
    self.display = display
    self.module = module
    self.camera = Camera(filename)
    self.proxy_server = ConsoleServer((interface, int(proxy_port)), root_dir,
        module)

  def run_webserver(self):
    try:
      self.proxy_server.serve_forever()
    except:
      traceback.print_exc(file=sys.stdout)
    finally:
      os._exit(1)

  def encode_jpeg(self, image):
    jpeg = cv.EncodeImage(".jpg", cv.fromarray(image),
        [cv.CV_IMWRITE_JPEG_QUALITY, 50]).tostring()
    if len(jpeg) == 0:
      raise IOError("Failed to encode raw image")
    return jpeg

  def process_stream(self):
    while True:
      begin = time.time()
      retval, raw_image, bandwidth = self.camera.read()
      if not retval:
        raise IOError("Failed to read image")
      retrieve_time = time.time() - begin

      begin = time.time()
      processed_image = self.module.process_image(raw_image)
      process_time = time.time() - begin

      begin = time.time()
      raw_jpeg = self.encode_jpeg(raw_image)
      processed_jpeg = self.encode_jpeg(processed_image)
      encode_time = time.time() - begin

      self.proxy_server.new_images(raw_jpeg, processed_jpeg)
      self.proxy_server.new_data(retrieve_time, process_time, encode_time,
          bandwidth)

      if self.display:
        cv2.imshow("raw_image", raw_image)
        cv2.imshow("processed_image", processed_image)
        cv2.waitKey(min(1, max(1, 16 - process_time)))

  def run_camera(self):
    try:
      backoff = 1
      while True:
        try:
          self.camera.connect()
          backoff = 1
          self.process_stream()
        except IOError:
          traceback.print_exc(file=sys.stdout)

        print "Connection failure. Sleeping for " + str(backoff) + " seconds"
        sleep_time = backoff
        while sleep_time > 0:
          self.proxy_server.zero_data()
          time.sleep(0.5)
          sleep_time -= 0.5
        backoff = min(backoff * 1.5, 2)
    except:
      traceback.print_exc(file=sys.stdout)
    finally:
      os._exit(1)

  def run(self):
    self.camera_thread = threading.Thread(target=self.run_camera)
    self.camera_thread.start()
    self.run_webserver()

class DummyModule:
  def process_image(self, image):
    return image

  def get_data(self):
    return {}

if __name__ == "__main__":
  parser = optparse.OptionParser()
  parser.add_option("-f", "--file", dest="filename", metavar="FILE", default=0,
      help="file or stream to capture from")
  parser.add_option("-p", "--proxy_port", dest="proxy_port", metavar="PORT",
      default=8080, help="port to listen for web clients on")
  parser.add_option("-s", "--stream_port", dest="stream_port",
      metavar="STREAMPORT", default=8081,
      help="port to listen for video streaming clients on")
  parser.add_option("-i", "--interface", dest="interface", metavar="INTERFACE",
      default="0.0.0.0", help="network interface to listen for clients on")
  parser.add_option("-d", "--display", action="store_true",
      help="display stream natively")
  parser.add_option("-m", "--module", dest="module", metavar="MODULE",
      default=None, help="load custom logic from this module")
  parser.add_option("-r", "--root", dest="root", metavar="ROOT",
      default=os.path.dirname(os.path.abspath(__file__)),
      help="directory to serve static files from")

  options, args = parser.parse_args()

  module = DummyModule()
  if options.module:
    module = __import__(options.module)

  camera_proxy = CameraProxy(options.filename, options.interface,
      options.proxy_port, options.stream_port, options.display, options.root,
      module)
  camera_proxy.run()

