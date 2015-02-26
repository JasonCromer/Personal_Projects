import pygame, math, random
from pygame.locals import *
pygame.init()
random.seed()

class Tank():
    def __init__(self):
        self.x, self.y = 300, 200
        self.speed = 5
        self.wid, self.hei = 20,25
        self.color = (0,0,255)
        self.angle = random.randint(0, 360)

    def movement(self):
        keys = pygame.key.get_pressed()
        if keys[pygame.K_w]:
            self.y -= self.speed
            self.angle *= -1
        if keys[pygame.K_s]:
            self.y += self.speed
            self.angle *= -1
        if keys[pygame.K_a]:
            self.x -= self.speed
            self.angle *= -1
        if keys[pygame.K_d]:
            self.x += self.speed
            self.angle *= -1

    def draw(self):
        pygame.draw.rect(screen, self.color, (self.x, self.y, self.wid, self.hei))

        
class Follower(Tank):
    def __init__(self):
        self.x, self.y = 70,70
        self.speed = 5
        self.wid, self.hei = 10,10
        self.color = (255,0,0)
        self.angle = random.uniform(0,2*math.pi)
        self.size = 5

    def movement(self):
        self.x += math.sin(self.angle) * self.speed
        self.y += math.cos(self.angle) * self.speed

        if self.x >= width - self.size:
            self.x = width-self.size
            self.angle = -self.angle
        if self.x <= 0:
            self.x = 0
            self.angle = - self.angle
        if self.y >= height - self.size:
            self.y = height - self.size
            self.angle = math.pi - self.angle
        if self.y <= 0:
            self.y = 0
            self.angle = math.pi - self.angle



    def draw(self):
        pygame.draw.rect(screen, self.color, (self.x, self.y, self.wid, self.hei))

    def add_vectors((angle1,length1),(angle2,length2)):
        x = math.sin(angle1) * length1 + math.sin(angle2) * length2
        y = math.cos(angle1) * length1 + math.cos(angle2) * length2

        length = math.hypot(x,y)
        angle = .5 * math.pi - math.atan2(y,x)
        return (angle, length)

    

width, height = 640, 480
screen = pygame.display.set_mode((640,480))
pygame.display.set_caption("Tank")
clock = pygame.time.Clock()
FPS = 60
tank = Tank()
follower = Follower()


while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            exit()

    screen.fill((255,255,255))
    clock.tick(FPS)
    tank.movement()
    tank.draw()
    #follower.movement()
    follower.draw()
    follower.movement()
    pygame.display.flip()
