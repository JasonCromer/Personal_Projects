# Copyright 2014 Reflexion Health
# All rights reserved.

#
# BuildNotesFromGitOutput.py
#
#	Generates notes on a build in Markdown from a git difference line.
#
#	Git line example:
#
#		$ git log bc23977..fd2a1d5 --no-merges --pretty=format:"%h|%an|%ad" --date=short

import os, sys, re
import argparse

DESCRIPTION = "Generates notes on a build in Markdown from a git difference line."
DEFAULT_INPUT_PATH = "gitOutput.txt"
FIELD_DELIMITER = "|"

def WikiToConfluenceConverter():
        argsobj = Parse() 
        while True:
                try:
                    getPathInput = raw_input('Input path of MarkdownToWikiConverter --> ')
                    FULL_PATH = os.path.abspath(getPathInput)
                    sys.path.append(FULL_PATH)
                    #We can now import the MarkdownToWikiConverter script
                    import MarkdownToConfluenceConverter
                    break
                except Exception:
                    print('\nMarkdownToWikiConverter failed to import, re-enter path')
 

        while True:
                try:
                    getMarkdownPath = raw_input('Input path of Markdown files --> ')
                    #Call function to convert files in specified directory
                    MarkdownToConfluenceConverter.convertAllFiles(getMarkdownPath)
                    break
                except Exception:
                    print('\nCannot find path specified, re-enter path to Markdown files')

class AuthorInformationStruct:

	def __init__( self, authorRE, authorProperName, reviewerName, reviewDate ):
		self.authorRE = authorRE
		self.authorProperName = authorProperName
		self.reviewerName = reviewerName
		self.reviewDate = reviewDate


VALID_AUTHOR_LIST = [
										#Regexp of author 			Author proper name 	Reviewer 		Date of review
	AuthorInformationStruct( re.compile( "mark", re.IGNORECASE ), 	"Mark Barrett", 	"Andrew Hilvers", "12/16/2014" ),
	AuthorInformationStruct( re.compile( "drew", re.IGNORECASE ), 	"Andrew Hilvers", 	"Mark Barrett", "12/16/2014" ),
	AuthorInformationStruct( re.compile( "thoth", re.IGNORECASE ), 	"Azazel Thoth", 	"Mark Barrett", "12/16/2014" ),
	AuthorInformationStruct( re.compile( "sam", re.IGNORECASE ), 	"Sam Edwards", 		"Mark Barrett", "12/16/2014" ),
	AuthorInformationStruct( re.compile( "richard", re.IGNORECASE ), "Richard Hicks", 	"Mark Barrett", "12/16/2014" ),
]

MARKDOWN_PREFIX = r"""# Unity Patient App, 2.0.2

Reviewers certify that they have reviewed all the indicated software coding and found no major defects. All (if any) coding issues have been tracked for resolution as required.

| Date of Commit | Commit ID | Author | Reviewer | Date of Review |
| -------------- | --------- | ------ | -------- | -------------- |
"""

def PopulateRevisionList( inputPath, outputStream=None ):

	if outputStream is None:
		outputStream = sys.stdout

	outputStream.write( MARKDOWN_PREFIX )

	with open( inputPath ) as f:

		outputStringList = []

		for line in f:
			( thisRevisionNumber, thisAuthor, thisGitDate ) = line.rstrip().split( FIELD_DELIMITER )

			for validAuthor in VALID_AUTHOR_LIST:
				if( validAuthor.authorRE.search( thisAuthor ) ):
					validatedAuthorName = validAuthor.authorProperName
					reviewerName = validAuthor.reviewerName
					reviewDate = validAuthor.reviewDate
					break
			else:
				print( "DEBUG: Author {thisAuthor} is not valid!".format( **vars() ) )
				continue

			thisMarkdownDate = thisGitDate.replace( "-", "/" )

			outputStringList.append( "| {thisMarkdownDate} | {thisRevisionNumber} | {validatedAuthorName} | {reviewerName} | {reviewDate} |".format( **vars() ) )

		outputStream.write( "\n".join( outputStringList ) )


def Parse():
	parser = argparse.ArgumentParser( description = DESCRIPTION )

	parser.add_argument( '-i', '--input_path', type=str, default=DEFAULT_INPUT_PATH, help="Path to the git log file." )
	parser.add_argument( '-o', '--output_path', type=str, default=None, help="Path to the markdown output file.")

	args = parser.parse_args()
	return args




def main():
	argsobj = Parse()

	f = None
	if( argsobj.output_path is not None ):
		f = open( argsobj.output_path, "w" )

	revisionList = PopulateRevisionList( argsobj.input_path, f )


if __name__ == '__main__':
	main()
	WikiToConfluenceConverter()
	
