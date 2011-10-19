package org.alarmapp.util;

import org.alarmapp.Actions;
import org.alarmapp.activities.AlarmActivity;
import org.alarmapp.model.Alarm;
import org.alarmapp.services.SyncService;

import android.content.Context;
import android.content.Intent;

public class IntentUtil {
	public static void createAlarmStatusUpdateIntent(Context c, Alarm a) {
		Intent statusUpdateIntent = new Intent(c, SyncService.class);
		statusUpdateIntent.putExtras(a.getBundle());
		statusUpdateIntent.setAction(Actions.UPDATE_ALARM_STATUS);
		c.startService(statusUpdateIntent);
	}

	public static void createDisplayAlarmIntent(Context context, Alarm alarm) {
		Intent alarmIntent = new Intent(context, AlarmActivity.class);
		alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		alarmIntent.putExtras(alarm.getBundle());
		context.startActivity(alarmIntent);
	}
}
