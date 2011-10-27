package org.alarmapp;

import org.alarmapp.model.AlarmedUser;
import org.alarmapp.util.Ensure;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class Broadcasts {
	public static String ALARMSTATUS_CHANGED = "org.alarmapp.alarm_status_changed";
	public static String C2DM_REGISTERED = "org.alarmapp.c2dm_registered";
	public static String SMARTPHONE_CREATED = "org.alarmapp.smartphone_created";

	public static void sendC2DMRegisteredBroadcast(Context ctxt,
			String registrationId) {
		Intent intent = new Intent(C2DM_REGISTERED);
		intent.putExtra("registration_id", registrationId);
		ctxt.sendBroadcast(intent);
	}

	public static void sendSmartphoneCreatedBroadcast(Context ctxt) {
		Intent intent = new Intent(SMARTPHONE_CREATED);
		ctxt.sendBroadcast(intent);
	}

	public static void sendAlarmstatusChangedBroadcast(Context ctxt,
			AlarmedUser changedUser) {
		Ensure.notNull(changedUser);

		Intent intent = new Intent(ALARMSTATUS_CHANGED);
		intent.putExtras(changedUser.getBundle());
		ctxt.sendBroadcast(intent);
	}

	public static void registerForAlarmstatusChangedBroadcast(Context ctxt,
			BroadcastReceiver recceiver) {
		ctxt.registerReceiver(recceiver, new IntentFilter(ALARMSTATUS_CHANGED));
	}

	public static void registerForSmartphoneCreatedBroadcast(Context ctxt,
			BroadcastReceiver receiver) {
		ctxt.registerReceiver(receiver, new IntentFilter(SMARTPHONE_CREATED));
	}

	public static void registerForC2DMRegisteredBroadcast(Context ctxt,
			BroadcastReceiver receiver) {
		ctxt.registerReceiver(receiver, new IntentFilter(C2DM_REGISTERED));
	}
}
