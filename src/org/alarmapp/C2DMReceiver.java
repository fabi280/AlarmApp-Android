package org.alarmapp;

//--------------------------------------------------------
//My knowledge came from
//Android Cloud to Device Messaging (C2DM) - Tutorial
//http://www.vogella.de/articles/AndroidCloudToDeviceMessaging/article.html
//--------------------------------------------------------

import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmState;
import org.alarmapp.model.classes.AlarmData;
import org.alarmapp.util.Device;
import org.alarmapp.util.IntentUtil;
import org.alarmapp.util.LogEx;
import org.alarmapp.web.WebException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.c2dm.C2DMBaseReceiver;

public class C2DMReceiver extends C2DMBaseReceiver {

	public static final String ME = "C2DMReceiver";

	public C2DMReceiver() {
		super("man"); // This is currently not used, a constructor is required
		Log.v(ME, "Constructor");
	}

	@Override
	public void onRegistered(Context context, String registrationId)
			throws java.io.IOException {

		LogEx.verbose("Received registration id");

		Broadcasts.sendC2DMRegisteredBroadcast(this, registrationId);

		try {
			Controller.getWebClient().createSmartphone(
					Controller.getUser(context).getAuthToken(), registrationId,
					Device.id(context), Device.name(context),
					Device.platform(context), Device.version(context));
		} catch (WebException e) {
			LogEx.exception(e);
		}

		Broadcasts.sendSmartphoneCreatedBroadcast(context);
	};

	@Override
	protected synchronized void onMessage(Context context, Intent intent) {
		Bundle extras = intent.getExtras();

		if (extras != null) {
			LogEx.info("Received push message " + intent);

			LogEx.verbose("Push message contains " + extras.keySet());

			LogEx.verbose("Kind is " + extras.getString("kind"));

			if (extras.getString("kind").equals("alarm")) {
				handleAlarmMessage(context, extras);
			}
		}
	}

	private void handleAlarmMessage(Context context, Bundle extras) {
		Alarm alarm = AlarmData.Create(extras);

		if (Controller.getAlarmStore(context).contains(
				alarm.getOperationId())) {
			LogEx.warning("The Alarm " + alarm
					+ " does already exist. Aborting");
			return;
		}
		Controller.getAlarmStore(context).put(alarm);
		alarm.setState(AlarmState.Delivered);

		LogEx.verbose("Display Alarm Activity.");

		IntentUtil.createDisplayAlarmIntent(this, alarm);

		IntentUtil.createAlarmStatusUpdateIntent(this, alarm);
	}

	@Override
	public void onError(Context context, String errorId) {
		try {
			JSONObject json;
			json = new JSONObject().put("event", "error");

			// My application on my host server sends back to "EXTRAS" variables
			// msg and msgcnt
			// Depending on how you build your server app you can specify what
			// variables you want to send
			//
			json.put("msg", errorId);

			Log.e(ME + ":onError ", json.toString());

			// C2DMPlugin.sendJavascript(json);
			// Send the MESSAGE to the Javascript application
		} catch (JSONException e) {
			Log.e(ME + ":onMessage", "JSON exception");
		}

	}

}