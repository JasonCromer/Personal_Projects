import pygame, math, random
from pygame.locals import *



def add_vectors((angle1,length1),(angle2,length2)):
        x = math.sin(angle1) * length1 + math.sin(angle2) * length2
        y = math.cos(angle1) * length1 + math.cos(angle2) * length2

        length = math.hypot(x,y)
        angle = .5 * math.pi - math.atan2(y,x)
        return (angle, length)


def findParticle(particles, x, y):
    for p in particles:
        if math.hypot(p.x-x, p.y-y) <= p.size:
            return p
    return None


class Particle:
    def __init__(self, (x,y), size):
        self.x = x
        self.y = y
        self.size = size
        self.color = (0,0,255)
        self.thickness = 1
        self.speed = 0.01
        self.angle = 1
   
    def display(self):
        pygame.draw.circle(screen, self.color, (int(self.x), int(self.y)), self.size, self.thickness)

    def movement(self):
        (self.angle, self.speed) = add_vectors((self.angle, self.speed), gravity)
        self.x += math.sin(self.angle) * self.speed
        self.y -= math.cos(self.angle) * self.speed
        self.speed *= drag

        if self.x >= width - self.size:
            self.x = width-self.size
            self.angle = -self.angle
            self.speed *= elasticity
        if self.x <= 0:
            self.x = 0
            self.angle = - self.angle
            self.speed *= elasticity
        if self.y >= height - self.size:
            self.y = height - self.size
            self.angle = math.pi - self.angle
            self.speed *= elasticity
        if self.y <= 0:
            self.y = 0
            self.angle = math.pi - self.angle
            self.speed *= elasticity




gravity = (math.pi, 0.002)
drag = .999
elasticity = 0.80
width, height = 600, 480
screen = pygame.display.set_mode((width, height)) 
pygame.display.set_caption('ManziOrbs')


number_of_particles = 3
all_particles = []

for n in range(number_of_particles):
    size = random.randint(10,20)
    x = random.randint(size, width-size)
    y = random.randint(size, height - size)

    particle = Particle((x,y), size)
    particle.speed = random.uniform(0.01, .5)
    particle.angle = random.uniform(0, 2*math.pi)
    all_particles.append(particle)

selected_particle = None
while True:
    for event in pygame.event.get():
        if event.type == pygame.QUIT:
            exit()
        if event.type == MOUSEBUTTONDOWN:
            (mouseX, mouseY) = pygame.mouse.get_pos()
            selected_particle = findParticle(all_particles, mouseX, mouseY)
        elif event.type == MOUSEBUTTONUP:
            selected_particle = None

    if selected_particle:
        (mouseX, mouseY) = pygame.mouse.get_pos()
        dx = mouseX - selected_particle.x
        dy = mouseY - selected_particle.y
        selected_particle.angle = 0.5*math.pi + math.atan2(dy,dx)
        selected_particle.speed = math.hypot(dx, dy) * 0.1
            
    background= (255,255,255)
    screen.fill(background)
    for particle in all_particles:
        particle.movement()
        particle.display()                
    pygame.display.flip()
