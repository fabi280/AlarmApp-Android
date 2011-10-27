package org.alarmapp.util;

import org.alarmapp.Actions;
import org.alarmapp.activities.AlarmActivity;
import org.alarmapp.activities.AlarmStatusActivity;
import org.alarmapp.activities.MainActivity;
import org.alarmapp.model.Alarm;
import org.alarmapp.services.SyncService;

import android.content.Context;
import android.content.Intent;

public class IntentUtil {
	public static void createAlarmStatusUpdateIntent(Context c, Alarm a) {
		Ensure.notNull(a);

		Intent statusUpdateIntent = new Intent(c, SyncService.class);
		statusUpdateIntent.putExtras(a.getBundle());
		statusUpdateIntent.setAction(Actions.UPDATE_ALARM_STATUS);
		c.startService(statusUpdateIntent);
	}

	public static void createDisplayAlarmStatusUpdateIntent(Context context,
			Alarm alarm) {
		Ensure.notNull(alarm);

		Intent alarmIntent = new Intent(context, AlarmStatusActivity.class);
		alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		alarmIntent.putExtras(alarm.getBundle());
		context.startActivity(alarmIntent);
	}

	public static void createDisplayAlarmIntent(Context context, Alarm alarm) {
		Ensure.notNull(alarm);

		Intent alarmIntent = new Intent(context, AlarmActivity.class);
		alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		alarmIntent.putExtras(alarm.getBundle());
		context.startActivity(alarmIntent);
	}

	public static void createMainIntent(Context context) {
		Intent mainIntent = new Intent(context, MainActivity.class);
		context.startActivity(mainIntent);
	}
}
