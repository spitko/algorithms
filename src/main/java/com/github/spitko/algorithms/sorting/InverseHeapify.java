package com.github.spitko.algorithms.sorting;


import java.util.*;
import java.util.stream.IntStream;

public class InverseHeapify extends InverseSort {


	public static void main(String[] args) {
		int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
		InverseHeapify sort = new InverseHeapify();
		sort.shuffle(arr, 2);
		System.out.println(Arrays.toString(arr));
		System.out.println(sort.getMaxSwaps(arr));
	}


	@Override
	public void shuffle(int[] arr, int swaps) {
		if (arr == null || swaps < 0 || swaps > getMaxSwaps(arr)) {
			throw new IllegalArgumentException();
		}
		do {
			for (int i = arr.length - 1; i > 0; i--) {
				int j = random.nextInt(i + 1);
				swap(arr, i, j);
			}
		} while (getSwaps(arr) != swaps);
	}

	public int getSwaps(int[] arr) {
		int n = arr.length;
		int[] clone = arr.clone();
		return IntStream.iterate(n / 2 - 1, i -> i >= 0, i -> i - 1).map(i -> sink(clone, n, i)).sum();
	}

	public int getMaxSwaps(int[] arr) {
		return (int) Math.ceil(Math.log(arr.length + 1) / Math.log(2));
	}

	private int sink(int[] arr, int n, int i) {
		int swaps = 0;
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
				swaps++;
				i = largest;
			} else break;
		}
		return swaps;
	}
}
