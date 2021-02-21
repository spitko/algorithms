package com.github.spitko.algorithms.sorting;

import java.util.Arrays;

public class InverseSelectionSort extends InverseSort {

	public static void main(String[] args) {
		int[] arr = {1, 2, 3, 4, 5};
		InverseSelectionSort sort = new InverseSelectionSort();
		sort.shuffle(arr, 3);
		System.out.println(Arrays.toString(arr));
	}

	/**
	 * @param arr Array
	 * @param swaps Between [0, arr.length] (inclusive)
	 */
	@Override
	public void shuffle(int[] arr, int swaps) {
		if (arr == null || swaps < 0 || swaps >= arr.length) {
			throw new IllegalArgumentException();
		}
		if (swaps == 0) {
			return;
		}
		for (int i = arr.length - 1; i > 0; i--) {
			if (random.nextInt(i) < swaps) {
				int j = random.nextInt(0, i);
				swap(arr, i, j);
				swaps--;
			}
		}
	}
}
