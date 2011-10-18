package org.alarmapp.util;

public class Ensure {
	public static void notNull(Object value) {
		if (value == null)
			throw new IllegalArgumentException(
					"The given Value shall not be <null>");
	}

	public static void valid(boolean expr) {
		if (!expr)
			throw new IllegalArgumentException(
					"The given assertion does not hold");
	}
}
