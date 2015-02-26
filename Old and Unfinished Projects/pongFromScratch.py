import pygame, math, random
from pygame.locals import *
pygame.init()



class Player():
    def __init__(self):
        self.x, self.y = 16, SCR_HEI/2
        self.padWid, self.padHei = 8, 64
        self.speed = 5
        

    def movement(self):
        keys = pygame.key.get_pressed()
        if keys[pygame.K_w]:
            self.y -= self.speed
        elif keys[pygame.K_s]:
            self.y += self.speed

        if self.y <= 0:
            self.y = 0
        elif self.y >= SCR_HEI - 64:
            self.y = SCR_HEI - 64

    def draw(self):
        pygame.draw.rect(screen, white, (self.x, self.y, self.padWid, self.padHei))


class Enemy(Player):
    def __init__(self):
        self.x, self.y = SCR_WID - 16, SCR_HEI/2
        self.padWid, self.padHei = 8, 64
        self.speed = 5

    def movement(self):
        keys = pygame.key.get_pressed()
        if keys[pygame.K_UP]:
            self.y -= self.speed
        elif keys[pygame.K_DOWN]:
            self.y += self.speed

        if self.y <= 0:
            self.y = 0
        elif self.y >= SCR_HEI - 64:
            self.y = SCR_HEI - 64
            

class Ball():
    def __init__(self):
        self.x, self.y = SCR_WID/2, SCR_HEI/2
        self.speed_x = -3
        self.speed_y = 3
        self.size = 8

    def movement(self):
        self.x += self.speed_x
        self.y += self.speed_y

        #wall collision
        if self.y <= 0:
            self.speed_y *= -1
        elif self.y >= SCR_HEI:
            self.speed_y *= -1

        if self.x <= 0 or self.x >= (SCR_WID - self.size):
            self.__init__()
            self.speed_x = 3

        #paddle collide

        for n in range(-self.size, player.padHei):
            if self.y == player.y + n:
                if self.x <= player.x + player.padWid:
                    self.speed_x *= -1.1
                    self.speed_y *= 1.2
                    break
            n += 1

        for n in range(-self.size, enemy.padHei):
            if self.y == enemy.y + n:
                if self.x >= enemy.x - enemy.padWid:
                    self.speed_x *= -1.1
                    self.speed_y *= 1.1
                    break
            n += 1

    def draw(self):
        pygame.draw.rect(screen, (255,255,255), (self.x, self.y, 8,8))






SCR_WID, SCR_HEI = 640, 480
screen = pygame.display.set_mode((SCR_WID, SCR_HEI))
pygame.display.set_caption("PONG")
white = (255,255,255)
player = Player()
enemy = Enemy()
ball = Ball()
clock = pygame.time.Clock()
fps = 60

while True:
    for event in pygame.event.get():
        if event.type == QUIT:
            exit()
    screen.fill((0,0,0))       
    player.movement()
    player.draw()
    enemy.movement()
    enemy.draw()
    ball.movement()
    ball.draw()
    pygame.display.flip()
    clock.tick(fps)
