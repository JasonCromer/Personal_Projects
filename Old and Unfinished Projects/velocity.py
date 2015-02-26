import math


#Get horizontal and vertical components of velocity
#Return components
def GetVelocityComponents(velocityVector,velocityAngle):
    horizontalVel = float(velocityVector) * math.cos(float(velocityAngle))
    verticalVel = float(velocityVector) * math.sin(float(velocityAngle))
    return abs(horizontalVel), abs(verticalVel)


#Get x distance traveled
#Return x distance
def GetXDistance(xVelocity, time):
    xDistance = float(xVelocity) * float(time)
    return abs(xDistance)


def GetYDistance(initialYPos, acceleration, time, initVerticalVel):
    yDistance = float(initialYPos) + (.5 * float(time) * float(initVerticalVel)) + (-.5 * float(acceleration) * float(acceleration))
    return yDistance
    

def GetInputVariables():
    acceleration = 9.81
    initVerticalPosition = raw_input('Initial Vertical Position in m? --> ')
    initVelocity = raw_input('Initial Velocity in m/s? --> ')
    initVelocityAngle = raw_input('Initial Angle? --> ')
    initTime = raw_input('Time of flight in seconds? --> ')
    initHorizontalVel, initVerticalVel = GetVelocityComponents(initVelocity, initVelocityAngle)
    xTraveled = GetXDistance(initHorizontalVel, initTime)
    print('You have traveled %s meters in %s seconds' % (xTraveled, initTime))
    yTraveled = GetYDistance(initVerticalPosition, acceleration, initTime, initVerticalVel)
    print('\nYou have traveled %s meters in the vertical direction' % yTraveled)
    
if __name__ == '__main__':
    GetInputVariables()
