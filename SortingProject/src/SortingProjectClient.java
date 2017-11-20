import java.util.*;

/**
 * @author 	Ian Moreno
 * @date 	10/06/2017
 * @purpose 	SortingProjectClient class demonstrates Insertion Sort, Selection Sort, and Counting Sort
 * 			on an array of 10 random integers between 0...9
 */

public class SortingProjectClient {
	public static void main(String[] args) {
		final int ARRAY_SIZE = 10;
		int[] intArr = buildRandomIntArray(ARRAY_SIZE);
		
		System.out.println("PART 1:");
		printArray("*Before InsertionSort*", intArr);
		insertionSort(intArr, ARRAY_SIZE);
		printArray("*After InsertionSort*", intArr);
		System.out.println("\n");

		intArr = buildRandomIntArray(ARRAY_SIZE);
		printArray("*Before Selection Sort*",intArr);
		selectionSort(intArr, ARRAY_SIZE);
		printArray("*After Selection Sort*", intArr);
		System.out.println("\n");
		
		System.out.println("PART 2:");
		intArr = buildRandomIntArray(ARRAY_SIZE);
		printArray("*Before Counting Sort*", intArr);
		countingSort(intArr, ARRAY_SIZE);
	}
	
	/**
	 * @purpose helper method builds an array of length = size of random integers 0...9
	 * @param size int variable representing length of array
	 * @return int[] representing an array of 'size' random integers 0...9
	 */
	private static int[] buildRandomIntArray(int size) {
		Random rand = new Random();
		int[] intArr = new int[size];

		for (int i = 0; i < size; i++) {
			intArr[i] = rand.nextInt(size);
		}
		
		return intArr;
	}
	
	/**
	 * @purpose prints formatted output of array contents
	 * @param msg String variable representing String to prepend to array element output
	 * @param arr int[] representing array of random integers
	 */
	private static void printArray(String msg, int[] arr) {
		System.out.println(msg + "\t" + Arrays.toString(arr));
	}
	
	// COUNTING SORT
	private static void countingSort(int[] arr, int n) {
		int[] counterArr = new int[n]; // holds count of each occurrence of an integer found in arr
		int[] sortedArr = new int[n]; // array to hold sorted elements
		int sortedIndex = 0;
		
		// loop through each element in arr and increment the count of each number found
		for (int i = 0; i < n; i++) {
			counterArr[arr[i]]++;
		}
		
		printArray("Counter Array\t", counterArr);
		
		for (int j = 0; j < n; j++) { // loop through each element in the counterArr
			while (counterArr[j] > 0) { // while the count of a number is greater than 0
				sortedArr[sortedIndex] = j;  // add the number from counterArr's current position into sortedArr
				sortedIndex++; // increment to the next index of sortedArr
				counterArr[j]--; // decrement the count of the number found in counterArr
			}
			printArray(("Sorted Array Pass #" + j), sortedArr);
		}
		printArray("*After Counting Sort*", sortedArr);
	}


	// SELECTION SORT
	/** Sorts the first n objects in an array into ascending order.
	   @param a  An array of integers.
	   @param n  An integer > 0. */
	private static void selectionSort(int[] a, int n)
	{
	  for (int index = 0; index < n - 1; index++)
	  {
		 int indexOfNextSmallest = getIndexOfSmallest(a, index, n - 1);
		 swap(a, index, indexOfNextSmallest);
		 // Assertion: a[0] <= a[1] <= . . . <= a[index] <= all other a[i]
		 printArray(("Selection Sort Pass #" + (index+1)), a);
	  } // end for
	} // end selectionSort

	// Finds the index of the smallest value in a portion of an array a.
	// Precondition: a.length > last >= first >= 0.
	// Returns the index of the smallest value among
	// a[first], a[first + 1], . . . , a[last].
	private static int getIndexOfSmallest(int[] a, int first, int last)
	{
	  int min = a[first];
	  int indexOfMin = first;
	  for (int index = first + 1; index <= last; index++)
	  {
		 if (a[index] < min)
		 {
			min = a[index];
			indexOfMin = index;
		 } // end if
		 // Assertion: min is the smallest of a[first] through a[index].
	  } // end for

	  return indexOfMin;
	} // end getIndexOfSmallest
	  
	// -------------------------------------------------------------------------------

	// INSERTION SORT
	private static void insertionSort(int[] a, int n)
	{
		insertionSort(a, 0, n - 1);
	} // end insertionSort

	private static void insertionSort(int[] a, int first, int last)
	{
		int index = 0;

		for (int unsorted = first + 1; unsorted <= last; unsorted++)
		{   // Assertion: a[first] <= a[first + 1] <= ... <= a[unsorted - 1]
		
			int firstUnsorted = a[unsorted];
			
			insertInOrder(firstUnsorted, a, first, unsorted - 1);

			printArray(("Insertion Sort Pass #" + (++index)), a);
		} // end for
	} // end insertionSort

