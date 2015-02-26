import sys, os



def CallWikiToConfluenceConverter():
    #Get the directory and import the path for the MarkdownToWikiConverter script
    while True:
        try:
            getPathInput = raw_input('Input path of MarkdownToWikiConverter --> ')
            FULL_PATH = os.path.abspath(getPathInput)
            sys.path.append(FULL_PATH)
            #We can now import the MarkdownToWikiConverter script
            import MarkdownToConfluenceConverter
            break

        
        except Exception:
            print('MarkdownToWikiConverter failed to import, re-enter path')
 

    while True:
        try:
            #Get the directory for usage of the MarkdownToWikiConverter script
            getMarkdownPath = raw_input('Input path of Markdown files --> ')
            #Call function to convert files in specified directory
            MarkdownToConfluenceConverter.convertAllFiles(getMarkdownPath)
            break
        
        except Exception:
            print('Cannot find path specified, re-enter path to Markdown files')

if __name__ == '__main__':
    CallWikiToConfluenceConverter()
