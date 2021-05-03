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

class InverseInsertionSortTest extends InverseSortTest {

	@Test
	void testInvalid() {
		int[] arr = new int[]{1};
		InverseInsertionSort inv = new InverseInsertionSort();
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(null, 0));
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(arr, -1));
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(arr, 1));
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 1, 2, 3, 4})
	void testCountTotal(int insertions) {
		Map<List<Integer>, Integer> all = new HashMap<>();
		for (int i = 0; i < 100000; i++) {
			int[] arr = {1, 2, 3, 4, 5};
			InverseInsertionSort inv = new InverseInsertionSort();
			inv.shuffle(arr, insertions);
			List<Integer> key = Arrays.stream(arr).boxed().collect(Collectors.toUnmodifiableList());
			all.merge(key, 1, Integer::sum);

			InsertionSort sort = new InsertionSort();
			sort.sort(arr);
			assertEquals(insertions, sort.insertions);
		}
		System.out.println(all.keySet().size());
		System.out.println(all);
	}

	@Test
	void testLongArray() {
		int[] arr = IntStream.rangeClosed(1, 100).toArray();
		for (int insertions = 0; insertions < arr.length; insertions++) {
			for (int i = 0; i < 1000; i++) {
				InverseInsertionSort inv = new InverseInsertionSort();
				inv.shuffle(arr, insertions);
				InsertionSort sort = new InsertionSort();
				sort.sort(arr);
				assertEquals(insertions, sort.insertions);
			}
		}
	}

	static class InsertionSort extends InverseSortTest.InplaceSort {

		int insertions = 0;

		@Override
		void sort(int[] arr) {
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
		}
	}
}
