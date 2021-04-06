package com.github.spitko.algorithms.sorting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InverseHeapify extends InverseSort {


	public static void main(String[] args) {
		int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
		InverseHeapify sort = new InverseHeapify();
		sort.shuffle(arr, 2);
		System.out.println(Arrays.toString(arr));
		System.out.println(sort.getMaxSwaps(arr));
	}


	/**
	 * @param arr        Array
	 * @param operations Between [0, arr.length / 2] (inclusive)
	 */
	@Override
	public void shuffle(int[] arr, int operations) {
		if (arr == null || operations < 0 || operations > arr.length / 2) {
			throw new IllegalArgumentException();
		}
		shuffle(arr);
		int n = arr.length;
		for (int i = n / 2 - 1; i >= 0; i--) {
			sink(arr, n, i);
		}
		List<Integer> list = IntStream.range(0, n / 2).boxed().collect(Collectors.toList());
		Collections.shuffle(list);
		list = list.subList(0, operations);
		Collections.sort(list);
		for (Integer i : list) {
			int j = i * 2 + 1 + random.nextInt(2);
			swap(arr, i, j);
		}
	}

	@Override
	public int getRemaining(int[] arr) {
		arr = arr.clone();
		int n = arr.length;
		int operations = 0;
		for (int i = n / 2 - 1; i >= 0; i--) {
			operations += sink(arr, n, i) ? 1 : 0;
		}
		return operations;
	}

	public int getMaxSwaps(int[] arr) {
		return (int) Math.ceil(Math.log(arr.length + 1) / Math.log(2));
	}

	private boolean sink(int[] arr, int n, int i) {
		boolean swapped = false;
		while (true) {
			int left = 2 * i + 1;
			int right = 2 * i + 2;
			int largest = i;

			if (right < n && arr[right] > arr[largest]) {
				largest = right;
			}
			if (left < n && arr[left] > arr[largest]) {
				largest = left;
			}
			if (largest != i) {
				swap(arr, largest, i);
				swapped = true;
				i = largest;
			} else break;
		}
		return swapped;
	}
}
