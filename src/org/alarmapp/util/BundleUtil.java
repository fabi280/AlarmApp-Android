package org.alarmapp.util;

import android.os.Bundle;

public class BundleUtil {
	public static boolean containsAll(Bundle bundle, String... keys) {

		if (bundle == null)
			return false;

		for (String key : keys) {
			if (!bundle.containsKey(key))
				return false;
		}
		return true;
	}
}
