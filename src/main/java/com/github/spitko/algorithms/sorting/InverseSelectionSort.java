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
	 * Shuffles array so that selection sort does specified number of swaps
	 *
	 * @param arr   Sorted array
	 * @param swaps From 0 to arr.length - 1
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

	@Override
	public int getRemaining(int[] arr) {
		arr = arr.clone();
		int swaps = 0;
		for (int i = 0; i < arr.length - 1; i++) {
			int min = i;
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[j] < arr[min]) {
					min = j;
				}
			}
			if (min != i) {
				swap(arr, i, min);
				swaps++;
			}
		}
		return swaps;
	}
}
