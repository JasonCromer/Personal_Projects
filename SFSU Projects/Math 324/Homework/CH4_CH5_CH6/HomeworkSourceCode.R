#Problem 1
#n = 500, k = 10 and 50

#a, find uniform distribtion from 0 to 5
Y=runif(n=500,min=0,max=5)
X=matrix(data=Y,500,100)
xmean=apply(X,2,mean)
par(mfrow=c(1,2))
hist(xmean)

