package com.github.spitko.algorithms.sorting;


import java.util.Arrays;

public class InverseQuickselect extends InverseSort {

	public static void main(String[] args) {
		int[] arr = {1, 2, 3, 4, 5};
		InverseQuickselect sort = new InverseQuickselect();
		sort.shuffle(arr, 2, 1);
		System.out.println(Arrays.toString(arr));
	}

	@Override
	public void shuffle(int[] arr, int partitions) {
		throw new UnsupportedOperationException("Not enough parameters");
	}

	/**
	 * Shuffle array so that Quickselect does specified number of partitions when finding k-th smallest element
	 *
	 * @param arr        Array
	 * @param k          From 0 to arr.length - 1
	 * @param partitions Number of partitions to do, from 1 to arr.length - 1
	 */
	public void shuffle(int[] arr, int k, int partitions) {
		if (arr == null || k < 0 || k >= arr.length || partitions < 1 || partitions >= arr.length) {
			throw new IllegalArgumentException();
		}
		Quickselect qs = new Quickselect();
		shuffle(arr, a -> qs.countPartitions(a, k) == partitions);
	}

	@Override
	public int getRemaining(int[] arr) {
		throw new UnsupportedOperationException("Not supported");
	}

	private static class Quickselect {
		private int partitions = 0;

		int countPartitions(int[] arr, int k) {
			partitions = 0;
			select(arr, 0, arr.length - 1, k);
			return partitions;
		}

		private int select(int[] arr, int left, int right, int k) {
			if (left == right) {
				return arr[left];
			}
			int pi = partition(arr, left, right);
			partitions++;
			if (pi == k) {
				return arr[k];
			} else if (k < pi) {
				return select(arr, left, pi, k);
			} else {
				return select(arr, pi + 1, right, k);
			}
		}

		private int partition(int[] arr, int left, int right) {
			int pivot = arr[left];
			int i = left - 1, j = right + 1;
			while (true) {
				do {
					i++;
				} while (arr[i] < pivot);
				do {
					j--;
				} while (arr[j] > pivot);
				if (i < j) swap(arr, i, j);
				else return j;
			}
		}
	}
}
