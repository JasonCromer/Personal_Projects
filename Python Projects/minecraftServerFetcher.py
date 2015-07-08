import urllib2, re


SERVER_ADDRESS_LIST = []
URL = 'http://www.minecraftserverlist.eu/'


def getWebsiteResponse():
	#Create header as User-Agent so we are allowed to access webpage
    request = urllib2.Request(URL, headers={'User-Agent' : "Chrome"})
    response = urllib2.urlopen(request).read()
    return response


def getServerIPAddresses(webResponse):
    #find all strings that follow 'IP: ', \S matching any non-whitespace characters
    minecraftAddresses = re.findall('(?<=IP: )[\S]*', webResponse)

    for ipAddress in minecraftAddresses:
        #Since strings contain '<br/>' remove it
        ipAddress =  re.sub('(<br ?(/>)?)', '', ipAddress)
        SERVER_ADDRESS_LIST.append(ipAddress)
    return SERVER_ADDRESS_LIST


def main():
    webResponse = getWebsiteResponse()
    serverAddressList = getServerIPAddresses(webResponse)
    print serverAddressList


if __name__ == '__main__':
    main()
