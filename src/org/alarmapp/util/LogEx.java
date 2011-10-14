package org.alarmapp.util;

import android.util.Log;

public class LogEx {

	private static String TAG = "AlarmApp";

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
