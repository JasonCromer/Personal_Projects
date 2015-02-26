import cv2, time

CURRENT_TIME = time.time()
END_TIME = CURRENT_TIME + (43200) #12 Hours of capture in seconds


def timelapseIt():
    videoSrc = cv2.VideoCapture(0)
    videoSrc.set(3,1280)
    videoSrc.set(4,1024)
    time.sleep(2)
    COUNT = 0
    while CURRENT_TIME < END_TIME:
        frame_num = "%08d" % (COUNT,)
        ret, img = videoSrc.read()
        cv2.imwrite(frame_num + '.jpg', img)
        COUNT = COUNT + 1
        time.sleep(3600) #Take pic every 5 min
        if cv2.waitKey(1) == ord('q'):
            sys.exit()

    cv2.destroyAllWindows()
    videoSrc.release()
    
    




if __name__ == '__main__':
    timelapseIt()
    
