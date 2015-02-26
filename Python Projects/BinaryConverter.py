#Program that converts numbers to Binary

def binary_convert():
    print("Please choose a number less than 10,000")
    num = input()
    print("hello")
    if num < 10000:

        a = num/8192
        if a == 1:
            num = num - 8192
        b = num/4096
        if b == 1:
            num = num - 4096
        c = num/2048
        if c == 1:
            num = num - 2048
        d = num/1024
        if d == 1:
            num = num - 1024
        e = num/512
        if e == 1:
            num = num - 512
        f = num/256
        if f == 1:
            num = num - 256
        g = num/128
        if g == 1:
            num = num - 128
        h = num/64
        if h == 1:
            num = num - 64
        i = num/32
        if i == 1:
            num = num - 32
        j = num/16
        if j == 1:
            num = num - 16
        k = num/8
        if k == 1:
            num = num - 8
        l = num/4
        if l == 1:
            num = num - 4
        m = num/2
        if m == 1:
            num = num - 2
        n = num/1
        if n == 1:
            num = num - 1  
        print(a,b,c,d,e,f,g,h,i,j,k,l,m,n)



def hex_convert():
    print("Please choose any number to convert to hex")
    num = input()
    hex_num = hex(num)
    print(hex_num)
    return hex_num


x = True
while x:
    binary_convert()
    hex_convert()