	private static void insertInOrder(int anEntry, int[] a, int begin, int end)
	{
	  int index = end;
	  
		while ((index >= begin) && (anEntry < a[index]))
		{
			a[index + 1] = a[index]; // Make room
		 index--;
		} // end for
		
		// Assertion: a[index + 1] is available
		a[index + 1] = anEntry;  // Insert
	} // end insertInOrder
	
	// Swaps the array entries a[i] and a[j].
	private static void swap(int[] a, int i, int j)
	{
	  int temp = a[i];
	  a[i] = a[j];
	  a[j] = temp; 
	} // end swap
}

/**
PART 1:
*Before InsertionSort*	[4, 8, 3, 3, 4, 6, 6, 8, 3, 7]
Insertion Sort Pass #1	[4, 8, 3, 3, 4, 6, 6, 8, 3, 7]
Insertion Sort Pass #2	[3, 4, 8, 3, 4, 6, 6, 8, 3, 7]
Insertion Sort Pass #3	[3, 3, 4, 8, 4, 6, 6, 8, 3, 7]
Insertion Sort Pass #4	[3, 3, 4, 4, 8, 6, 6, 8, 3, 7]
Insertion Sort Pass #5	[3, 3, 4, 4, 6, 8, 6, 8, 3, 7]
Insertion Sort Pass #6	[3, 3, 4, 4, 6, 6, 8, 8, 3, 7]
Insertion Sort Pass #7	[3, 3, 4, 4, 6, 6, 8, 8, 3, 7]
Insertion Sort Pass #8	[3, 3, 3, 4, 4, 6, 6, 8, 8, 7]
Insertion Sort Pass #9	[3, 3, 3, 4, 4, 6, 6, 7, 8, 8]
*After InsertionSort*	[3, 3, 3, 4, 4, 6, 6, 7, 8, 8]


*Before Selection Sort*	[2, 0, 5, 9, 0, 1, 2, 7, 2, 3]
Selection Sort Pass #1	[0, 2, 5, 9, 0, 1, 2, 7, 2, 3]
Selection Sort Pass #2	[0, 0, 5, 9, 2, 1, 2, 7, 2, 3]
Selection Sort Pass #3	[0, 0, 1, 9, 2, 5, 2, 7, 2, 3]
Selection Sort Pass #4	[0, 0, 1, 2, 9, 5, 2, 7, 2, 3]
Selection Sort Pass #5	[0, 0, 1, 2, 2, 5, 9, 7, 2, 3]
Selection Sort Pass #6	[0, 0, 1, 2, 2, 2, 9, 7, 5, 3]
Selection Sort Pass #7	[0, 0, 1, 2, 2, 2, 3, 7, 5, 9]
Selection Sort Pass #8	[0, 0, 1, 2, 2, 2, 3, 5, 7, 9]
Selection Sort Pass #9	[0, 0, 1, 2, 2, 2, 3, 5, 7, 9]
*After Selection Sort*	[0, 0, 1, 2, 2, 2, 3, 5, 7, 9]


PART 2:
*Before Counting Sort*	[7, 7, 1, 2, 8, 3, 9, 3, 3, 6]
Counter Array		[0, 1, 1, 3, 0, 0, 1, 2, 1, 1]
Sorted Array Pass #0	[0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
Sorted Array Pass #1	[1, 0, 0, 0, 0, 0, 0, 0, 0, 0]
Sorted Array Pass #2	[1, 2, 0, 0, 0, 0, 0, 0, 0, 0]
Sorted Array Pass #3	[1, 2, 3, 3, 3, 0, 0, 0, 0, 0]
Sorted Array Pass #4	[1, 2, 3, 3, 3, 0, 0, 0, 0, 0]
Sorted Array Pass #5	[1, 2, 3, 3, 3, 0, 0, 0, 0, 0]
Sorted Array Pass #6	[1, 2, 3, 3, 3, 6, 0, 0, 0, 0]
Sorted Array Pass #7	[1, 2, 3, 3, 3, 6, 7, 7, 0, 0]
Sorted Array Pass #8	[1, 2, 3, 3, 3, 6, 7, 7, 8, 0]
Sorted Array Pass #9	[1, 2, 3, 3, 3, 6, 7, 7, 8, 9]
*After Counting Sort*	[1, 2, 3, 3, 3, 6, 7, 7, 8, 9]
*
*
* Project #6 
* b) O(n)
* c) In the worst case; Counting Sort is O(n) and Insertion Sort is O(n^2)
 */
