#Program that converts numbers to Binary and hex

def binaryConvert(binaryNumberInput):
    binNum = bin(binaryNumberInput)
    return binNum



def hexConvert(hexNumberInput):
    hexNum = hex(hexNumberInput)
    return hexNum



def main():
    print('Please enter your number: ')
    num = input()
    binaryNumber = binaryConvert(num)
    hexNumber = hexConvert(num)
    print('Your number in binary is: ' + str(binaryNumber) + ' and your number in hexadecimal is: ' + str(hexNumber))

if __name__ == '__main__':
    main()
