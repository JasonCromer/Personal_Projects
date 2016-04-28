#4.6 Homework #88,94

######## #88 ###########

#Given velocity data
velocity=c(69.0,69.7,72.7,80.3,81.0,85.0,86.0,86.3,86.7,87.7,89.3,90.7,91.0,92.5,93.0)

#Given percentile data
percentiles=c(-1.83,-1.28,-.97,-.73,-.52,-.34,-.17,0,.17,.34,.52,.73,.97,1.28,1.83)

#create 4 plots on one picture
par(mfrow=c(2,2))

#Create a Histogram of the velocity and percentiles
hist(velocity)
hist(percentiles)

#Create a dotplot of the velocity and percentiles
dotchart(velocity)
dotchart(percentiles)


#No, both distributions are not normal, as they are not symmetrical, or in a bell-curved shape


########## #94 #############

dataValues = c(.77,1.2,3,1.62,2.81,2.48,1.74,.47,3.09,1.31,1.87,
               .96,.81,1.43,1.51,.32,1.18,1.89,1.2,3.37,2.1,.59,1.35,.90,1.95, 2.2,.52,.81,4.75,2.05)

#a.)

#Create image with two pictures on it
par(mfrow=c(1,2))

#Convert data into histogram
hist(dataValues)

#Draw normal probability plot
qqnorm(dataValues)

#Draw line on normal probability plot
qqline(dataValues)

#b.)

#Take square root of dataValues
rootedDataValues = dataValues^(.5)

#Create image with two plots
par(mfrow=c(1,2))

#Convert into histogram and draw normal probability plot with line
hist(rootedDataValues)
qqnorm(rootedDataValues)
qqline(rootedDataValues)


#Yes, the square rooted values seem to represent a normally distributed data set


#c.)

#Take cube root of datavalues
cubeRootedDataValues = dataValues^(1/3)

#Create image with two plots
par(mfrow=c(1,2))

#Convert into histogram and draw normal probability plot with line
hist(cubeRootedDataValues)
qqnorm(cubeRootedDataValues)
qqline(cubeRootedDataValues)

#Yes, the cube rooted data values also seem to represent a normally distributed data set

