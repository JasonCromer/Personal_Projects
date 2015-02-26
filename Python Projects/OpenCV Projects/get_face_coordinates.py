import cv2
import sys, getopt
import numpy as np
import cv2
from matplotlib import pyplot as plt

args, video_src = getopt.getopt(sys.argv[1:], '', ['cascade=', 'nested-cascade='])
args = dict(args)
cascPath = args.get('--cascade', "haarcascade_frontalface_alt.xml")
faceCascade = cv2.CascadeClassifier(cascPath)

video_src = cv2.VideoCapture(0)


def graph_computations():
    print((x,y), (x+w, y+h))
    length = (x + (x+w))/2
    height = (y + (y+h))/2
    print(length,height)
    plt.show()
    



while True:
    # Capture frame-by-frame
    plt.ion()
    ret, frame = video_src.read()

    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    
    faces = faceCascade.detectMultiScale(
        gray,
        scaleFactor=1.1,
        minNeighbors=5,
        minSize=(30, 30),
        flags=cv2.CASCADE_SCALE_IMAGE
    )

    # Draw a rectangle around the faces
    for (x, y, w, h) in faces:
        cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 2)

    # Display the resulting frame
    cv2.imshow('Video', frame)
    plt.imshow(frame, 'gray')



    if cv2.waitKey(1) == ord('o'):
        graph_computations()

    elif cv2.waitKey(1) == ord('q'):
        break

# When everything is done, release the capture
plt.close()
video_src.release()
cv2.destroyAllWindows()
