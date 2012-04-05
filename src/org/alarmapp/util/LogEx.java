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

import android.util.Log;

public class LogEx {

	public static final String TAG = "AlarmApp";

	private static String analyzeStackTrace() {
		StackTraceElement elem = Thread.currentThread().getStackTrace()[5];
		return "[" + elem.getClassName() + "." + elem.getMethodName() + ":"
				+ elem.getLineNumber() + "] ";
	}

	private static String f(String msg) {
		return analyzeStackTrace() + msg;
	}

	public static void info(String msg) {
		Log.i(TAG, f(msg));
	}

	public static void debug(String msg) {
		Log.d(TAG, f(msg));
	}

	public static void warning(String msg) {
		Log.w(TAG, f(msg));
	}

	public static void exception(String msg) {
		Log.e(TAG, f(msg));
	}

	public static void exception(Throwable ex) {
		Log.e(TAG, f(ex.getMessage()), ex);
	}

	public static void exception(String msg, Throwable ex) {
		Log.e(TAG, f(msg), ex);
	}

	public static void verbose(String msg) {
		Log.v(TAG, f(msg));
	}
}
