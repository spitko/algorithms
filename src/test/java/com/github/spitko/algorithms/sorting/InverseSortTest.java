package com.github.spitko.algorithms.sorting;


import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InverseSortTest {

	protected static void swap(int[] a, int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	protected static void permute(int[] arr, int n, Consumer<int[]> consumer) {
		if (n == 1)
			consumer.accept(arr);
		for (int i = 0; i < n; i++) {
			InverseSortTest.permute(arr, n - 1, consumer);
			if (n % 2 == 1) {
				swap(arr, 0, n - 1);
			} else {
				swap(arr, i, n - 1);
			}
		}
	}

	@Test
	void testGenerateArrayInvalid() {
		InverseSort sort = new InverseBubbleSort();
		assertThrows(IllegalArgumentException.class, () -> sort.generateSortedArray(1, 10, 10));
	}

	@Test
	void testGenerateArray() {
		InverseSort sort = new InverseBubbleSort();
		int[] arr = sort.generateSortedArray(1, 11, 10);
		assertArrayEquals(IntStream.range(1, 11).toArray(), arr);
	}

	abstract static class InplaceSort {
		abstract void sort(int[] arr);

		void swap(int[] a, int i, int j) {
			int temp = a[i];
			a[i] = a[j];
			a[j] = temp;
		}
	}

}