import pygame
import math
import sys
from random import randrange

pygame.init()
scr_width = 640
scr_height = 480
white = (255,255,255)
black = (0,0,0)
blue = (0,0,255)
green = (0, 255,0)
screen = pygame.display.set_mode((scr_width, scr_height))
i = 0

def create_rand():
    global rand_x
    global rand_y
    rand_x = randrange(0,591)
    rand_y = 320

def create_screen(color):
    screen.fill(color)

def draw_rect(color, x,y, width, height):
    rect_one = pygame.draw.rect(screen, color, (x, y, width, height), 2)


while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
             pygame.quit(); sys.exit();
            
    
    create_screen(white)
    create_rand()
    conditionals = 3
    while i < conditionals: 
        draw_rect(blue,rand_x,rand_y,50,100)
        create_rand()
        i += 1
        print(i)
        print(rand_x, rand_y)
        if(i ==3):
            print("whoo")
            pygame.display.update()
    break
