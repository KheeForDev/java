package com.kheefordev.junit.helper;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.junit.Test;

public class ArraysCompareTest {

	// Test sorted array
	@Test
	public void test() {
		int[] numbers = { 12, 3, 1, 4 };
		int[] expected = { 1, 3, 4, 12 };
		Arrays.sort(numbers);
		assertArrayEquals(expected, numbers);
	}

	// Test null array
	@Test(expected = NullPointerException.class)
	public void testArraySort_NullArray() {
		int[] numbers = null;
		Arrays.sort(numbers);
	}

	// Test for sort performance
	@Test(timeout = 100)
	public void testSort_Performance() {
		int array[] = { 12, 23, 4 };
		for (int i = 1; i <= 1000000; i++) {
			array[0] = 1;
			Arrays.sort(array);
		}
	}
}
