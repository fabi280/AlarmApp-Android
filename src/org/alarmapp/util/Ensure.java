/*
 * Copyright (C) 2011-2012 AlarmApp.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.alarmapp.util;

import java.util.HashSet;

import org.alarmapp.AlarmApp;

import android.os.Bundle;

public class Ensure {
	public static void notNull(Object value) {
		if (!AlarmApp.isDebuggable())
			return;

		if (value == null)
			throw new IllegalArgumentException(
					"The given Value shall not be <null>");
	}

	public static void valid(boolean expr) {
		if (!AlarmApp.isDebuggable())
			return;

		if (!expr)
			throw new IllegalArgumentException(
					"The given assertion does not hold");
	}

	public static void bundleHasKeys(Bundle bundle, String... keys) {
		if (!AlarmApp.isDebuggable())
			return;

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
