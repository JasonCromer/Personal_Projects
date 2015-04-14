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
from jira.client import JIRA


DATA_CELL = '{urn:schemas-microsoft-com:office:spreadsheet}Data'
TICKET_CELL = '{urn:schemas-microsoft-com:office:spreadsheet}HRef'
INDENT_LEVEL_ID = '{urn:schemas-microsoft-com:office:spreadsheet}StyleID'
ROW_CELL = '{urn:schemas-microsoft-com:office:spreadsheet}Row'
DEFAULT_INPUT_PATH = 'Vera_srs.xml'
DEFAULT_OUTPUT_PATH = 'srsText.txt'
JIRA_USERNAME = 'Jason'
JIRA_PASSWORD = 'Doorsshut1'
LOGIN_WAIT_STRING = 'Logging into Jira Server, please wait..'


class JiraClient:
    '''This class accesses Reflexion Health's Jira server to retrieve information about Issue Types and Properties.
        This is done by logging in through Jira's Basic Authentication (basic_auth), and using that object to get
        a list of Priority Resources as well as a list of Issue Type Resources from the Jira server.
    '''

    def __init__(self):
        self.jiraOptions = {'server' : 'https://projects.reflexionhealth.com'}
        self.authent = JIRA(options = self.jiraOptions, basic_auth = (JIRA_USERNAME, JIRA_PASSWORD))
        self.priorityTypeList = []
        self.issueTypeList = []

    def authentication():
        #Get Jira Reflexion url
        jira_options={'server' : 'https://projects.reflexionhealth.com'}

        #Authorize username and password
        jira = JIRA(options=jira_options, basic_auth=('Jason', 'Doorsshut1'))


    def GetIssuePriorities(self):
        jiraDatabase = self.authent

        #Get Priority resource types from Jira Server (i.e. "1 - Highest Priority")
        jiraPrioritiesList = jiraDatabase.priorities()
        
        for priority in jiraPrioritiesList:
            self.priorityTypeList.append(priority.name)

        return self.priorityTypeList
        


    def GetIssueTypes(self):
        jiraDatabase = self.authent

        #Get Issue resource types from Jira Server (i.e. 'Bug', 'Requirement', ect)
        jiraIssueTypeList = jiraDatabase.issue_types()
        
        for issueType in jiraIssueTypeList:
            self.issueTypeList.append(issueType.name)

        return self.issueTypeList






class RequirementIdentifierStringGenerator:
    '''This class generates requirement strings that identify the indent level of a
        current requirement. It operates as an iterator that adjusts the indentation
        level of your current requirement based on the indent level you tell it that
        you are on. This creates a hierarchical ordered list for each requirement,
        with the requirement string being the prefix of the requirement. I.e:
        A. Top level requirement
            A.1. Second level requirement
                A.1.2. Third Level requirement
                A.1.3. Another Third level requirement
        "A.1" and "A.1.2", ect being the strings that this class generates.
    '''


    def __init__(self,alphabetList):
        self.lists = []
        self.alphaList = alphabetList


    def GetCurrentIdentifierString(self):
        '''Return e.g. "A.1.1.4", based on the contents of 
            self.lists.
        '''

        count = 0
        outputString = ''
        for currentIdentifierLevel in self.lists:
            if count == 0:
                outputString += (self.alphaList[currentIdentifierLevel])
            else:
                outputString += str((currentIdentifierLevel) + 1)

            if count + 1 < len(self.lists):
                outputString += '.'

            count +=1
        return outputString
    

    def SetNextRequirementIndentLevel(self, indentLevel):
        if indentLevel + 1 == len(self.lists):
            self.lists[indentLevel] += 1

        if indentLevel + 1 > len(self.lists):
            self.lists.append(0)

        if indentLevel + 1 < len(self.lists):
            self.lists = self.lists[:indentLevel + 1]
            self.lists[indentLevel] += 1






class IndentCodeList:
    '''A sequential list of the stylesheet codes that correspond to an
       "indent level", which should be both the number of tabs before a
       particular item in a sequential list and the number of ordinals in
       its identifier string. Codes are things like "s54", "s55", "s56".
    '''

    def __init__(self):
        self.idList = [0]


    def Parse(self, inputPath):
        '''This function removes first 3 id's in list because they do not contain any pertinent information.
           Keep zero as first element so numbers are incrimented instead of letters
           and looks like: A.
                            A.1
                             A.1.1
                              A.1.2
                               A.1.3
           instead of: A.
                       B.
                       C.
        '''
        
        self.openFile = open(inputPath)
        for line in self.openFile:
            if re.search('ss:ID="s', line):
                self.idList.append(line[16:-3])
        del self.idList[1:4]



    def GetIndentLevelFromIndentCode(self, indentCodeIn):
        idListIndentLevelIndex = self.idList.index(indentCodeIn)
        return idListIndentLevelIndex

    
    def GetLastIndentLevelFromIDList(self):
        return len(self.idList)-1





    

def CreateAlphaList():
    '''Creates a list of the alphabet for use in the  RequirementIdentifierStringGenerator Class.
    '''
    alphaList = []
    for c in range(ord('A'), ord('Z')+1):
        alphaList.append(chr(c))
    return alphaList




