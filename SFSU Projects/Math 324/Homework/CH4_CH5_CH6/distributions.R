
#Distributions
 
#Exponential distribution
#Usage

#dexp(x, rate = 1) #Density
#pexp(q, rate = 1, lower.tail = TRUE) # distribution function,
#qexp(p, rate = 1, lower.tail = TRUE)# quantile function
#rexp(n, rate = 1)#random generation for the exponential distribution with rate=lambda

pexp(1, 2) #(Probability P(X<1), when X belong Exp(2))

qexp(0.5, 2) # median of the X, X belong Exp(2)

ind <- c(5, 4, 3,2,1)
for (i in ind) curve(dexp(x, rate = i), -0.5, 5,col=i,  add = TRUE)

# or you can plot density as follwos
x=seq(0,4,length=200)
y=dexp(x,rate=1)
plot(x,y,type="l",lwd=2,col="red",ylab="f(x)")
####################################################################################
# Gamma distribution

#dgamma(x, shape, rate = 1, scale = 1/rate)# pdf of gamma distribution, 
#shape=alpha, rate=lambda=1/beta, scale=beta=1/rate


pgamma(q, shape, rate = 1, scale = 1/rate, lower.tail = TRUE) # cdf of gamma distribution
qgamma(p, shape, rate = 1, scale = 1/rate, lower.tail = TRUE)# quantiles
rgamma(n, shape, rate = 1, scale = 1/rate)# random numbers

shapes=c(2,5,6,12)
for (i in seq_along(shapes))
  curve(dgamma(x,shape= shapes[i] , scale = 1) , from=0, to=30, col=i, add=TRUE)


scales=c(2, 1/3, 1/2, 1)
for (i in seq_along(scales))
  curve(dgamma(x, shape=3 , scale = scales[i]) , from=0, to=10,ylim=c(0,1),  col=i, add=TRUE)

############################################################




#Chi-squared distribution
#If X1,X2,…,Xm are m independent random variables having the standard normal distribution,
# then the following quantity follows a Chi-Squared distribution with m degrees of freedom. 
#Its mean is m, and its variance is 2m.

#V = X1^2 + X2^2 +....+ Xm^2 ~ ?2(m)


#dchisq(x, df, ncp = 0) #density
#pchisq(q, df, ncp = 0, lower.tail = TRUE)#cdf
#qchisq(p, df, ncp = 0, lower.tail = TRUE)#quantiles
#rchisq(n, df, ncp = 0)#random numbers

dfs=c(1,3,5,7)
for (i in seq_along(dfs))
  curve(dchisq(x,df=dfs[i]) , from=0, to=18, col=i,ylim=c(0,1),  add=TRUE)


ncps=c(0,1,2,3)
for (i in seq_along(dfs))
  curve(dchisq(x,df=4, ncp=ncps[i]) , from=0, to=18, col=i,ylim=c(0,1),  add=TRUE)


pchisq(2,4) #( Calculates P(X<2) when X has Chisq with df=4)
pchisq(2,4, lower.tail=FALSE)#Calculates P(X>2) when X has Chisq with df=4)
qchisq(0.4,4) # Calculates point a such that P(X<=a)=0.4)

#######################################################################################################

#Weibull distribution

dweibull(x, shape, scale = 1)#pdf of weibull distribution shape=alpha, scale=beta
pweibull(q, shape, scale = 1, lower.tail = TRUE)#cdf
qweibull(p, shape, scale = 1, lower.tail = TRUE)#quantiles
rweibull(n, shape, scale = 1) #random numbers




shapes=c(1,2,6,10)
for (i in seq_along(shapes))
  curve(dweibull(x,shape= shapes[i] , scale = 1) , from=0, to=4, ylim=c(0,4), col=i, add=TRUE)


scales=c(1/3, 1/2, 1,2)
for (i in seq_along(scales))
  curve(dweibull(x, shape=3 , scale = scales[i]) , from=0, to=4, ylim=c(0,4),  col=i, add=TRUE)
###################################################################################################################

#Lognormal distribution

 #if the random variable X is log-normally distributed, 
#then Y = log(X) has a normal distribution. 
#Likewise, if Y has a normal distribution, then X = exp(Y) has a log-normal distribution.
# A random variable which is log-normally distributed takes only positive real values.

dlnorm(x, meanlog = 0, sdlog = 1)#pdf
plnorm(q, meanlog = 0, sdlog = 1, lower.tail = TRUE)#cdf
qlnorm(p, meanlog = 0, sdlog = 1, lower.tail = TRUE)#quantiles
rlnorm(n, meanlog = 0, sdlog = 1)#random numbers



meanlogs=c(0,0.5,1,2)
for (i in seq_along(meanlogs))
  curve(dlnorm(x,meanlog=meanlogs[i],1) , from=0, to=10, col=i,ylim=c(0,0.8),  add=TRUE)


sdlogs=c(0.5,1,2,3)
for (i in seq_along(dfs))
  curve(dlnorm(x,meanlog=1,sdlog=sdlogs[i]) , from=0, to=10, col=i,ylim=c(0,0.8),  add=TRUE)
