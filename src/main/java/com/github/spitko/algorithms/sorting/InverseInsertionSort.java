package com.github.spitko.algorithms.sorting;

import java.util.Arrays;

public class InverseInsertionSort extends InverseSort {

	private static void insert(int[] arr, int from, int to) {
		while (from < to) {
			swap(arr, from, ++from);
		}
	}

	public static void main(String[] args) {
		int[] arr = new int[]{1, 2, 3, 4, 5};
		InverseInsertionSort inv = new InverseInsertionSort();
		inv.shuffle(arr, 3);
		System.out.println(Arrays.toString(arr));
	}

	/**
	 * @param arr        Array
	 * @param insertions Between [0, arr.length] (inclusive)
	 */
	@Override
	public void shuffle(int[] arr, int insertions) {
		if (arr == null || insertions < 0 || insertions >= arr.length) {
			throw new IllegalArgumentException();
		}
		if (insertions == 0) {
			return;
		}
		for (int i = arr.length - 1; i > 0; i--) {
			if (random.nextInt(i) < insertions) {
				int j = random.nextInt(0, i);
				insert(arr, j, i);
				insertions--;
			}
		}
	}
}
