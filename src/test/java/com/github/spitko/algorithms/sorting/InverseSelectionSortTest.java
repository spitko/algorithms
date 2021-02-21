package com.github.spitko.algorithms.sorting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InverseSelectionSortTest {

	@Test
	void testInvalid() {
		int[] arr = new int[]{1};
		InverseSelectionSort inv = new InverseSelectionSort();
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(null, 0));
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(arr, -1));
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(arr, 1));
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 1, 2, 3, 4})
	void testCountTotal(int swaps) {
		Map<List<Integer>, Integer> all = new HashMap<>();
		for (int i = 0; i < 100000; i++) {
			int[] arr = {1, 2, 3, 4, 5};
			InverseSelectionSort inv = new InverseSelectionSort();
			inv.shuffle(arr, swaps);
			List<Integer> key = Arrays.stream(arr).boxed().collect(Collectors.toUnmodifiableList());
			all.merge(key, 1, Integer::sum);

			SelectionSort sort = new SelectionSort();
			sort.sort(arr);
			assertEquals(swaps, sort.swaps);
		}
		System.out.println(all.keySet().size());
		System.out.println(all);
	}

	@Test
	void testLongArray() {
		int[] arr = IntStream.rangeClosed(1, 100).toArray();
		for (int insertions = 0; insertions < arr.length; insertions++) {
			for (int i = 0; i < 1000; i++) {
				InverseSelectionSort inv = new InverseSelectionSort();
				inv.shuffle(arr, insertions);
				SelectionSort sort = new SelectionSort();
				sort.sort(arr);
				assertEquals(insertions, sort.swaps);
			}
		}
	}

	static class SelectionSort extends InverseSortTest.InplaceSort {

		int swaps = 0;

		@Override
		void sort(int[] arr) {
			for (int i = 0; i < arr.length - 1; i++) {
				int min = i;
				for (int j = i + 1; j < arr.length; j++) {
					if (arr[j] < arr[min]) {
						min = j;
					}
				}
				if (min != i) {
					swap(arr, i, min);
					swaps++;
				}
			}
		}
	}
}
