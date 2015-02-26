#Program that converts video into HSV, and detects only rgb values that
#resemble a red hue

import cv2, math
import numpy as np

vc = cv2.VideoCapture(0)


while(True):
    
    rval, frame = vc.read()
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

    lower_red = np.array([100,50,50])
    upper_red = np.array([255,131,255])

    mask = cv2.inRange(hsv, lower_red, upper_red)

    res = cv2.bitwise_and(frame,frame, mask = mask)

    cv2.imshow('Red Mask', mask)

    k = cv2.waitKey(1)
    if k == 27:
        break
cv2.destroyAllWindows()

