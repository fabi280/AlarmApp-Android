package org.alarmapp.util;

import java.util.HashSet;

import android.os.Bundle;

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

	public static void bundleHasKeys(Bundle bundle, String... keys) {
		if (BundleUtil.containsAll(bundle, keys))
			return;

		HashSet<String> keyList = new HashSet<String>();
		for (String key : keys)
			keyList.add(key);

		keyList.removeAll(bundle.keySet());
		String missingKeys = keyList.toString();

		throw new IllegalArgumentException(
				"The Keys ("
						+ missingKeys
						+ ") are not in the given Bundle. The bundle contains the following keys: "
						+ bundle.keySet());
	}
}
