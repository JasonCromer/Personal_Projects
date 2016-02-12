# simple example
x=3 
y=5
x + y

#Vector
Numb=c(2,4,-1.4)


seq(0,3,length=10) 
1:10 
rep(c(1,2,3), each=3) 


rep(c(1,2,3), times=3) 

#######Graphicla summary###########################################

#input data

glucose = c(81, 85, 93, 93, 99, 76, 75, 84, 78, 84, 81, 82, 89,
+ 81, 96, 82, 74, 70, 84, 86, 80, 70, 131, 75, 88, 102, 115,
+ 89, 82, 79, 106)
########################################################################

stem(glucose)

#############################
#install.packages("aplpack")
library("aplpack")
PtDepth=c(0.41, 0.41, 0.41, 0.41, 0.43, 0.43, 0.43, 0.48, 0.48,
           0.58, 0.79, 0.79, 0.81, 0.81, 0.81, 0.91, 0.94, 0.94,
           1.02, 1.04, 1.04, 1.17, 1.17, 1.17, 1.17, 1.17, 1.17,
           1.17, 1.19, 1.19, 1.27, 1.40, 1.40, 1.59, 1.59, 1.60,
           1.68, 1.91, 1.96, 1.96, 1.96, 2.10, 2.21, 2.31, 2.46,
           2.49, 2.57, 2.74, 3.10, 3.18, 3.30, 3.58, 3.58, 4.15,
           4.75, 5.33, 7.65, 7.70, 8.13, 10.41, 13.44)
stem.leaf(PtDepth, trim.outliers=T, Max=8.15, m=1)
#or

PitDepth=read.table("D:\\Dropbox\\classes\\SFSUclasses\\324math\\324_Spring16\\ch1-ch9data\\CH01\\ex01-77.txt", header=T)
Pt=as.vector(as.matrix(PitDepth))
stem.leaf(Pt, trim.outliers=T, Max=8.15, m=1)


####################################################################
getwd() # 
setwd("C:\\Users\\alpiryat\\Dropbox\\classes\\324R")
source("dotplot.R")
 dotplot(glucose)
################################################################

hist(glucose)
hist(glucose, breaks = 14)
 hist(glucose, breaks = seq(60, 140, by = 8), col = 2)
#####################################################################


preen = c(34, 24, 10, 16, 52, 76, 33, 31, 46, 24, 18, 26, 57,
+ 32, 25, 48, 22, 48, 29, 19)
 stem(preen)

stem(preen, scale = 2)
##############################################################################
#Bar chart 
Categ=c("A", "B", "C", "D","F", "Don't_know")
Freq=c(478, 893, 680, 178, 100, 172)

barplot(Freq, main="School rating data",names.arg=Categ)

barplot(Freq, main="School rating data",names.arg=Categ, horiz="T")

# Simple Pie Chart

pie(Freq, labels = Categ, main="Pie Chart, School Rating data")
################################################################################

#Measures of Locations: Example 1.14 textbook
#Crack length data
Crack=c(16.2, 9.6, 24.9,20.4,12.7, 21.2, 30.2,
     25.8,  18.5, 10.3, 25.3, 14.0, 27.1, 45.0,
  23.3, 24.2, 14.6,  8.9, 32.4, 11.8, 28.5 )


mean(Crack)
median(Crack)
var(Crack)
sd(Crack)
fivenum(Crack)
summary(Crack)
###################################
#boxplot, Ex.1.19
ultra=c(40, 52, 55, 60, 70, 75,
        85, 85, 90, 90, 92, 94, 94, 95, 98, 100, 115, 125, 125)
boxplot(ultra)



#Example 1.20. Outliers

Water=c(9.69, 13.16, 17.09, 18.12, 23.70, 24.07, 24.29, 26.43,
        30.75, 31.54, 35.07, 36.99, 40.32, 42.51, 45.64, 48.22,
        49.98, 50.06, 55.02, 57.00, 58.41, 61.31, 64.25, 65.24,
        66.14, 67.68, 81.40, 90.80, 92.17, 92.42,100.82, 101.94,
        103.61, 106.28, 106.80, 108.69, 114.61, 120.86, 124.54,  143.27,
        143.75, 149.64, 167.79, 182.50, 192.55, 193.53, 271.57, 292.61,
        312.45, 352.09, 371.47, 444.68, 460.86, 563.92, 690.11, 826.54,
        1529.35)

