package org.alarmapp.util;

import org.alarmapp.Actions;
import org.alarmapp.AlarmApp;
import org.alarmapp.activities.AccountCreateActivity;
import org.alarmapp.activities.AlarmActivity;
import org.alarmapp.activities.AlarmListActivity;
import org.alarmapp.activities.AlarmPreferenceActivity;
import org.alarmapp.activities.AlarmStatusActivity;
import org.alarmapp.activities.InformationActivity;
import org.alarmapp.activities.JoinDepartmentActivity;
import org.alarmapp.activities.JoinPendingActivity;
import org.alarmapp.activities.MainActivity;
import org.alarmapp.activities.map.AlarmMapActivity;
import org.alarmapp.model.Alarm;
import org.alarmapp.model.WayPoint;
import org.alarmapp.model.classes.PersonData;
import org.alarmapp.services.PositionService;
import org.alarmapp.services.SyncService;

import android.content.Context;
import android.content.Intent;

public class IntentUtil {
	public static void sendToSyncService(Context c, Alarm a) {
		Ensure.notNull(a);

		Intent statusUpdateIntent = new Intent(c, SyncService.class);
		statusUpdateIntent.putExtras(a.getBundle());
		statusUpdateIntent.setAction(Actions.UPDATE_ALARM_STATUS);
		c.startService(statusUpdateIntent);
	}

	public static void sendToSyncService(Context c, WayPoint w) {
		Ensure.notNull(c);
		Ensure.notNull(w);

		Intent statusUpdateIntent = new Intent(c, SyncService.class);
		statusUpdateIntent.putExtras(w.getBundle());
		statusUpdateIntent.setAction(Actions.START_TRACKING);
		c.startService(statusUpdateIntent);
	}

	public static void displayAccountCreateActivity(Context context) {
		Ensure.notNull(context);

		Intent alarmIntent = new Intent(context, AccountCreateActivity.class);
		context.startActivity(alarmIntent);
	}

	public static void startPositionService(Context c, Alarm alarm) {
		Ensure.notNull(c);
		Ensure.notNull(alarm);

		Intent startPositionServiceIntent = new Intent(c, PositionService.class);
		startPositionServiceIntent.setAction(Actions.START_TRACKING);
		startPositionServiceIntent.putExtras(alarm.getBundle());

		c.startService(startPositionServiceIntent);
	}

	public static void displayAlarmMapActivity(Context context, Alarm alarm) {
		Ensure.notNull(alarm);

		Intent intent = new Intent(context, AlarmMapActivity.class);
		intent.putExtras(alarm.getBundle());
		context.startActivity(intent);
	}

	public static void displayAlarmStatusUpdateIntent(Context context,
			Alarm alarm) {
		Ensure.notNull(alarm);

		Intent alarmIntent = new Intent(context, AlarmStatusActivity.class);
		alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		alarmIntent.putExtras(alarm.getBundle());
		context.startActivity(alarmIntent);
	}

	public static void displayAlarmIntent(Context context, Alarm alarm) {
		Ensure.notNull(alarm);

		Intent alarmIntent = new Intent(context, AlarmActivity.class);
		alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		alarmIntent.putExtras(alarm.getBundle());
		context.startActivity(alarmIntent);
	}

	public static void sendFeedbackEmailIntent(Context context) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_EMAIL,
				new String[] { "frank@alarmapp.org" });
		intent.putExtra(Intent.EXTRA_SUBJECT,
				"Feedback für die Android AlarmApp von "
						+ AlarmApp.getUser().getFullName());
		intent.setType("message/rfc822");
		context.startActivity(Intent.createChooser(intent,
				"Bitte wählen Sie eine Anwendung"));
	}

	public static void displayMainActivity(Context context) {
		Intent mainIntent = new Intent(context, MainActivity.class);
		context.startActivity(mainIntent);
	}

	public static void displayAlarmListActivity(Context context) {
		Intent alarmListIntent = new Intent(context, AlarmListActivity.class);
		context.startActivity(alarmListIntent);
	}

	public static void displayInformationsActivity(Context context) {
		Intent intent = new Intent(context, InformationActivity.class);
		context.startActivity(intent);
	}

	public static void displayPreferencesActivity(Context context) {
		Intent intent = new Intent(context, AlarmPreferenceActivity.class);
		context.startActivity(intent);
	}

	public static void displayJoinDepartmentActivity(Context context,
			PersonData person) {
		Intent intent = new Intent(context, JoinDepartmentActivity.class);
		intent.putExtras(person.getBundle());
		context.startActivity(intent);
	}

	public static void displayJoinPendingActivity(Context context) {
		Intent intent = new Intent(context, JoinPendingActivity.class);
		context.startActivity(intent);
	}
}
