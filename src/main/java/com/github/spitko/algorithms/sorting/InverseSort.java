package com.github.spitko.algorithms.sorting;

import java.util.concurrent.ThreadLocalRandom;

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

	public abstract void shuffle(int[] arr, int diff);

	public int[] generateArray(int start, int end, int count) {
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
}
