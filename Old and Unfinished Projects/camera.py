#Video capture!

import pygame
import pygame.camera
from pygame.locals import *


class VideoCapture(object):

    size = (640,480)
    def __init__(self, **argd):
        self.__dict__.update(**argd)
        super(VideoCapture, self).__init__(**argd)

        self.display = pygame.display.set_mode(self.size)
        self.clist = pygame.camera.list_cameras()
        if not self.clist:
            raise ValueError("No cameras here")

        self.camera = pygame.camera.Camera(self.clist[0], self.size, 'HSV')
        self.camera.start()
        self.clock = pygame.time.Clock()
        self.snapshot = pygame.surface.Surface(self.size, 0, self.display)

    def calibration(self):
        # capture the image
        self.snapshot = self.camera.get_image(self.snapshot)
        # blit it to the display surface
        self.display.blit(self.snapshot, (0,0))
        # make a rect in the middle of the screen
        crect = pygame.draw.rect(self.display, (255,0,0), (245,205,30,30), 4)
        # get the average color of the area inside the rect
        self.ccolor = pygame.transform.average_color(self.snapshot, crect)
        # fill the upper left corner with that color
        self.display.fill(self.ccolor, (0,0,50,50))
        pygame.display.flip()


    def get_and_flip(self):
        self.snapshot = self.camera.get_image(self.snapshot)
        #threshold against the color in calibration()
        mask = pygame.mask.from_threshold(self.snapshot, self.ccolor, (30,30,30))
        self.display.blit(self.snapshot, (0,0))
        #keep only the largest blob of that color
        connected = mask.connected_component()
        #make sure the blob is big enough that it isnt just noise
        if mask.count() > 100:
            #find center of the blob
            coord = mask.centroid()
            #draw a circle with size variable on the size of the blob
            pygame.draw.circle(self.display, (0,255,0), coord, max(min(50, mask.count()/400),5))
        pygame.display.flip()

    def main(self):
        going = True
        while going:
            events = pygame.event.get()
            for e in events:
                if e.type == QUIT:
                    going = False

            self.calibration()
            self.get_and_flip()
            self.clock.tick()


def main():
    pygame.init()
    pygame.camera.init()
    VideoCapture().main()
    pygame.quit()

if __name__ == "__main__":
    main()
