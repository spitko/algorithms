package com.github.spitko.algorithms.sorting;

import java.util.Arrays;

public class InverseInsertionSort extends InverseSort {

	/**
	 * @param arr Array
	 * @param i   Index of element to insert
	 * @param j   Index where to insert selected element
	 */
	private static void insert(int[] arr, int i, int j) {
		int x = arr[i];
		while (i < j) {
			arr[i] = arr[++i];
		}
		arr[j] = x;
	}

	public static void main(String[] args) {
		int[] arr = new int[]{1, 2, 3, 4, 5};
		InverseInsertionSort inv = new InverseInsertionSort();
		inv.shuffle(arr, 3);
		System.out.println(Arrays.toString(arr));
	}

	/**
	 * Shuffles array so that insertion sort does specified number of insertions
	 *
	 * @param arr        Sorted array
	 * @param insertions From 0 to arr.length - 1
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

	@Override
	public int getRemaining(int[] arr) {
		arr = arr.clone();
		int insertions = 0;
		for (int i = 1; i < arr.length; i++) {
			int x = arr[i];
			int j = i - 1;
			while (j >= 0 && arr[j] > x) {
				arr[j + 1] = arr[j];
				j--;
			}
			if (j + 1 != i) {
				arr[j + 1] = x;
				insertions++;
			}
		}
		return insertions;
	}
}
