
#                               How To Use
#                               ----------
# Place this script in the directory of the desired file(s) that you would
# like to convert from Markdown to the Wiki Confluence Format.
#
#                               How It Works
#                               ------------
# This script reads through each Markdown file and removes all Markdown-ish
# formatting, then replaces it with the Wiki Confluence Formatting. Your
# original Markdown file will be kept, and an identical text file containing
# the converted content will be inserted in the same directory.

 
import re, os, sys
 
def convertText(c):
    # replace all links
    #c = re.sub(r'\[([^\]]+)\]\(([^\]]+)\)', r'[\1|\2]', c)
    c = re.sub(r'\[(.*?)\]\((.*?)\)', r'[\1|\2]', c)
 
    # replace bold temporarily
    c = re.sub(r'\*\*(.*?)\*\*', r'bdirkb\1bdirkb', c)
    
    # replace italics
    #c = re.sub(r'\*(.*?)\*', r'_\1_', c)
    
    # replace bold
    c = re.sub(r'bdirkb(.*?)bdirkb', r'*\1*', c)
 
    # replace inline code
    c = re.sub(r'`(.*?)`', r'\1', c)
 
    # print c
    c = c.split('\n')
 
    words = []
    words.append( ['`',''] )
    words.append( ['   *','#*'] )
    words.append( ['1. *','# *'] )
    words.append( ['#','h1.'] )
    words.append( ['##','h2.'] )
    words.append( ['###','h3.'] )
    words.append( ['####','h4.'] )
    words.append( ['#####','h5.'] )
    words.append( ['######','h6.'] )
    words.append( ['{', '\{'] )
    words.append( ['}', '\}'] )
 
    newContent = []
 
    i = 0
    isCode = 0
    isQuote = 0
    isList = 0
 
    for l in c:
      i += 1
      if 0 == 0:
        k = l
        if l[0:1]=='*':
          isList = 1
        if l == '':
          isList = 0
 
        if l[0:1] == '>':
          if isQuote==0:
            k = '{quote}\n'+k[1:]
            isQuote = 1
          else:
            k = k[1:]
 
 
        if isList == 0:
          if isCode == 1:
            if l[0:1] == ' ' or l[0:1]=='\t':
              pass
            else:
              k = '\n'+k
              isCode = 0
 
          else:
            if l[0:1]==' ' or l[0:1]=='\t':
              #k = '{code}\n'+k
              isCode = 1
        else:
           if l[0:4]=='\t\t\t*':
             k = '****' + l[4:]
           if l[0:3]=='\t\t*':
             k = '***' + l[3:]
           if l[0:2]=='\t*':
             k = '**' + l[2:]

        for w in words:
          if l[:len(w[0])] == w[0]:
            k = w[1]+l[len(w[0]):]
 
 
        if l[0:1] != '>' and isQuote == 1:
          k = '{quote}\n'+k
          isQuote = 0
 
 
        if l[0:3] != '| -':
          newContent.append(k)
          
    return '\n'.join(newContent)
 
 
 
def convertFile(filename):
  old_content = open(filename, 'r').read()
  new_content = convertText(old_content)
  open(filename+'.txt','w').write(new_content)
  print "converted",filename
 
 
 
def convertAllFiles(dirname):
  f_counter = 0
  for f in os.listdir(dirname):
 
      fname = os.path.join(dirname,f)
 
      if fname[-3:] =='.md':
          f_counter += 1
          convertFile(fname)
 
 
if __name__=='__main__':
 
 
    if len(sys.argv)<2:
        convertAllFiles(".")
        sys.exit()
 
    infile = sys.argv[1]
 
    if not os.path.exists(infile):
        sys.stderr("%s does not exist!" %infile)
        sys.exit(15)
 
    if os.path.isdir(infile):
        convertAllFiles(infile)
 
    else:
        convertFile(infile)
