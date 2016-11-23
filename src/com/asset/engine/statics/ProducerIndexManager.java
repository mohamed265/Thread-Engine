package com.asset.engine.statics;

import java.util.HashSet;

public class ProducerIndexManager {

	private static int current = 0;

	private static Integer producersSize = 1;

	private static HashSet<Integer> s = new HashSet<Integer>();

	public static void update(int producersSie) {
		synchronized (producersSize) {
			producersSize = producersSie;
		}
	}

	public static int getIndex(int last) {
		synchronized (producersSize) {
			s.remove(last);
			do {
				current = (current + 1) % producersSize;
			} while (s.contains(current));
			s.add(current);
			return current;
		}
	}
}
