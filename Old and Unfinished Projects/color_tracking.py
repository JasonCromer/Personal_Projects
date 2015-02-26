#Program that shows masks of all three rgb values in seperate windows

import cv2
import numpy as np

cap = cv2.VideoCapture(0)

while(1):

    #take each frame
    _, frame = cap.read()

    #convert BGR to HSV
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

    #define range of blue, green, and red color in HSV
    lower_blue = np.array([110,50,50])
    upper_blue = np.array([130,255,255])
    lower_red = np.array([50,50,110])
    upper_red = np.array([255,255,130])
    lower_green = np.array([50,110,50])
    upper_green = np.array([255,130,255])

    #Threshold the HSV image to get only the blue, red, and green colors
    mask = cv2.inRange(hsv, lower_blue, upper_blue)
    mask2 = cv2.inRange(hsv, lower_red, upper_red)
    mask3 = cv2.inRange(hsv, lower_green, upper_green)

    #bitwise mask and original mask
    res = cv2.bitwise_and(frame,frame, mask = mask)

    cv2.imshow('green mask', mask3)
    cv2.imshow('red mask', mask2)
    #cv2.imshow('frame', frame)
    cv2.imshow('blue mask', mask)
    #cv2.imshow('res', res)
    k = cv2.waitKey(5)
    if k == 27:
        break
cv2.destroyAllWindows()
