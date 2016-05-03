#Normal probability plot
x=rnorm(1000)#generate 1000 random numbers from normal distribution with mean 0 and std   1
par(mfrow=c(1,2))#create 2 plots on one picture
hist(x)
qqnorm(x)# normal probability plot
qqline(x)#straight line on normal probability plot

x1=rexp(1000, 0.5)#generate 1000 random numbers from exponential distribution with lambda=0.5
hist(x1)
qqnorm(x1)
qqline(x1)

x1=rbinom(1000,30, 0.5)generate 1000 random numbers from binomial  distribution with n=30, p=0.5

par(mfrow=c(1,2))
hist(x1)
qqnorm(x1)
qqline(x1)


#############################################################
par(mfrow=c(1,1))
curve(dgamma(x,2,1), from = 0, to = 20, ylab = "y")
ind <- c(4, 5, 10, 15)
for (i in ind) curve(dgamma(x, shape = i,1), 0, 20, add = TRUE)


curve(dchisq(x, df = 3), from = 0, to = 20, ylab = "y")
ind=c(4, 5, 10, 15)
for (i in ind) curve(dchisq(x, df = i), 0, 20, add = TRUE, col=i)

curve(dbeta(x, 0.5,0.5), from = 0, to = 1, ylab = "y", ylim=c(0,3), xlim=c(0,1))
ind=c(0.5, 1, 2, 5)
for (i in ind) curve(dbeta(x, shape1=i,3), 0, 1, add = TRUE, col=i, ylim=c(0,3),)


ind= c(0, 1, 2, 3)
for (i in ind) curve(dlnorm(x, mean = i,1), -3, 10,  add = TRUE)


#Example for central limit theorem

#########################################
m=numeric(10000);
p=0.75; for(j in (1:50)){ k=j*j
                           for(i in(1:10000)){m[i]=(mean(rbinom(k,1,p))-p)/sqrt(p*(1-p)/k)}
                           hist(m,breaks=41,xlim=c(-4,4),main=sprintf("%d",k))
}

########################



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
n1=15
n2=30
n3=100
n4=1000
n=n1
Y=rexp(100*n, 2)
X=matrix(Y, n,100)

xmean=apply(X,2,mean)
par(mfrow=c(1,2))
hist(xmean)
qqnorm(xmean)
qqline(xmean)


mean(xmean)
var(xmean)



n=n2
Y=rexp(100*n, 2)
X=matrix(Y, n,100)

xmean=apply(X,2,mean)
par(mfrow=c(1,2))
hist(xmean)
qqnorm(xmean)
qqline(xmean)


mean(xmean)
var(xmean)


n=n3
Y=rexp(100*n, 2)
X=matrix(Y, n,100)

xmean=apply(X,2,mean)
par(mfrow=c(1,2))
hist(xmean)
qqnorm(xmean)
qqline(xmean)


mean(xmean)
var(xmean)



n=n4

Y=rexp(100*n, 2)
X=matrix(Y, n,100)

xmean=apply(X,2,mean)
par(mfrow=c(1,2))
hist(xmean)
qqnorm(xmean)
qqline(xmean)


mean(xmean)
var(xmean)

###########################################

n=5 #we use different sample size n=5,30,100
n=30
n=100
Y=rbinom(100*n,  30, 0.6) 
X=matrix(Y,n,100)#reshape data to the matrix n x 100
xmean=apply(X,2,mean)#calculate mean by column
par(mfrow=c(1,2))
hist(xmean)
qqnorm(xmean)

mean(xmean) #check the mean of the means, which value do you expect?
var(xmean) #check the variance of the means, which value do you expect?

#########################################################
######################################

#Example with resistors
#An electrical engineer will connect two resistors labelad 100 Om and 25 om 
#in parrallel. The actual reistance may differ from the labelad values. Denote
#he actual resistance of the resisters that are choosen X and Y. The total 
#esistance is R=XY\(X+Y).
#Assume X ~Normal(mu=100, sd=10) distr, Y~N(25, sq=2.5) and the resistors are choosen
#independantly. Find P(19<R<21)

x=rnorm(1000, mean=100, sd=10)
y=rnorm(1000, mean=25, sd=2.5)
R=x*y/(x+y)
RP=R[R>19 & R<21]
(Prob=length(RP)/length(x))

#Example
#An engineer has to choose between two types of cooling fans to install in a
#computer A~Exponential distributon with mean life 50 month, B~exp with mean 30 
#month. Since type A fans are more expensive, the engineer decides that 
#she will choose type A fans if P(A>2B)>0.5. Estimate this probability.

A=rexp(1000, 1/50)
B=rexp(1000, 1/30) 

qqnorm(A)
qqline(A)
hist(A)   

C=A-2*B
(Prob=length(C[C>0])/length(A))


#
# Example for reability analysis, 

#A systems consists of components A and B connected in parllel. 
#The life time in month  of A ~Exp(1), B~Exp(0.5)
# The system will
#function function until both A and B fail. Estimate the mean lifetime of the system
#lifetime. 



A=rexp(1000,1) #generate random numbers from exp(1)
B=rexp(1000, 0.5) #generate random numbers from exp(0.5).
L=pmax(A,B)#calculate max for every pair
mean(L)
PP=length(L[L<1])/length(A)# 
Lsorted=sort(L)
Lsorted[100]
quantile(L,0.1) #calculate sample 10 precentile
############################################

#########################################################

#