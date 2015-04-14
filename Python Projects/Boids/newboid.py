import pygame, sys, random, math
from pygame.locals import *

pygame.init()

#set screen size and black
size = width, height = 800, 600
black = 0,0,0

#set max velocity, number of boids, and create boid array
max_velocity = 10
num_boids = 50
boids = []

class Boid():
    def __init__(self, x, y):
        '''initiate x and y positions, and x and y random velocities'''
        self.x = x
        self.y = y
        self.velocityX = random.randint(1,10)/10.0
        self.velocityY = random.randint(1,10)/10.0


    '''return the distance from another boid'''
    def distance(self, boid):
        #distance = my position - boid position
        distX = self.x - boid.x
        distY = self.y - boid.y
        #pythag theoreom to obtain distance
        return math.sqrt(distX*distX + distY*distY)



    '''cohesion method'''
    def cohesion(self, boids):
        if len(boids) < 1: return

        # calculate the average distance from other boids
        avgX = 0
        avgY = 0

        #for each boid in the array, if the boids position = my pos, then do nothing and continue
        #if boid position != my pos, then calculate difference in position
        for boid in boids:
            if boid.x == self.x and boid.y == self.y:
                continue
        
            avgX += (self.x - boid.x)
            avgY += (self.y - boid.y)

        #divide the distance between myself and the total amount of boids to get the average
        avgX /= len(boids)
        avgY /= len(boids)


        #set velocity towards the others
        #distance is the distance from them to me, multiplied by
        # -1 gives it the distance from myself to the other boids
        distance = math.sqrt((avgX * avgX) + (avgY * avgY)) * -1.0

        #set velocities -= the average velocities
        self.velocityX -= (avgX / 100)
        self.velocityY -= (avgY / 100)



    '''alignment method'''
    def alignment(self, boids):
        if len(boids) < 1: return

        #calculate average velocities of other boids
        #initiate avg velocities
        avgX = 0
        avgY = 0

        #for every boid in the boid array, set avg vel += to the random vel given at start
        for boid in boids:
            avgX += boid.velocityX
            avgY += boid.velocityY

        #set avg vel = the the random velocity divided by the number of boids in array
        avgX /= len(boids)
        avgY /= len(boids)

        #set velocity towards other
        #set my velocity(random number) to mine plus the average vel divided by 40
        self.velocityX += (avgX/40)
        self.velocityY += (avgY/40)



    '''seperation method'''
    def seperation(self, boids, minDistance):
        if len(boids) < 1: return

        #initiate distances and a close number
        distanceX = 0
        distanceY = 0
        numClose = 0

        #for each boid in boid array
        for boid in boids:
            #distance = distance of myself from other boid
            distance = self.distance(boid)
            if distance < minDistance:
                #add 1 for every boid that comes within distance
                numClose += 1
                # find x and y differences in position of each boid
                xdiff = (self.x - boid.x)
                ydiff = (self.y - boid.y)

                #if distance is big, keep at a normal fixed distance (sqrt of 20 = approx 4.47)
                if xdiff >= 0: xdiff = math.sqrt(minDistance) - xdiff
                #if distance is too small, seperate greatly
                elif xdiff < 0: xdiff = -math.sqrt(minDistance) - xdiff

                if ydiff >= 0: ydiff = math.sqrt(minDistance) - ydiff
                elif ydiff < 0: ydiff = -math.sqrt(minDistance) - ydiff

                #set x and y distance to itself plus the calculate difference above
                distanceX += xdiff
                distanceY += ydiff

        if numClose == 0:
            return

        #set velocities to distance divided by 5
        self.velocityX -= distanceX / 5
        self.velocityY -= distanceY / 5

    '''perform movement based on velocity'''
    def move(self):

        #if my velocity is greater than set max vel, scale the velocity
        #multiply scaled velocity and set it to new velocity
        if abs(self.velocityX) > max_velocity or abs(self.velocityY) > max_velocity:
            scaleFactor = max_velocity / max(abs(self.velocityX), abs(self.velocityY))
            self.velocityX *= scaleFactor
            self.velocityY *= scaleFactor

        self.x += self.velocityX
        self.y += self.velocityY



'''create screen, game objects and main loop'''
#set screen
screen = pygame.display.set_mode(size)


#grab image and get rectangle
ball = pygame.image.load("ball.png")
ball = pygame.transform.scale(ball, (10,10))
ballrect = ball.get_rect()




#create boids in random positions
for i in range(num_boids):
    boids.append(Boid(random.randint(0, width), random.randint(0, height)))

while 1:
    for event in pygame.event.get():
        if event.type == pygame.QUIT: exit()

    for boid in boids:
        closeBoids = []
        for otherBoid in boids:
            if otherBoid == boid: continue
            distance = boid.distance(otherBoid)
            if distance < 200:
                closeBoids.append(otherBoid)

        boid.cohesion(closeBoids)
        boid.alignment(closeBoids)
        boid.seperation(closeBoids,20)

        #stay within screen
        border = 25
        if boid.x < border and boid.velocityX < 0:
            boid.velocityX = -boid.velocityX * random.random()
        if boid.x > width - border and boid.velocityX > 0:
            boid.velocityX = -boid.velocityX * random.random()
        if boid.y < border and boid.velocityY < 0:
            boid.velocityY = -boid.velocityY * random.random()
        if boid.y > height - border and boid.velocityY > 0:
            boid.velocityY = - boid.velocityY * random.random()

        boid.move()

    screen.fill(black)
    for boid in boids:
        boidRect = pygame.Rect(ballrect)
        boidRect.x = boid.x
        boidRect.y = boid.y
        screen.blit(ball, boidRect)
    fps = 60
    pygame.display.flip()
    pygame.time.delay(10)
        




    
