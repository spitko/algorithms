package com.github.spitko.algorithms.sorting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(arr, 4));
	}

	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3})
	void testCountTotal(int swaps) {
		Map<List<Integer>, Integer> all = new HashMap<>();
		for (int i = 0; i < 100000; i++) {
			int[] arr = {1, 2, 3, 4, 5};
			InverseHeapify inv = new InverseHeapify();
			inv.shuffle(arr, swaps);
			List<Integer> key = Arrays.stream(arr).boxed().collect(Collectors.toUnmodifiableList());
			all.merge(key, 1, Integer::sum);
			Heapify sort = new Heapify();
			sort.sort(arr);
			assertEquals(swaps, sort.swaps);
		}
		System.out.println(all.keySet().size());
		System.out.println(all);
	}

	@Test
	void testLongArray() {
		int[] arr = IntStream.rangeClosed(1, 13).toArray();
		for (int swaps = 0; swaps < 5; swaps++) {
			for (int i = 0; i < 1000; i++) {
				InverseHeapify inv = new InverseHeapify();
				inv.shuffle(arr, swaps);
				Heapify sort = new Heapify();
				sort.sort(arr);
				assertEquals(swaps, sort.swaps);
			}
		}
	}

	static class Heapify extends InverseSortTest.InplaceSort {

		int swaps = 0;

		void sort(int[] arr) {
			int n = arr.length;
			for (int i = n / 2 - 1; i >= 0; i--) {
				sink(arr, n, i);
			}
		}

		private void sink(int[] arr, int n, int i) {
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
		}
	}
}
