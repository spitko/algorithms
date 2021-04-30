package com.github.spitko.algorithms.sorting;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InverseQuickselectTest extends InverseSortTest {


	private static Stream<Arguments> provideParameters() {
		return IntStream.rangeClosed(1, 4).boxed()
				.flatMap(partitions -> IntStream.rangeClosed(0, 4)
						.mapToObj(k -> Arguments.of(partitions, k)));
	}

	private static void permute(int[] arr, int size, int n, Consumer<int[]> consumer) {
		if (size == 1)
			consumer.accept(arr);
		for (int i = 0; i < size; i++) {
			permute(arr, size - 1, n, consumer);
			if (size % 2 == 1) {
				swap(arr, 0, size - 1);
			} else {
				swap(arr, i, size - 1);
			}
		}
	}

	@Test
	void testInvalid() {
		int[] arr = new int[]{1, 2, 3, 4, 5};
		InverseQuickselect inv = new InverseQuickselect();
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(null, 0, 1));
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(arr, 0, 0));
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(arr, 0, 5));
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(arr, -1, 1));
		assertThrows(IllegalArgumentException.class, () -> inv.shuffle(arr, 5, 1));
	}

	@ParameterizedTest
	@MethodSource("provideParameters")
	void testCountTotal(int partitions, int k) {
		int[] arr = {1, 2, 3, 4, 5};
		Map<List<Integer>, Integer> all = new HashMap<>();
		for (int i = 0; i < 100000; i++) {
			InverseQuickselect inv = new InverseQuickselect();
			inv.shuffle(arr, k, partitions);
			List<Integer> key = Arrays.stream(arr).boxed().collect(Collectors.toUnmodifiableList());
			all.merge(key, 1, Integer::sum);
			Quickselect qs = new Quickselect();
			qs.select(arr, k);
			assertEquals(partitions, qs.partitions);
		}
		System.out.println("partitions = " + partitions + ", k = " + k);
		System.out.println(all.keySet().size());
		System.out.println(all);
	}

	@Test
	void testLongArray() {
		Map<Integer /* k */, Map<Integer /* partitions */, Integer /* count */>> all = new HashMap<>();
		IntStream.rangeClosed(0, 9).forEach(i -> all.put(i, new HashMap<>()));
		Quickselect qs = new Quickselect();
		InverseQuickselect inv = new InverseQuickselect();
		int[] arr = inv.generateSortedArray(1, 100, 10);
		permute(arr, arr.length, arr.length, a -> {
			for (int k = 0; k <= 9; k++) {
				int[] cloned = arr.clone();
				qs.partitions = 0;
				qs.select(cloned, k);
				all.get(k).merge(qs.partitions, 1, Integer::sum);
			}
		});
		for (int p = 1; p < arr.length; p++) {
			for (int k = 5; k < 10; k++) {
				inv.shuffle(arr, k, p);
			}
		}
		int total = 3628800; // 10!
		System.out.println(all);
		System.out.printf("%-5s%-15s%-5s\n", "k", "partitions", "probability");
		all.forEach((k, value) -> value.forEach((partitions, count) -> System.out.printf("%-5d%-15d%-5.3f\n", k, partitions, count * 100.0 / total)));

	}

	/**
	 * Quickselect using Hoare partition scheme with pivot chosen as first element
	 */
	static class Quickselect extends InverseSortTest.InplaceSort {
		int partitions = 0;

		@Override
		void sort(int[] arr) {
			throw new UnsupportedOperationException();
		}

		int select(int[] arr, int k) {
			return select(arr, 0, arr.length - 1, k);
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