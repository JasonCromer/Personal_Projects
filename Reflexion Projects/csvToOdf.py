import csv


srsText = open('srsList.txt', 'w')
with open('veraSrs.csv', 'r') as f:
    reader = csv.reader(f)
    for row in reader:
        ticketNumber, summary = row[0], row[1]
        ticketNumber = ticketNumber.strip("'")
        summary = summary.strip("'")
        if('Requirements' in summary and summary.istitle()):
                summary.strip()
                
        if(summary == 'Summary'):
                summary = ''
        elif('Requirements' not in summary):
                summary = '\t\t' + summary
        
        FORMAT_STRING = '{0}'
        outputString = FORMAT_STRING.format(summary)
        srsText.write(outputString + '\n')

        