boxplot(Water)

############################################
boxplot(preen, col = 3)


######################################################
 M = c(6, 0, 2, 1, 2, 4.5, 8, 3, 17, 4.5, 4, 5)
F = c(5, 13, 3, 2, 6, 14, 3, 1, 1.5, 1.5, 3, 8, 4)
 boxplot(list(Male = M, Female = F))
#########################################################################################

 growthDark = c(15, 20, 11, 30, 33, 22, 37, 20, 29, 35, 8, 10,
+ 15, 25)
growthLight = c(10, 15, 22, 25, 9, 15, 4, 11, 20, 21, 27, 20,
+ 10, 20)
boxplot(list(dark = growthDark, light = growthLight))
##########################################################################################
cat=c(rep("M", 12), rep("F",13))
value=rbind(as.matrix(M), as.matrix(F))
dat=data.frame(cat, value)

boxplot(value ~ cat, data = dat) 

#Numerical summary########################################
mean(preen)
sd(preen)
var(preen)
median(preen)
 max(preen)
min(preen)
 fivenum(preen)
10 23 30 47 76

 IQR(preen)
#Here is a sample calculation to find the fences for a modied boxplot.
 fnum = fivenum(preen)
 iqr = IQR(preen)
 fnum[2] - 1.5 * iqr
 fnum[4] + 1.5 * iqr



####################################################################

library(MASS)
data(UScereal)
head(UScereal)
summary(UScereal)

UScereal$shelf=as.factor(UScereal$shelf)
summary(UScereal)

attach(UScereal)
 hist(sodium) 
 abline(v=mean(sodium),lty=3)
 plot(potassium, protein) 

boxplot(sugars) 
#####################################################################
# read data
dat=read.table("C:/Users/alpiryat/Dropbox/classes/324R/state.txt", sep=" ",  header=T,row.names=1)

plot(dat$Population, dat$Murder)

##############################################################################################
uk2007 = data.frame(Commodity =
                      factor(c("Cow milk", "Wheat", "Sugar beet", "Potatoes", "Barley"),
                             levels = c("Cow milk", "Wheat", "Sugar beet", "Potatoes", "Barley")),
                    Production = c(14023, 13221, 6500, 5635, 5079))

barplot(uk2007$Production, names = uk2007$Commodity,
       xlab = "Commodity", ylab = "Production (1,000 MT)",
       main = "UK 2007 Top 5 Food and Agricultural Commodities")

#We specify the data frame where the data is stored and then use the aes 
#argument to identify the x and y variables. The geom_bar function is used
#to create a bar chart display with the specified data and the last three
#options in the example are for creating the various labels to be added to the graph.

#The graph itself is constructed piece by piece to add the various layers and components on top of the base layer:
  
  ggplot(uk2007, aes(Commodity, Production)) + geom_bar() + xlab("Commodity") +
  ylab("Production (1,000 MT)") +
  opts(title = "UK 2007 Top 5 Food and Agricultural Commodities")


#################################################################################
# Simple Pie Chart
slices <- c(10, 12,4, 16, 8)
lbls <- c("US", "UK", "Australia", "Germany", "France")
pie(slices, labels = lbls, main="Pie Chart of Countries")



# Pie Chart with Percentages
slices <- c(10, 12, 4, 16, 8) 
lbls <- c("US", "UK", "Australia", "Germany", "France")
pct <- round(slices/sum(slices)*100)
lbls <- paste(lbls, pct) # add percents to labels 
lbls <- paste(lbls,"%",sep="") # ad % to labels 
pie(slices,labels = lbls, col=rainbow(length(lbls)),
    main="Pie Chart of Countries")



# 3D Exploded Pie Chart
library(plotrix)
slices <- c(10, 12, 4, 16, 8) 
lbls <- c("US", "UK", "Australia", "Germany", "France")
pie3D(slices,labels=lbls,explode=0.1,
      main="Pie Chart of Countries ")


# Pie Chart from data frame with Appended Sample Sizes
mytable <- table(iris$Species)
lbls <- paste(names(mytable), "\n", mytable, sep="")
pie(mytable, labels = lbls, 
    main="Pie Chart of Species\n (with sample sizes)")



######################



