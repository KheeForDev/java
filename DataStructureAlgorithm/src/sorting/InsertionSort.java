package sorting;

import java.util.Arrays;

public class InsertionSort {
	public static void main(String[] args) {
		// 1: Iterate from arr[1] to arr[n] over the array.
		// 2: Compare the current element (key) to its predecessor.
		// 3: If the key element is smaller than its predecessor, compare it to the elements before. Move the greater elements one position up to make space for the swapped element.

		// (99, 2, 1, 55, 101, 24) -> (2, 99, 1, 55, 101, 24), value (index 1) and index 0, swap since 2 < 99
		// (2, 99, 1, 55, 101, 24) -> (2, 99, 1, 55, 101, 24), no swap, j < -1
		// (2, 99, 1, 55, 101, 24) -> (2, 1, 99, 55, 101, 24), value (index 2) and index 1, swap since 1 < 99
		// (2, 1, 99, 55, 101, 24) -> (1, 2, 99, 55, 101, 24), value (index 2) and index 0, swap since 1 < 22
		// (1, 2, 99, 55, 101, 24) -> (1, 2, 99, 55, 101, 24), no swap, j < -1
		// (1, 2, 99, 55, 101, 24) -> (1, 2, 55, 99, 101, 24), value (index 3) and index 2, swap since 55 < 99
		// (1, 2, 55, 99, 101, 24) -> (1, 2, 55, 99, 101, 24), no swap, 55 > 2 (index 1)
		// (1, 2, 55, 99, 101, 24) -> (1, 2, 55, 99, 101, 24), no swap, 55 > 1 (index 0)
		// (1, 2, 55, 99, 101, 24) -> (1, 2, 55, 99, 101, 24), no swap, j < -1

		int[] myList = new int[] { 99, 2, 1, 55, 101, 24 };
		insertionSort(myList);
	}

	private static void insertionSort(int[] myList) {
		// not suitable for large data sets
		// average and worst case complexity Ο(n^2)
		// n is the number of items

		for (int i = 1; i < myList.length; i++) {
			int value = myList[i];
			int prevIndex = i - 1;
			
			while (prevIndex > -1 && value < myList[prevIndex]) {
				myList[prevIndex + 1] = myList[prevIndex];
				myList[prevIndex] = value;
				prevIndex--;
			}
		}

		System.out.println("insertionSort : " + Arrays.toString(myList));
	}
}
