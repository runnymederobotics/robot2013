#!/usr/bin/python

import socket
import threading
import sys
import urlparse

server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server.bind(('0.0.0.0', 8081))
server.listen(5)

parsed_url = urlparse.urlparse(sys.argv[1])

def HandleClient(client):
  remote = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  global parsed_url
  remote.connect((parsed_url.hostname, int(parsed_url.port)))
  remote = remote.makefile()

  lines = []
  while True:
    line = client.readline()
    lines.append(line)
    if line == "\r\n":
      break
  for line in lines:
    print line,
    remote.write(line)
  remote.flush()

  start = remote.readline()
  client.write(start)
  client.write("Access-Control-Allow-Origin: *\r\n")
  while True:
    data = remote.read(2048)
    client.write(data)
    client.flush()


while True:
  client, client_addr = server.accept()
  print "New connection from " + str(client_addr)
  threading.Thread(target=HandleClient, args=(client.makefile(),)).start()
