import cv2, argparse
from matplotlib import pyplot as plt


DEFAULT_CASCADE_INPUT_PATH = 'haarcascade_frontalface_alt.xml'
DEFAULT_OUTPUT_PATH = 'FaceCaptureImages\/'

class VideoCapture:
    ''' This class opens a stream through an installed or stock webcam, grabs each frame and applies a cascade to it.
        This means that for each frame that is captured, the frame is turned gray, and an xml that was designed to analyze
        and recognize a face is applied to the frame, so that when a face is present in a frame, we can use the fact that a
        face has been detected. This script utilizes this by drawing a green rectangle or square around the face, and saves
        the image.
    '''

    def __init__(self):
        self.count = 0
        self.argsObj = Parse()
        self.faceCascade = cv2.CascadeClassifier(self.argsObj.input_path)
        self.videoSource = cv2.VideoCapture(0)

    def CaptureFrames(self):
        while True:

            # Create a unique number for each frame
            frameNumber = '%08d' % (self.count,)

            # Capture frame-by-frame
            ret, frame = self.videoSource.read()

            # Set screen color to gray, so the haar cascade can easily detect edges and face
            screenColor = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

            # Customize how the cascade detects your face
            faces = self.faceCascade.detectMultiScale(
                screenColor,
                scaleFactor=1.1,
                minNeighbors=5,
                minSize=(30, 30),
                flags=cv2.CASCADE_SCALE_IMAGE
            )

            # Display the resulting frame
            cv2.imshow('Video', frame)

            # If length of faces is 0, there have been no faces detected
            if len(faces) == 0:
                pass

            # If a face is detected, faces returns 1 or more depending on amount of faces detected
            if len(faces) > 0:
                print('Face Detected')
                # Graph the face and draw a rectangle around it
                GetGraphVertices(frame, faces)

                # Once the image has been graphed and the face has a shape around it, and store the image array
                plt.imshow(frame, 'gray')

                # Write the image. dpi is the dots per inch resolution
                plt.savefig(DEFAULT_OUTPUT_PATH + frameNumber + '.png', dpi=200)
                pass

            # Increment count so we get a unique name for each frame
            self.count += 1

            #If 'esc' is hit and held for a second, the video is closed
            if cv2.waitKey(1) == 27:
                plt.close()
                break


    # When everything is done, release the capture
        self.videoSource.release()
        cv2.waitKey(1)
        cv2.destroyAllWindows()
        cv2.waitKey(1)



def GetGraphVertices(frame, faces):
    # Draw a rectangle around the faces
    for (x, y, w, h) in faces:
        cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 2)
    

def Parse():
    parser = argparse.ArgumentParser(description = 'Cascade Path for face detection')
    parser.add_argument('-i', '--input_path', type=str, default=DEFAULT_CASCADE_INPUT_PATH, help='Cascade input path')
    parser.add_argument('-o', '--output_path', type=str, default=DEFAULT_OUTPUT_PATH, help='Output path for pictures taken')
    args = parser.parse_args()
    return args
    


def main():
    #Instantiate Class object
    faceDetectImplementation = VideoCapture()

    #Call CaptureFrames from class to begin face detection
    faceDetectImplementation.CaptureFrames()



if __name__ == '__main__':
    main()

