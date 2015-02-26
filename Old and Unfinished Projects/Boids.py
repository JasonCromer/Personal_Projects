# BOIDS!

#imports
import pygame, sys, math, random
from pygame.locals import *
pygame.init()
random.seed()


#global
screen = pygame.display.set_mode((800,600))
clock = pygame.time.Clock()
allsprites = pygame.sprite.Group()



class Boid(pygame.sprite.Sprite):
    #functions: __init__: create sprite/rect, and rotate
    #             update: get mouse position on frame update
    
    def __init__(self):
        pygame.sprite.Sprite.__init__(self)
        self.image = pygame.image.load("arrow.png")
        self.rawimage = self.image
        self.rawimage = pygame.transform.scale(self.rawimage, (30,30))
        self.rect = self.image.get_rect()
        self.area = screen.get_rect()
        self.move = 5
        self.rect.move_ip(300,100)
        self.velocity = 100
        self.heading = random.randint(0,360)
        self.x = random.randint(0,600)
        self.y = random.randint(0,600)
        
    
    def get_pos(self):
        return pygame.math.Vector2(self.x, self.y)
        

    def update(self):
        dt = clock.get_time()/1000.
        incriment = self.velocity * dt
        self.y += math.cos(self.heading * math.pi/180) * incriment
        self.x += math.sin(self.heading * math.pi/180) * incriment
        self.x %= self.area.width
        self.y %= self.area.height
        self.rect.center = (self.x, self.area.height - self.y)
        self.image = pygame.transform.rotate(self.rawimage, 135 - self.heading)
        

        lfm = self.localFlockMates()
        sepvec = self.seperation(lfm)
        cohvec = self.cohesion(lfm)
        alignvec = self.alignment(lfm)
        

        steeringvec = ((sepvec * .5) + (cohvec * 2.5) + (alignvec * 2)) /3  # + other two vectors / average
        currentvec = pygame.math.Vector2(0, 1).rotate(-self.heading)
        turndirection = currentvec.angle_to(steeringvec)
        if turndirection > 5:
            self.heading -= 1
        elif turndirection < -5:
            self.heading += 1

        
 
    def localFlockMates(self):
        #search through sprites to determine near-by ones
        output = []
        for sprite in allsprites:
            if sprite != self and self.get_pos().distance_to(sprite.get_pos()) < 600:
                output.append(sprite)
        return output
        

    def seperation(self, localFlockMates):
        vec = pygame.math.Vector2()
        for lfm in localFlockMates:
            offset = self.get_pos() - lfm.get_pos()
            vec += offset/offset.length()
        return vec
            
    def cohesion(self, localFlockMates):
        vec2 = pygame.math.Vector2()
        for lfm in localFlockMates:
            centervec = lfm.get_pos()
            vec2 += lfm.get_pos()/len(localFlockMates)  #how do I get number of birds in radius?
        return vec2

    def alignment(self, localFlockMates):
        vec3 = pygame.math.Vector2()
        for lfm in localFlockMates:
            avgvec = pygame.math.Vector2(0, 1).rotate(-lfm.heading)
            vec3 += avgvec/len(localFlockMates)
        return vec3


    
        
        #cohesion and alignment vectors, add all together to produce steering vec
        #cohesion average position of lfm vectors and calculate self offset to that position
        #alignment calculate average vector of lfm's and calculate self offset to that position

        



def main():
    #initialize
    pygame.display.set_caption("BOIDS!")
    pygame.mouse.set_visible(0)

    #background
    background = pygame.Surface(screen.get_size())
    background.fill((250,250,250))
    screen.blit(background, (0,0))

    #game objects
    for x in range(20):
        boid = Boid()
        allsprites.add(boid)
        

    #loop through
    while "I can":
        clock.tick(60)
        for event in pygame.event.get():
            if event.type == pygame.QUIT: exit()
                
        #Draw everything
        allsprites.clear(screen, background)
        allsprites.update()
        allsprites.draw(screen)
        pygame.display.flip()

if __name__ == "__main__": main()
