package com.github.spitko.algorithms.sorting;


import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InverseSortTest {

	abstract static class InplaceSort {
		abstract void sort(int[] arr);

		void swap(int[] a, int i, int j) {
			int temp = a[i];
			a[i] = a[j];
			a[j] = temp;
		}
	}

	@Test
	void testGenerateArrayInvalid() {
		InverseSort sort = new InverseBubbleSort();
		assertThrows(IllegalArgumentException.class, () -> sort.generateArray(1, 10, 10));
	}

	@Test
	void testGenerateArray() {
		InverseSort sort = new InverseBubbleSort();
		int[] arr = sort.generateArray(1, 11, 10);
		assertArrayEquals(IntStream.range(1, 11).toArray(), arr);
	}

}