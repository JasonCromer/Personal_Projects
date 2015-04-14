import cv2, argparse, smtplib, os, getpass
from email.mime.image import MIMEImage
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText



SMTP_SERVER = 'smtp.gmail.com:587'

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
            cv2.imshow('Video', screenColor)

            # If length of faces is 0, there have been no faces detected
            if len(faces) == 0:
                pass

            # If a face is detected, faces returns 1 or more depending on amount of faces detected
            if len(faces) > 0:
                print('Face Detected')
                # Graph the face and draw a rectangle around it
                for (x, y, w, h) in faces:
                    cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 2)


                cv2.imwrite(DEFAULT_OUTPUT_PATH + frameNumber + '.png', frame)
                
            # Increment count so we get a unique name for each frame
            self.count += 1

            #If 'esc' is hit and held for a second, the video is closed
            if cv2.waitKey(1) == 27:
                break


    # When everything is done, release the capture
        self.videoSource.release()
        cv2.waitKey(1)
        cv2.destroyAllWindows()
        cv2.waitKey(1)


    

def Parse():
    parser = argparse.ArgumentParser(description = 'Cascade Path for face detection')
    parser.add_argument('-i', '--input_path', type=str, default=DEFAULT_CASCADE_INPUT_PATH, help='Cascade input path')
    parser.add_argument('-o', '--output_path', type=str, default=DEFAULT_OUTPUT_PATH, help='Output path for pictures taken')
    args = parser.parse_args()
    return args


def SendEmail(sender, receiver, password):
    # Create the container (outer) email message.
    msg = MIMEMultipart()
    msg['Subject'] = 'A Face Has Been Detected'
    msg['From'] = sender
    msg['To'] = receiver
    msg.preamble = 'Face Detected'

    
    textBody = MIMEText('<p>These are the images of a person(s) captured by your camera.</p>', _subtype='html')
    msg.attach(textBody)

    for fileName in os.listdir(DEFAULT_OUTPUT_PATH):
        if '.db' in fileName:
            continue
        elif '.png' in fileName:
            pictureFile = open(DEFAULT_OUTPUT_PATH + fileName, 'rb')
            img = MIMEImage(pictureFile.read())
            pictureFile.close()
            msg.attach(img)
        else:
            continue
    
    server = smtplib.SMTP(SMTP_SERVER)
    server.starttls()
    server.login(sender, password)
    problems = server.sendmail(sender, receiver, msg.as_string())
    server.quit()



def GetEmailsAndPassword():
    senderEmail = raw_input('Your Email: ')
    receivingEmail = raw_input('Receiving Email: ')
    password = getpass.getpass('Password: ')
    return senderEmail, receivingEmail, password
    


def ClearImageCache():
    for files in os.listdir(DEFAULT_OUTPUT_PATH):
        filePath = os.path.join(DEFAULT_OUTPUT_PATH, files)
        if os.path.isfile(filePath):
            os.unlink(filePath)
        else:
            continue
        

def main():
    ClearImageCache()

    senderEmail, receivingEmail, password = GetEmailsAndPassword()
    
    #Instantiate Class object
    faceDetectImplementation = VideoCapture()

    #Call CaptureFrames from class to begin face detection
    faceDetectImplementation.CaptureFrames()

    cv2.waitKey(5)
    #Send email with all pictures in directory
    SendEmail(senderEmail, receivingEmail, password)



if __name__ == '__main__':
    main()

