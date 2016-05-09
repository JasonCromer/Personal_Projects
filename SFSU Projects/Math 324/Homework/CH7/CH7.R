################ Chapter 7 Homework ####################################

################ Jason Cromer | MATH 324-06 | 5-6-16 ###################



############# 6 ############
sigma = 100

###a.)
sampleSize = 25
yieldPoint = 8439
confidenceLevel = .90

#Find z value for confidence percent
zalpha = qnorm(confidenceLevel)

#Calculate lower and upper bound
lowerBound = yieldPoint - (zalpha * (sigma/sqrt(sampleSize)))
upperBound = yieldPoint + (zalpha * (sigma/sqrt(sampleSize)))

#calculate confidence Interval
confidenceInterval = c(lowerBound,upperBound)

###b.)
#The way to calculate a 92% confidence level would be do find the new critical point.
#This can be done by looking for it on the z-table or using qnorm(.92).




############## 16 ##############
data = c(62,50,53,57,41,53,55,61,59,64,50,53,64,62,50,68,54,55,57,50,55,50,56,55,46,55,53,54,52,47,47,55,
         57,48,63,57,57,55,53,59,53,52,50,55,60,50,56,58)

###a.) 
boxplot(data)
#The boxplot seems to have a couple outliers and a slightly skewed distribution.

###b.)
sampleMean = mean(data)
dataLength = length(data)
sigma = sd(data)

#use confidence level of 95%
zalpha = qnorm(.95)

#calculate upper and lower bound
lowerBound = sampleMean - (zalpha * (sigma/sqrt(dataLength)))
upperBound = sampleMean + (zalpha * (sigma/sqrt(dataLength)))

#Calculate confidence interval
confidenceInterval = c(lowerBound,upperBound)
confidenceInterval

#Yes, it appears to me, given the data and the boxplot, that the interval is accurate.

###c.)



############### 22 ####################
sampleSize = 142
defective = 10
proportion = defective/sampleSize

###a.)
se=-qnorm((.95)/2) * sqrt(proportion * (1-proportion)/sampleSize)

#calculate lowerbound
lowerBound = c(proportion - se)

###b.)
#The lowerBound confidence level in part a seems to correctly correspond the data set 
# and the proportion calculated



############## 33 ####################
data = c(418,421,421,422,425,427,431,434,437,439,446,447,448,453,454,463,465)

###a.)
boxplot(data)
#The boxplot seems to show nearly no skew, and to contain no outliers. The deviations seem slightly large
#on the high end.

###b.)
#Yes, given the lack of a skew, the data seems to be normally distributed

###c.)
sampleMean = mean(data)
dataLength = length(data)
sigma = sd(data)
confidenceLevel = qt(.95,dataLength-1)

#calculate lower and upper bound
lowerBound = sampleMean - (confidenceLevel * (sigma/sqrt(dataLength)))
upperBound = sampleMean + (confidenceLevel * (sigma/sqrt(dataLength)))

#calculate confidence level
confidenceLevel = c(lowerBound, upperBound)

#Since the confidence level is from 431.88 to 444.70, 440 would be a plausable value for a true average
#degree of polymerization. However, 450 would not, as it does not fall into the lower or upper bound.
          


############### 36 ####################
n = 26
sampleMean = 370.69
sigma = 24.36

###a.)
confidenceLevel = qt(.95, n-1)

#Calculate upperbound of data
upperBound = sampleMean + (confidenceLevel * (sigma/sqrt(n)))