def WriteNumberedList(rowElement, srsText, jiraIssueType, jiraIssuePriorities, indentCodeListIdentifierObject, requirementIdentifierStringGenerator):
    #Sort through Row and find child elements
    for child in rowElement:
        ticketLink = child.attrib.get(TICKET_CELL)
        indentLevelString = child.attrib.get(INDENT_LEVEL_ID)

        #Create Jira List objects for the Issue Types (i.e. Requirement, Bug, ect) and Priority Types (i.e. 1 - Highest Priority, ect)
        jiraIssueTypeList =  jiraIssueType
        jiraIssuePriorityList =  jiraIssuePriorities

        #Exclude ticket number when writing list of srs tickets
        if ticketLink:
            continue
        
        #Check if each Cell contains anything. If not, will return NoneType error, so we avoid it and continue
        if child.find(DATA_CELL) == None:
            continue


        #Assign text content in each Data cell to requirementText
        requirementText = child.find(DATA_CELL).text
        
        if indentLevelString is None:
            if requirementText in jiraIssueTypeList:
                continue
                
            elif requirementText in jiraIssuePriorityList:
                continue
                
            else:
                requirementIdentifierStringGenerator.SetNextRequirementIndentLevel(0)
                srsText.write(requirementIdentifierStringGenerator.GetCurrentIdentifierString() + '. ' + requirementText + '\n')
                continue


        if indentLevelString not in indentCodeListIdentifierObject.idList:
            continue
            
        indentLevel = indentCodeListIdentifierObject.GetIndentLevelFromIndentCode(indentLevelString)
        lastIndentLevel = indentCodeListIdentifierObject.GetLastIndentLevelFromIDList()


        
        if indentLevel == lastIndentLevel:
            #Amount of tabs based on the length of output string. i.e. D.1.2.3 is 4 tabs (we replace . with '' to discount the periods when determining length
            tabPrefixValue = len(requirementIdentifierStringGenerator.GetCurrentIdentifierString().replace('.',''))
            requirementIdentifierStringGenerator.SetNextRequirementIndentLevel(indentLevel)
            srsText.write(('   '*tabPrefixValue) + requirementIdentifierStringGenerator.GetCurrentIdentifierString() + ' ' + requirementText + '\n')
            continue

        
        if indentLevel:
            #Check if text in Cell are an Issue Type or Priority Type. If so, do not write to text file
            if requirementText in jiraIssueTypeList:
                continue

            if requirementText in jiraIssuePriorityList:
                continue

            #If text in Cell is not an Issue Type or Priority Type, proceed
            else:
                requirementIdentifierStringGenerator.SetNextRequirementIndentLevel(indentLevel)
                srsText.write(('   '*indentLevel) + requirementIdentifierStringGenerator.GetCurrentIdentifierString() + ' ' + requirementText + '\n')
                continue

        else:
            print('Too many indents in structure and could not be evaluated for ticket %s' % ticketLink)






def WriteDashedList(rowElement, srsText, jiraIssueType, jiraIssuePriorities, indentCodeListIdentifierObject):
    for child in rowElement:
        ticketLink = child.attrib.get(TICKET_CELL)
        indentLevelString = child.attrib.get(INDENT_LEVEL_ID)

        #Create list objects for Issue Type and Priority types that may be found in the xml
        jiraIssueTypeList =  jiraIssueType
        jiraIssuePriorityList =  jiraIssuePriorities

        if ticketLink:
            continue

        #Check if cell has data. If no data is found in cell avoid it and continue
        if child.find(DATA_CELL) == None:
            continue


        requirementText = child.find(DATA_CELL).text

        
        if indentLevelString is None:
            if requirementText in jiraIssueTypeList:
                continue
                
            if requirementText in jiraIssuePriorityList:
                continue
                
            else:
                srsText.write('- ' + requirementText + '\n')
                continue


        if indentLevelString not in indentCodeListIdentifierObject.idList:
            continue
        

        if indentLevelString:
            #If data in the xml is an Issue Type or Priority Type, avoid it and continue
            if requirementText in jiraIssueTypeList:
                continue
            
            if requirementText in jiraIssuePriorityList:
                continue
            
            else:
                srsText.write('- ' + requirementText + '\n')
                continue
            
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

    #Authenticate Jira Client and create Jira Priority and Issue Type Lists
    print(LOGIN_WAIT_STRING)
    jiraDatabaseObject = JiraClient()
    jiraIssueTypes = jiraDatabaseObject.GetIssueTypes()
    jiraIssuePriorities = jiraDatabaseObject.GetIssuePriorities()

    #Instantiate IndentCodeList and RequirementIdentifierStringGenerator objects
    indentCodeListIdentifier = IndentCodeList()
    indentCodeListIdentifier.Parse(argsobj.input_path)
    RequirementIdentifierStringGeneratorClass = RequirementIdentifierStringGenerator(alphaList)


    #Set XML file path to jiraStructure object
    jiraStructure = etree.parse(argsobj.input_path)
    srsText = open(argsobj.output_path, 'w')

    
    #Loop through each Row in the XML file
    for value in jiraStructure.iter(ROW_CELL):
        if argsobj.dashes == True:
            WriteDashedList(value, srsText, jiraIssueTypes, jiraIssuePriorities, indentCodeListIdentifier)
        elif argsobj.numbered == True:
            WriteNumberedList(value, srsText,  jiraIssueTypes, jiraIssuePriorities, indentCodeListIdentifier, RequirementIdentifierStringGeneratorClass)
        else:
            print('Please check that input matches directory and file name')

if __name__ == '__main__':
    main()
