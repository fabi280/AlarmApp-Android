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
