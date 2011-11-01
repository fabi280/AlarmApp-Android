package org.alarmapp.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionUtil {
	public static <T> Set<T> asSet(T... vals) {
		HashSet<T> result = new HashSet<T>();
		for (T i : vals)
			result.add(i);
		return result;
	}

	public static <T> List<T> asList(T... vals) {
		ArrayList<T> result = new ArrayList<T>();
		for (T i : vals)
			result.add(i);
		return result;
	}
}
