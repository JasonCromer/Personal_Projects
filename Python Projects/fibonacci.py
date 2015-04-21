





def GetFibNumber(nthNumber):
    try:
        assert nthNumber > 0
    except AssertionError:
        print '\nERROR: Number must be greater than 0\n'
        main()

    series = [1]

    while len(series) < nthNumber:
        if len(series) == 1:
            series.append(1)
        else:
            series.append(series[-1] + series[-2])


    for i in range(len(series)):
        series[i] = str(series[i])


    neatSeries = ', '.join(series)
    print neatSeries



def main():
    inputNumber = input('How many fibonacci numbers do you want? ')
    GetFibNumber(inputNumber)





if __name__ == '__main__':
    main()
