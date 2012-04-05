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

import org.alarmapp.R;
import org.alarmapp.model.Alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class NotificationUtil {

	public static void notifyUser(Context c, Alarm alarm, Class<?> cls) {
		notifyUser(c, ParserUtil.parseInt(alarm.getOperationId(), 0),
				alarm.getTitle(), alarm.getText(), cls, alarm.getBundle());
	}

	public static void notifyUser(Context c, String title, String text,
			Class<?> callbackIntentClass) {
		notifyUser(c, 0, title, text, callbackIntentClass, null);
	}

	public static void closeNotification(Context c, int id) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager = (NotificationManager) c
				.getSystemService(ns);
		notificationManager.cancel(id);
	}

	public static void notifyUser(Context c, int id, String title,
			String message, Class<?> cls, Bundle bundle) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager = (NotificationManager) c
				.getSystemService(ns);

		Notification notification = createNotification(title);

		Intent notificationIntent = new Intent(c, cls);
		if (bundle != null)
			notificationIntent.putExtras(bundle);

		PendingIntent contentIntent = PendingIntent.getActivity(c, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(c, title, message, contentIntent);

		notificationManager.notify(id, notification);
	}

	private static Notification createNotification(String title) {
		int icon = R.drawable.startus_bar_icon;
		CharSequence tickerText = title;
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);
		notification.sound = null;

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		return notification;
	}
}
