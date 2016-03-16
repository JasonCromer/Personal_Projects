package bigohelper;

import java.util.HashMap;
import java.util.Map;

public final class TimeComplexities {

    private static final Map<String, String> timeComplexities = new HashMap<String, String>();

    private TimeComplexities() {
    }

    static {
        timeComplexities.put("array", "Time complexity for an array, on average, is o of one for "
            + "access, o of n for search, insertion, and deletion.");

        timeComplexities.put("stack", "Time complexity for a stack, on average, is o of n for "
            + "access and search, and o of one for insertion and deletion.");

        timeComplexities.put("singly linked list", "Time complexity for a singly linked list, "
            + "on average, is o of n for access and search, and o of one for insertion and "
            + "deletion.");

        timeComplexities.put("doubly linked list", "Time complexity for a doubly linked list, "
            + "on average, is o of n for access and search, and o of one for insertion and "
            + "deletion.");

        timeComplexities.put("skip list", "Time complexity for a skip list, on average, is "
            + "o of log n for access, search, insertion and deletion.");

        timeComplexities.put("hash table", "Time complexity for a hash table, on average, is "
            + "o of one for search, insertion and deletion. Nice!");

        timeComplexities.put("binary search tree", "Time complexity for a binary search tree " 
            + ", on average, is o of log n for access, search, insertion and deletion.");

        timeComplexities.put("cartesian tree", "Time complexity for cartesian tree " 
            + ", on average, is o of log n for search, insertion and deletion.");

        timeComplexities.put("b tree", "Time complexity for a b tree, on average, is "
            + " o of log n for access, search, insertion, and deletion.");

        timeComplexities.put("red black tree", "Time complexity for a red black tree "
            + ", on average, is o of log n for access, search, insertion and deletion.");

        timeComplexities.put("splay tree", "Time complexity for a splay tree "
            + ", on average, is o of log n for search, insertion, and deletion.");

        timeComplexities.put("a v l tree", "Time complexity for an a v l tree "
            + ", on average, is o of log n for access, search, insertion, and deletion.");

        timeComplexities.put("quicksort", "Time complexity for quicksort, on average, "
            + "is o of n log n.");

        timeComplexities.put("mergesort", "Time complexity for mergesort, on average, "
            + "is o of n log n.");

        timeComplexities.put("timsort", "Time complexity for timsort, on average, "
            + "is o of n log n.");

        timeComplexities.put("heapsort", "Time complexity for heapsort, on average, "
            + "is o of n log n.");

        timeComplexities.put("bubble sort", "Time complexity for bubble sort, on average, "
            + "is o of n squared. Ouch!");

        timeComplexities.put("insertion sort", "Time complexity for insertion sort, on average, "
            + "is o of n squared.");

        timeComplexities.put("selection sort", "Time complexity for selection sort, on average, "
            + "is o of n squared.");

        timeComplexities.put("shell sort", "Time complexity for shell sort, on average, " 
            + "is o of n log n squared.");

        timeComplexities.put("bucket sort", "Time complexity for bucket sort, on average, "
            + "is o of n plus k.");
        
        timeComplexities.put("radix sort", "Time complexity for radix sort, on average, "
            + "is o of n times k.");

    }

    public static String get(String item) {
        return timeComplexities.get(item);
    }
}
