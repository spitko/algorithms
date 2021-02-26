package com.github.spitko.algorithms.sorting;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class InverseQuicksortTest {

	@Test
	void test() {
		int[] arr = {3, 5, 1, 4, 2};
		int[] arr2 = {36, 10, 20, 50, 32, 21, 85};
		int[] arr3 = {36, 10, 20, 49, 50, 32, 21, 85};
		int[] arr4 = {10, 20, 21, 32, 36, 49, 50, 85};

		Quicksort sorter = new Quicksort();
		sorter.sort(arr);
		System.out.println(Arrays.toString(arr));
		System.out.println(sorter.maxDepth);

		sorter = new Quicksort();
		sorter.sort(arr2);
		System.out.println(Arrays.toString(arr2));
		System.out.println(sorter.maxDepth);

		sorter = new Quicksort();
		sorter.sort(arr3);
		System.out.println(Arrays.toString(arr3));
		System.out.println(sorter.maxDepth);

		sorter = new Quicksort();
		sorter.sort(arr4);
		System.out.println(Arrays.toString(arr4));
		System.out.println(sorter.maxDepth);

	}
	/**
	 * Quicksort using Hoare partition scheme with pivot chosen as first element
	 */
	static class Quicksort extends InverseSortTest.InplaceSort {
		int maxDepth = 0;

		@Override
		void sort(int[] arr) {
			quicksort(arr, 0, arr.length - 1, 0);
		}

		private void quicksort(int[] arr, int lo, int hi, int depth) {
			maxDepth = Math.max(depth, maxDepth);
			while (lo < hi) {
				int pi = partition(arr, lo, hi);
				if (pi - lo < hi - pi) {
					quicksort(arr, lo, pi, depth + 1);
					lo = pi + 1;
				} else {
					quicksort(arr, pi + 1, hi, depth + 1);
					hi = pi;
				}
			}
		}

		private int partition(int[] arr, int lo, int hi) {
			int pivot = arr[lo];
			int i = lo - 1, j = hi + 1;
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