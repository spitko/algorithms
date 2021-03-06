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

class InverseBubbleSortTest extends InverseSortTest {

	@Test
	void testInvalid() {
		int[] arr = new int[]{1};
		InverseBubbleSort inv = new InverseBubbleSort();
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(null, 0));
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(arr, 0));
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(arr, 2));
	}

	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3, 4, 5})
	void testCountTotal(int iterations) {
		Map<List<Integer>, Integer> all = new HashMap<>();
		for (int i = 0; i < 100000; i++) {
			int[] arr = {1, 2, 3, 4, 5};
			InverseBubbleSort inv = new InverseBubbleSort();
			inv.shuffle(arr, iterations);
			List<Integer> key = Arrays.stream(arr).boxed().collect(Collectors.toUnmodifiableList());
			all.merge(key, 1, Integer::sum);

			BubbleSort sort = new BubbleSort();
			sort.sort(arr);
			assertEquals(iterations, sort.iterations);
		}
		System.out.println(all.keySet().size());
		System.out.println(all);
	}

	@Test
	void testLongArray() {
		int[] arr = IntStream.rangeClosed(1, 100).toArray();
		for (int iterations = 1; iterations <= arr.length; iterations++) {
			for (int i = 0; i < 1000; i++) {
				InverseBubbleSort inv = new InverseBubbleSort();
				inv.shuffle(arr, iterations);
				BubbleSort sort = new BubbleSort();
				sort.sort(arr);
				assertEquals(iterations, sort.iterations);
			}
		}
	}

	static class BubbleSort extends InverseSortTest.InplaceSort {

		int iterations = 0;

		@Override
		void sort(int[] arr) {
			boolean sorted;
			do {
				iterations++;
				sorted = true;
				for (int i = arr.length - 1; i > 0; i--) {
					if (arr[i - 1] > arr[i]) {
						swap(arr, i - 1, i);
						sorted = false;
					}
				}
			}
			while (!sorted);
		}
	}
}
