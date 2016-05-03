#Chapter 5 Homework

###########Problem #1 ##############

n = 500 # number of samples
k1 = 10 # sample size of each sample
k2 = 50

#.a)

X=runif(5000, min=0, max=5)
xMatrix = matrix(X,k1,n) 
xmean = apply(xMatrix,2,mean)

#Graph the mean of the samples
hist(xmean)
qqnorm(xmean)
qqline(xmean)

#Emperical Expected Value of samples means. Theoretical E(x) is 2.5. Compare the two values
mean(xmean)

#Emperical Variance of samples means.Theoretical V(x) is .20833333. Compare the two values
var(xmean)


## For both the expected value and variance, the emperical values of the expected mean and variance of 
## samples means were extremely close. Using the central limit theorem, these two are very close

X=runif(25000, min=0, max=5)
xMatrix = matrix(X,k2,n) 
xmean = apply(xMatrix,2,mean)

#Graph the mean of the samples
hist(xmean)
qqnorm(xmean)
qqline(xmean)

#Emperical Expected Value of samples means. Theoretical E(x) is 2.5. Compare the two values
mean(xmean)

#Emperical Variance of samples means.Theoretical V(x) is .0416667. Compare the two values
var(xmean)


## For both the expected value and variance, the emperical values of the expected mean and variance of 
## samples means were extremely close. Using the central limit theorem, these two are very close


#b. check page 173 for formulas


X=rgamma(5000,4,scale = .5)
xMatrix = matrix(X,k1,n) 
xmean = apply(xMatrix,2,mean)

#Graph the mean of the samples
hist(xmean)
qqnorm(xmean)
qqline(xmean)

#Emperical Expected Value of samples means. Theoretical E(x) is 2.0. Compare the two values
mean(xmean)

#Emperical Variance of samples means.Theoretical V(x) is .1 . Compare the two values
var(xmean)


## For both the expected value and variance, the emperical values of the expected mean and variance of 
## samples means were extremely close. Using the central limit theorem, these two are very close


X=rgamma(25000,4,scale = .5)
xMatrix = matrix(X,k2,n) 
xmean = apply(xMatrix,2,mean)

#Graph the mean of the samples
hist(xmean)
qqnorm(xmean)
qqline(xmean)

#Emperical Expected Value of samples means. Theoretical E(x) is 2.0. Compare the two values
mean(xmean)

#Emperical Variance of samples means.Theoretical V(x) is .02 . Compare the two values
var(xmean)


## For both the expected value and variance, the emperical values of the expected mean and variance of 
## samples means were extremely close. Using the central limit theorem, these two are very close



############### Problem 2 ###################

lambda = 1/80

#a.)
vectorOne = c(rexp(1000,lambda))
vectorTwo = c(rexp(1000,lambda))
sumVectors = vectorOne + vectorTwo

#calculate the probability that the sum of lifetimes are greater than 100 hours
x = sumVectors[sumVectors>100]
answer = (length(x)/length(vectorOne))^2
answer

#b.)
vOne = vectorOne[vectorOne>50]
vTwo = vectorTwo[vectorTwo>50]
lengthOne = (length(vOne)/length(vectorOne))^2
lengthTwo = (length(vTwo)/length(vectorTwo))^2

#Calculate the probability that both lasers will last longer than 12 hours
answer = lengthOne * lengthTwo
answer