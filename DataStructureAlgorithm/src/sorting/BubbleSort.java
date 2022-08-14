package sorting;

import java.util.Arrays;

public class BubbleSort {
	public static void main(String[] args) {
		// (99, 2, 1, 55, 101, 24) –> (2, 99, 1, 55, 101, 24), index 0 and index 1, swap since 99 > 2
		// (2, 99, 1, 55, 101, 24) –> (2, 1, 99, 55, 101, 24), index 1 and index 2, swap since 99 > 1
		// (2, 1, 99, 55, 101, 24) –> (2, 1, 55, 99, 101, 24), index 2 and index 3, swap since 99 > 55
		// (2, 1, 55, 99, 101, 24) -> (2, 1, 55, 99, 101, 24), index 3 and index 4, no swap and move to next index
		// (2, 1, 55, 99, 101, 24) -> (2, 1, 55, 99, 24, 101), index 4 and index 5, swap since 101 > 24 and reach last index

		// (2, 1, 55, 99, 24, 101) -> (1, 2, 55, 99, 24, 101), index 0 and index 1, swap since 2 > 1
		// (1, 2, 55, 99, 24, 101) -> (1, 2, 55, 99, 24, 101), index 1 and index 2, no swap and move to next index
		// (1, 2, 55, 99, 24, 101) -> (1, 2, 55, 99, 24, 101), index 2 and index 3, no swap and move to next index
		// (1, 2, 55, 99, 24, 101) -> (1, 2, 55, 24, 99, 101), index 3 and index 4, swap since 99 > 24 and reach last index

		// (1, 2, 55, 24, 99, 101) -> (1, 2, 55, 24, 99, 101), index 0 and index 1, no swap and move to next index
		// (1, 2, 55, 24, 99, 101) -> (1, 2, 55, 24, 99, 101), index 1 and index 2, no swap and move to next index
		// (1, 2, 55, 24, 99, 101) -> (1, 2, 24, 55, 99, 101), index 2 and index 3, swap since 55 > 24 and reach last index

		// (1, 2, 55, 24, 99, 101) -> (1, 2, 24, 55, 99, 101), index 0 and index 1, no swap and move to next index
		// (1, 2, 55, 24, 99, 101) -> (1, 2, 24, 55, 99, 101), index 1 and index 2, no swap and move to next index
		
		int[] myList = new int[] { 99, 2, 1, 55, 101, 24 };
		bubbleSort(myList);
	}

	private static void bubbleSort(int[] myList) {
		// not suitable for large data sets
		// average and worst case complexity Ο(n^2)
		// n is the number of items

		for (int i = myList.length - 1; i >= 0; i--) {
			for (int j = 0; j < i; j++) {
				if (myList[j] > myList[j + 1]) {
					int temp = myList[j + 1];
					myList[j + 1] = myList[j];
					myList[j] = temp;
				}
			}
		}

		System.out.println("bubbleSort : " + Arrays.toString(myList));
	}
}
