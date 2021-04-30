package com.github.spitko.algorithms.sorting;

import java.util.Arrays;

public class InverseBubbleSort extends InverseSort {

	public static void main(String[] args) {
		int[] arr = {1, 2, 3, 4, 5};
		InverseBubbleSort sort = new InverseBubbleSort();
		sort.shuffle(arr, 3);
		System.out.println(Arrays.toString(arr));
	}

	/**
	 * Shuffles array so that bubble sort does specified number of iterations
	 *
	 * @param arr        Sorted array
	 * @param iterations Between [1, arr.length]
	 */
	@Override
	public void shuffle(int[] arr, int iterations) {
		if (arr == null || iterations > arr.length || iterations < 1) {
			throw new IllegalArgumentException();
		}
		if (iterations == 1) {
			return;
		}
		int n = arr.length;
		int bound = n - iterations + 1;
		// Number of maximum distance swaps to do, at least one
		int swaps = getBinomial(1, bound, 1.0 / iterations);
		for (int i = 0; i < n; i++) {
			int max = i + iterations - 1;
			int j;
			if (swaps > 0 && random.nextInt(n - max) < swaps) {
				j = max;
				swaps--;
			} else {
				j = random.nextInt(i, Math.min(max, n));
			}
			swap(arr, i, j);
		}
	}

	@Override
	public int getRemaining(int[] arr) {
		arr = arr.clone();
		int iterations = 0;
		boolean sorted;
		do {
			iterations++;
			sorted = true;
			for (int i = arr.length - 1; i > 0; i--) {
				if (arr[i - 1] > arr[i]) {
					swap(arr, i - 1, i);
					sorted = false;
				}
			}
		}
		while (!sorted);
		return iterations;
	}
}
