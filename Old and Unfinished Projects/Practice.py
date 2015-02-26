
import cv2
import cv2.cv as cv
import numpy as np
from matplotlib import pyplot as plt



img1 = cv2.imread('koala.jpg', 0)
img2 = cv2.imread('koalaFace.jpg', 0)

sift = cv2.SIFT()

kp1, des1 = sift.detectAndCompute(img1,None)
kp2, des2 = sift.detectAndCompute(img2,None)

bf = cv2.BFMatcher()
matches = bf.knnMatch(des1,des2, k=2)

good = []
for m,n in matches:
    if m.distance < 0.75*n.distance:
        good.append([m])

img3 = cv2.drawMatchesKnn(img1,kp1,img2,kp2,good,flags=2)
plt.imshow(img3), plt.show()
