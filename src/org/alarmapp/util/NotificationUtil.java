/*
 * Copyright (C) 2011 AlarmApp.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.alarmapp.util;

import java.util.Iterator;

import org.alarmapp.AlarmApp;
import org.alarmapp.R;
import org.alarmapp.model.Alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * @author frankenglert
 * 
 */
public class NotificationUtil {

	public static void notifyUser(Context c, Alarm alarm, Class<?> cls) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager = (NotificationManager) c
				.getSystemService(ns);

		Notification notification = createNotification();

		Intent notificationIntent = new Intent(c, cls);
		notificationIntent.putExtras(alarm.getBundle());
		PendingIntent contentIntent = PendingIntent.getActivity(c, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(c, alarm.getTitle(), alarm.getText(),
				contentIntent);

		notificationManager.notify(
				ParserUtil.parseInt(alarm.getOperationId(), 0), notification);
	}

	/**
	 * @return
	 */
	private static Notification createNotification() {
		int icon = R.drawable.startus_bar_icon;
		CharSequence tickerText = "Einsatz";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.sound = getRingtone();

		// Only perform this pattern one time (-1 means "do not repeat")
		if (AlarmApp.getPreferences().getBoolean("alarm_vibrate", true)) {
			notification.vibrate = createVibrationPattern();
		}

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		return notification;
	}

	/**
	 * @return
	 */
	private static long[] createVibrationPattern() {
		long[] r = new long[30 * pattern.length];
		for (int i = 0; i < r.length; i++) {
			r[i] = vibrationPattern.next();
		}
		return r;
	}

	final static long[] pattern = { 200, 200, 200, 200, 200, 500 };

	private static Iterator<Long> vibrationPattern = new Iterator<Long>() {

		private int count = 0;

		public boolean hasNext() {
			return true;
		}

		public Long next() {
			return pattern[count % pattern.length];
		}

		public void remove() {

		}
	};

	private static Uri getRingtone() {
		try {
			if (AlarmApp.getPreferences().contains("alarm_ringtone")) {
				return Uri.parse(AlarmApp.getPreferences().getString(
						"alarm_ringtone", null));
			}
		} catch (Exception e) {
			LogEx.exception(
					"Failed to load the ringtone from pref. Using default one",
					e);
		}
		return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
	}
}
