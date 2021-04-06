package com.github.spitko.algorithms.sorting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class InverseHeapifyTest {

	@Test
	void testInvalid() {
		int[] arr = new int[]{1, 2, 3, 4, 5};
		InverseHeapify inv = new InverseHeapify();
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(null, 0));
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(arr, -1));
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(arr, 3));
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 1, 2})
	void testCountTotal(int ops) {
		Map<List<Integer>, Integer> all = new HashMap<>();
		for (int i = 0; i < 100000; i++) {
			int[] arr = {1, 2, 3, 4, 5};
			InverseHeapify inv = new InverseHeapify();
			inv.shuffle(arr, ops);
			List<Integer> key = Arrays.stream(arr).boxed().collect(Collectors.toUnmodifiableList());
			all.merge(key, 1, Integer::sum);
			Heapify sort = new Heapify();
			sort.sort(arr);
			assertEquals(ops, sort.operations);
		}
		System.out.println(all.keySet().size());
		System.out.println(all);
	}

	@Test
	void testLongArray() {
		int[] arr = IntStream.rangeClosed(1, 13).toArray();
		for (int ops = 0; ops < 7; ops++) {
			for (int i = 0; i < 10000; i++) {
				InverseHeapify inv = new InverseHeapify();
				inv.shuffle(arr, ops);
				Heapify sort = new Heapify();
				sort.sort(arr);
				assertEquals(ops, sort.operations);
			}
		}
	}

	static class Heapify extends InverseSortTest.InplaceSort {

		int operations = 0;

		void sort(int[] arr) {
			int n = arr.length;
			for (int i = n / 2 - 1; i >= 0; i--) {
				sink(arr, n, i);
			}
		}

		private void sink(int[] arr, int n, int i) {
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
			if (swapped) {
				operations++;
			}
		}
	}
}
