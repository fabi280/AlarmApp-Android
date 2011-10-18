package org.alarmapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	private final static SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static Date parse(String data) {
		try {
			String date = data;
			if (data.indexOf('.') > -1)
				date = data.substring(0, data.indexOf('.'));
			return formatter.parse(date);
		} catch (ParseException e) {
			LogEx.warning("Failed to parse the date string " + data
					+ ". Using now.");
			return new Date();
		}
	}

	public static String format(Date date) {
		return formatter.format(date);
	}
}
