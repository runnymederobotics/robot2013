import cv2
import numpy
import time

GREEN_MIN = numpy.array([55, 120, 10], numpy.uint8)
GREEN_MAX = numpy.array([115, 255, 255], numpy.uint8)

def process_image(image):
  hsv_image = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
  threshold = cv2.inRange(hsv_image, GREEN_MIN, GREEN_MAX)
  cont = numpy.copy(threshold)
  contours, hierarchy = cv2.findContours(cont, cv2.RETR_EXTERNAL,
      cv2.CHAIN_APPROX_SIMPLE)
  cv2.drawContours(image, contours, -1, (0, 255, 0), 1)
  return threshold

def get_data():
  return {"time": time.time()}
