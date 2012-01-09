package org.alarmapp;

import org.alarmapp.activities.AlarmActivity;
import org.alarmapp.activities.LoginActivity;
import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmState;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.model.classes.AlarmData;
import org.alarmapp.model.classes.AlarmedUserData;
import org.alarmapp.model.classes.AnonymusUserData;
import org.alarmapp.util.Device;
import org.alarmapp.util.Ensure;
import org.alarmapp.util.IntentUtil;
import org.alarmapp.util.LogEx;
import org.alarmapp.util.NotificationUtil;
import org.alarmapp.web.WebException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.c2dm.C2DMBaseReceiver;

public class C2DMReceiver extends C2DMBaseReceiver {

	public C2DMReceiver() {
		super("org.AlarmApp");
	}

	@Override
	public void onRegistered(Context context, String registrationId)
			throws java.io.IOException {

		LogEx.verbose("Received registration id");

		Broadcasts.sendC2DMRegisteredBroadcast(this, registrationId);

		try {
			AlarmApp.getAuthWebClient().createSmartphone(registrationId,
					Device.id(context), Device.name(context),
					Device.platform(context), Device.version(context));
		} catch (WebException e) {
			LogEx.exception(e);
		}

		Broadcasts.sendSmartphoneCreatedBroadcast(context);
	};

	@Override
	public void onUnregistered(final Context context) {
		LogEx.warning("C2DM unregister happened.");
		LogEx.info("Removing user Account. Login required!");
		AlarmApp.setUser(new AnonymusUserData());

		NotificationUtil
				.notifyUser(
						context,
						"Push-Dienst deaktiviert",
						"Google hat den Push-Dienst auf Ihrem Smartphoen deaktiviert. Sie müssen Sich erneut anmelden, um die AlarmApp weiterhin zu verweden",
						LoginActivity.class);
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
			if (extras.getString("kind").equals("alarm_status")) {
				handleAlarmStatusMessage(context, extras);
			}
		}
	}

	private void handleAlarmStatusMessage(Context context, Bundle extras) {
		Ensure.valid(AlarmedUserData.isAlarmedUserDataBundle(extras));
		AlarmedUser user = AlarmedUserData.create(extras);

		Broadcasts.sendAlarmstatusChangedBroadcast(context, user);
		LogEx.verbose("Sent AlarmStatus change Broadcast for Firefighter "
				+ user.getFirstName() + " " + user.getLastName()
				+ ". Firefighter is " + user.getAlarmState());

		if (AlarmApp.getAlarmStore().contains(user.getOperationId())) {
			Alarm a = AlarmApp.getAlarmStore().get(user.getOperationId());
			a.updateAlarmedUser(user);
			a.save();
		}
	}

	private void handleAlarmMessage(Context context, Bundle extras) {
		Ensure.valid(AlarmData.isAlarmDataBundle(extras));
		Alarm alarm = AlarmData.create(extras);

		if (AlarmApp.getAlarmStore().contains(alarm.getOperationId())) {
			LogEx.warning("The Alarm " + alarm
					+ " does already exist. Aborting");
			return;
		}

		alarm.setState(AlarmState.Delivered);
		AlarmApp.getAlarmStore().put(alarm);

		LogEx.verbose("Display Alarm Activity.");

		NotificationUtil.notifyUser(context, alarm, AlarmActivity.class);
		IntentUtil.displayAlarmActivity(this, alarm);
		IntentUtil.startAudioPlayerService(this, alarm);

		IntentUtil.sendToSyncService(this, alarm);
	}

	@Override
	public void onError(Context context, String errorId) {

		LogEx.exception("Failed. Error code is " + errorId);

	}

}