package bigohelper;

import java.util.HashMap;
import java.util.Map;

public final class TimeComplexityCards {

    private static final Map<String, String> timeComplexityCard = new HashMap<String, String>();

    private TimeComplexityCards() {
    }

    static {
        timeComplexityCard.put("array", "Time complexity for an array, on average, is O(1) for "
            + "access, O(n) for search, insertion, and deletion.");

        timeComplexityCard.put("stack", "Time complexity for a stack, on average, is O(n) for "
            + "access and search, and O(1) for insertion and deletion.");

        timeComplexityCard.put("singly linked list", "Time complexity for a singly linked list, "
            + "on average, is O(n) for access and search, and O(1) for insertion and "
            + "deletion.");

        timeComplexityCard.put("doubly linked list", "Time complexity for a doubly linked list, "
            + "on average, is O(n) for access and search, and O(1) for insertion and "
            + "deletion.");

        timeComplexityCard.put("skip list", "Time complexity for a skip list, on average, is "
            + "O(log(n)) for access, search, insertion and deletion.");

        timeComplexityCard.put("hash table", "Time complexity for a hash table, on average, is "
            + "O(1) for search, insertion and deletion. Nice!");

        timeComplexityCard.put("binary search tree", "Time complexity for a binary search tree " 
            + ", on average, is O(log(n)) for access, search, insertion and deletion.");

        timeComplexityCard.put("Cartesian tree", "Time complexity for cartesian tree " 
            + ", on average, is O(log(n)) for search, insertion and deletion.");

        timeComplexityCard.put("B-tree", "Time complexity for a b. tree, on average, is "
            + " O(log(n)) for access, search, insertion, and deletion.");

        timeComplexityCard.put("red black tree", "Time complexity for a red black tree "
            + ", on average, is O(log(n)) for access, search, insertion and deletion.");

        timeComplexityCard.put("splay tree", "Time complexity for a splay tree "
            + ", on average, is O(log(n)) for search, insertion, and deletion.");

        timeComplexityCard.put("AVL tree", "Time complexity for an a. v. l. tree "
            + ", on average, is O(log(n)) for access, search, insertion, and deletion.");

        timeComplexityCard.put("quicksort", "Time complexity for quicksort, on average, "
            + "is O(n*log(n)).");

        timeComplexityCard.put("mergesort", "Time complexity for mergesort, on average, "
            + "is O(n*log(n)).");

        timeComplexityCard.put("timsort", "Time complexity for timsort, on average, "
            + "is O(n*log(n)).");

        timeComplexityCard.put("heapsort", "Time complexity for heapsort, on average, "
            + "is O(n*log(n)).");

        timeComplexityCard.put("bubble sort", "Time complexity for bubble sort, on average, "
            + "is O(n^2). Ouch!");

        timeComplexityCard.put("insertion sort", "Time complexity for insertion sort, on average, "
            + "is O(n^2).");

        timeComplexityCard.put("selection sort", "Time complexity for selection sort, on average, "
            + "is O(n^2).");

        timeComplexityCard.put("shell sort", "Time complexity for shell sort, on average, " 
            + "is O((n*log(n))^2).");

        timeComplexityCard.put("bucket sort", "Time complexity for bucket sort, on average, "
            + "is O(n+k).");
        
        timeComplexityCard.put("radix sort", "Time complexity for radix sort, on average, "
            + "is O(n*k).");

    }

    public static String get(String item) {
        return timeComplexityCard.get(item);
    }
}
