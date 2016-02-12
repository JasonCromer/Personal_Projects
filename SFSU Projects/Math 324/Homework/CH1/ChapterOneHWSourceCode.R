#Chapter One Source Code
#Jason Cromer
#2-9-16
#Math 324 Section 03


##########SECTION 1.2##########

#11
x = c(.31,.35,.36,.36,.37,.38,.40,.40,.40,.41,.41,.42,.42,.42,.42,.42,.43,.44,.45,.46,.46,.47,.48,.48,.48,.51,.54,
     .54,.55,.58,.62,.66,.66,.67,.68,.75)
stem.leaf(x)


##########SECTION 1.3##########

#36
oilData = c(389,356,359,363,375,424,325,394,402,373,373,370,364,366,364,325,339,393,392,369,374,359,356,403,334,397)
stem.leaf(oilData) 
mean(oilData)
median(oilData)
oilDataInMinutes = oilData / 60
mean(oilDataInMinutes)
median(oilDataInMinutes)

#39
flightData = c(.736,.863,.865,.913,.915,.937,.983,1.007,1.011,1.064,1.109,1.132,1.140,1.153,1.253,1.394)
mean(flightData)
median(flightData)


##########SECTION 1.4##########

#44
polymerData = c(180.5,181.7,180.9,181.6,182.6,181.6,181.3,182.1,182.1,180.3,181.7,180.5)
rangeVector = range(polymerData)
range = diff(rangeVector)
variance = var(polymerData)
standardDeviation = sd(polymerData)

