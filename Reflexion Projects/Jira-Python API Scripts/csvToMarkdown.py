#                                 How this program works
#
# This program reads the contents of a csv file, then queries Jira to obtain a list of ID's that
# match the contents of the csv file. These ID's, and the contents of the csv file, are then used
# to write a list of issues and their matching ID's, in Markdown format, to a text file
#
#
#                                 Instructions
#
# 1. Log into Jira and export the Requirements Structure into an excel file
# 2. Convert the excel file into a csv (comma seperated values) file 
# 3. Pass whatever you've named the csv file as a command-line arg.
#



import csv, sys, getpass
import argparse
from jira.client import JIRA


DEFAULT_INPUT_PATH = 'veraSrs.csv'
DEFAULT_OUTPUT_PATH = 'srsList.txt'
DESCRIPTION = 'Writes csv file list to text file'
JIRA_SERVER = 'https://projects.reflexionhealth.com'


def GetUsernameAndPassword(userName):
   if userName is None:
      userName = raw_input('Username: ')
   password = getpass.getpass()
   return userName, password


def Authentication(userName, password):
   jiraOptions={'server' : JIRA_SERVER}
   #Authorize username and password
   jira = JIRA(options=jiraOptions, basic_auth=(userName, password))
   return jira
      

def GetCodingTraceabilitySourceFilesString( issueIn ):
    return str( issueIn.fields.customfield_11000 )


def GetCodingTraceabilitySourceFileKeys( issueIn ):
    return str( issueIn.key )
   

def Parse():
   parser = argparse.ArgumentParser(description = DESCRIPTION)
   parser.add_argument('-i', '--input_path', type=str, default=DEFAULT_INPUT_PATH, help='Path to csv file')
   parser.add_argument('-o', '--output_path', type=str, default=DEFAULT_OUTPUT_PATH, help='Path to markdown output txt file')
   parser.add_argument('-u', '--jira_username', type=str, default=None, help='Jira username')
   args = parser.parse_args()
   return args
   

def ReadAndWrite():
   argsobj = Parse()
   
   #Loop through Authentication until credentials are validated
   while True:
      userName, password = GetUsernameAndPassword(argsobj.jira_username)
      try:
         jira = Authentication(userName, password)
      except Exception as e:
         print('Wrong Username or Password, please re-enter: ')
      else:
         break
      
   #Query custom source files and the type Requirement
   #issues returns list: <key='', id=''>
   issues = jira.search_issues('"Coding Traceability Source File(s)" IS NOT EMPTY AND type=Requirement ', startAt=0, maxResults=1000)
   
   #Initialize dictionary
   keyAndAssociatedFiles = {}
    
   #sort the Coding Traceability Source Files(s) fields
   for issue in issues:
      thisSourceFileString = GetCodingTraceabilitySourceFilesString( issue )
      thisSourceFileKey = GetCodingTraceabilitySourceFileKeys( issue )
      keyAndAssociatedFiles[ thisSourceFileKey ] = thisSourceFileString

   # Open text file and Initiate variables
   argsobj = Parse()
   srsList = open(argsobj.output_path, 'w')

   # Try-catch block to read and write .csv content rows
   with open(argsobj.input_path, 'r') as f:
      reader = csv.reader(f)
      try:
         for row in reader:
               #each row a list ['<ticketnumber>', '<summary>'] (no <s or >s) and must use indexes to split and strip
               thisTicketNumber, thisSummary = row[0], row[1]
               thisTicketNumber = thisTicketNumber.strip("'")
               thisSummary = thisSummary.strip("'")
               FORMAT_STRING = "| {0} | {1} | {2} |"
               if ( thisTicketNumber in keyAndAssociatedFiles ):
                  outputString = FORMAT_STRING.format(thisTicketNumber, thisSummary, keyAndAssociatedFiles[ thisSourceFileKey ] )
                  srsList.write( outputString + '\n')
         print('Success! Your list is now available.')
         srsList.close()
      except csv.Error as e:
         sys.exit('file %s, line %d: %s' % (argsobj.input_path, reader.line_num, e))
         srsList.close()


if __name__ == '__main__':
   ReadAndWrite()

