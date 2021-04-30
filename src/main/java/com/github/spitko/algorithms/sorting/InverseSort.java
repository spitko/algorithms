package com.github.spitko.algorithms.sorting;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public abstract class InverseSort {

	protected ThreadLocalRandom random;

	public InverseSort() {
		this.random = ThreadLocalRandom.current();
	}

	protected static void swap(int[] a, int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	/**
	 * Shuffle array
	 *
	 * @param arr  Array
	 * @param diff Difficulty, see specific subclass for details
	 */
	public abstract void shuffle(int[] arr, int diff);

	/**
	 * @param arr Array
	 * @return number of steps until sorted
	 */
	public abstract int getRemaining(int[] arr);

	/**
	 * Generate sorted array consisting of distinct numbers
	 *
	 * @param start Lower bound (inclusive)
	 * @param end   Higher bound (exlusive)
	 * @param count Length of array
	 * @return Sorted array
	 */
	public int[] generateSortedArray(int start, int end, int count) {
		if (count > end - start) {
			throw new IllegalArgumentException();
		}
		return random.ints(start, end).distinct().limit(count).sorted().toArray();
	}

	protected int getBinomial(int min, int n, double p) {
		int x = 0;
		while (x < min) {
			for (int i = 0; i < n; i++) {
				if (random.nextDouble() < p) {
					x++;
				}
			}
		}
		return x;
	}

	/**
	 * Fisher-Yates shuffle
	 *
	 * @param arr Array to shuffle
	 */
	public void shuffle(int[] arr) {
		for (int i = arr.length - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);
			swap(arr, i, j);
		}
	}

	/**
	 * Shuffle array until condition is met
	 *
	 * @param arr       Array to shuffle
	 * @param condition Condition to match
	 */
	public void shuffle(int[] arr, Predicate<int[]> condition) {
		do {
			shuffle(arr);
		} while (!condition.test(arr.clone()));
	}
}
