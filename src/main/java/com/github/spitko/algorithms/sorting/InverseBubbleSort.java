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
	 * @param arr        Array
	 * @param iterations Between [1, arr.length) (exclusive)
	 */
	@Override
	public void shuffle(int[] arr, int iterations) {
		if (arr == null || iterations > arr.length || iterations < 1) {
			throw new IllegalArgumentException();
		}
		if (iterations == 1) {
			return;
		}
		int max = arr.length - iterations + 1;
		int required = getBinomial(1, max, 1.0 / iterations);
		for (int i = 0; i < arr.length; i++) {
			int min = Math.max(arr.length - i - iterations, 0);
			int j;
			if (required > 0 && random.nextInt(min + 1) < required) {
				j = arr.length - min - 1;
				required--;
			} else {
				j = arr.length - 1 - random.nextInt(Math.max(arr.length - i - iterations + 1, 0), arr.length - i);
			}
			swap(arr, i, j);
		}
	}
}
