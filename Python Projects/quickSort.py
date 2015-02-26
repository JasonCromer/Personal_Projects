


class QuickSort:

    def __init__(self):
        self.less = []
        self.equal = []
        self.greater = []

    def Sort(self, array):
        if len(array) > 1:
            pivotElement = array[0]
            for x in array:
                if x < pivotElement:
                    self.less.append(x)
                if x == pivotElement:
                    self.equal.append(x)
                if x > pivotElement:
                    self.greater.append(x)

            # You want to return equal instead of pivotElement in the case that there are multiple of the same values
            return QuickSort().Sort(self.less) + self.equal + QuickSort().Sort(self.greater)
        
        else:
            return array
                    

def main():
    array = input('Please enter an array: ')
    quickSortObject = QuickSort()

    sortedArray = quickSortObject.Sort(array)
    print sortedArray


if __name__ == '__main__':
    main()
