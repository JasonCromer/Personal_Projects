#                                                   How This Script Works
#                                                   ---------------------
#   This script takes this input(XML) and output(TXT) file as parameters, reads the input file, and
#   searches for the Jira Structure summaries and indentation ID's within the input file. By default, it then cycles through each cell,
#   appends each indent to a list, resets the previous indent list, and continues cycling through to create an ordered list.
#   It then writes these summaries with the proper indentation respective to the Jira Structure file. Optionally, you can
#   select -d True in the command prompt to write each summary with only a dash preceding it, still retaining the proper order
#   of the Jira Structure.
#
#                                                   How To Use This Script
#                                                   ----------------------
#   First, the user must log into Jira, and export a Structure as an excel file. After exporting, locate the excel
#   file in your saved directory, open it, and save it as an 'XML Spreadsheet 2003 (*.xml)'. This new xml file
#   will now be the one used in this script. By default, the input and output files will be searched for and written
#   in the local directory as 'srsXml.xml' and 'srsText.txt'. If the XML and TXT files are not in the local
#   directory, please indicate the directory that they are located in via the -i and -o function for input file path
#   and output file path respectively. 


import xml.etree.ElementTree as etree
import argparse, re


DATA_CELL = '{urn:schemas-microsoft-com:office:spreadsheet}Data'
TICKET_CELL = '{urn:schemas-microsoft-com:office:spreadsheet}HRef'
INDENT_LEVEL_ID = '{urn:schemas-microsoft-com:office:spreadsheet}StyleID'
ROW_CELL = '{urn:schemas-microsoft-com:office:spreadsheet}Row'
DEFAULT_INPUT_PATH = 'srsXml.xml'
DEFAULT_OUTPUT_PATH = 'srsText.txt'
MICROSOFT_TAB = '    '


def CreateAlphaList():
    alphaList = []
    for c in range(ord('A'), ord('Z')+1):
        alphaList.append(chr(c))
    return alphaList


def GetIDList(argsobj):
    idList =[]
    thisFile = open(argsobj.input_path)
    for line in thisFile:
        if re.search('ss:ID="s', line):
            #16:-3 signifies the area in this line that contains the ids, i.e. s24 or s25
            idList.append(line[16:-3])
    return idList, len(idList)


def WriteNumberedList(rowElement, idList, thisList, srsText, alphabetList):
    #Sort through Row and find child elements
    for child in rowElement:
        ticketLink = child.attrib.get(TICKET_CELL)
        indents = child.attrib.get(INDENT_LEVEL_ID)
        #Exclude ticket number when writing list of srs tickets
        if ticketLink:
            continue
        #Exlude 'Key', 'Summary' and blank header in list of srs tickets
        if indents == (idList[0] or idList[1] or idList[2]):
            continue

        if indents == idList[-1]:
            srsText.write(MICROSOFT_TAB*3 + str(len(thisList[1])) + '.' + str(len(thisList[2])) + '.' + str(len(thisList[3]) + 1) + '. ' + child.find(DATA_CELL).text + '\n')
            continue
        
        if indents is not None:
            for i in idList:
                if indents == i:
                    #idElement is the element in the array that is identical to the 'indents' value i.e. s65, s66, s67, ect
                    idElement = idList.index(indents)
                    #append to element-2 list since the first three elements in idList are not used, and idList[0] belongs to titles that contain no indents
                    thisList[idElement-2].append(1)
                    #Clear previous list to restart number count
                    thisList[idElement-1] = []
                    srsText.write(MICROSOFT_TAB*(idElement-2))
                    #End range at idElement-2 to eliminate extra zeroes after numbers
                    for j in range(idElement-2):
                        #use j+1 element to retain correct ordering
                        srsText.write(str(len(thisList[j+1])) + '.')
                    srsText.write(' ' + child.find(DATA_CELL).text + '\n')
            continue
        
        if indents is None:
            #create list of Main titles
            thisList[0].append(1)
            thisList[1] = []
            #For every main title, add to index[-1] of alphaList so that main titles are alphabetically inserted
            srsText.write(alphabetList[-1 + len(thisList[0])] + '. ' + child.find(DATA_CELL).text + '\n')
            continue

        else:
            print('Too many indents in structure and could not be evaluated for ticket %s' % ticketLink)


def WriteDashedList(rowElement, idList, srsText):
    for child in rowElement:
        ticketLink = child.attrib.get(TICKET_CELL)
        indents = child.attrib.get(INDENT_LEVEL_ID)
        if ticketLink:
            continue
        if indents == (idList[0] or idList[1] or idList[2]):
            continue

        if not ticketLink:
            srsText.write('- ' + child.find(DATA_CELL).text + '\n')
            
        else:
            print('Too many indents in structure and could not be evaluated for ticket %s' % ticketLink)


def Parse():
    #Create input and output path parameters
    parser = argparse.ArgumentParser(description = 'Writes Structure to Text File')
    parser.add_argument('-i', '--input_path', type=str, default=DEFAULT_INPUT_PATH, help='Path to xml file')
    parser.add_argument('-o', '--output_path', type=str, default=DEFAULT_OUTPUT_PATH, help='Path to txt file')
    parser.add_argument('-d', '--dashes', type=bool, default=False, help='Indicate True or False for dashes per each ticket')
    parser.add_argument('-n', '--numbered', type=bool, default=True, help='Indicate True or False for numbered tickets')
    args = parser.parse_args()
    return args


def main():
    argsobj = Parse()
    alphaList = CreateAlphaList()
    idList, lengthOfIdList = GetIDList(argsobj)
    #Generate a list for every indent ID, and create each of these in a main list called listOfLists
    listOfLists = [[] for x in range(lengthOfIdList)]
    #Set XML file path to TREE object
    TREE = etree.parse(argsobj.input_path)
    srsText = open(argsobj.output_path, 'w')
    #Loop through each Row in the XML file
    for value in TREE.getiterator(ROW_CELL):
        if argsobj.dashes == True:
            WriteDashedList(value, idList, srsText)
        else:
            WriteNumberedList(value, idList, listOfLists, srsText, alphaList)

if __name__ == '__main__':
    main()
