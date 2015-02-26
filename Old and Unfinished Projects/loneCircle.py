


import pygame, random, math
from pygame.locals import *
import numpy as np
pygame.init()


background = (255,255,255)
width, height = 600,480
screen = pygame.display.set_mode((width, height))

class circle:
    def __init__(self):
        self.x = random.uniform(0, 598)
        self.y = random.uniform(0, 498)
        self.speed = 0.5
        self.color = (0,0,255)
        self.radius = 10
        self.width = 1
        self.angle = random.uniform(0,2*math.pi)

    def draw(self):
        pygame.draw.circle(screen, self.color, (int(self.x), int(self.y)), self.radius, self.width)

    def movement(self):
        self.x += math.sin(self.angle) * self.speed
        self.y -= math.cos(self.angle) * self.speed

        if self.x >= width - self.width:
            self.x = width - self.width
            self.angle = -self.angle
        if self.x <= 0:
            self.x = 0
            self.angle = -self.angle
        if self.y >= height - self.width:
            self.y = height - self.width
            self.angle = math.pi - self.angle
        if self.y <= 0:
            self.y = 0
            self.angle = math.pi - self.angle
        
        

circle = circle()

while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            exit()

    screen.fill(background)
    circle.draw()
    circle.movement()
    pygame.display.flip()
    
