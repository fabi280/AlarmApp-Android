package org.alarmapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

	private static SimpleDateFormat formatter;

	private static SimpleDateFormat getFormatter() {
		if (formatter == null) {
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		}
		return formatter;
	}

	public static Date parse(String data) {
		try {
			String date = data;
			if (data.indexOf('.') > -1)
				date = data.substring(0, data.indexOf('.'));
			Date result = getFormatter().parse(date);
			return result;
		} catch (ParseException e) {
			LogEx.warning("Failed to parse the date string " + data
					+ ". Using now.");
			return new Date();
		}
	}

	/**
	 * Formats a date in the iso Date format
	 * 
	 * @param date
	 * @return a Date string with the format yyyy-MM-ddTHH:mm:ss
	 */
	public static String isoFormat(Date date) {
		return format(date).replace(' ', 'T');
	}

	public static String format(Date date) {
		return getFormatter().format(date);
	}

	public static Date parseIso(String date) {
		return parse(date.replace('T', ' '));
	}
}
