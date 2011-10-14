package org.alarmapp.util;

public class Ensure {
	public static void notNull(Object value) {
		if (value == null)
			throw new IllegalArgumentException(
					"The given Value shall not be <null>");
	}
}
