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

public class GCMReceiver extends C2DMBaseReceiver {

	public GCMReceiver() {
		super("org.AlarmApp");
	}

	@Override
	public void onRegistered(Context context, String registrationId)
			throws java.io.IOException {

		LogEx.verbose("Received registration id " + registrationId);

		Broadcasts.sendGCMRegisteredBroadcast(this, registrationId);

		try {
			AlarmApp.getAuthWebClient().createSmartphone(registrationId,
					Device.id(context), Device.name(context), "Android_GCM",
					Device.version(context));

		} catch (WebException e) {
			LogEx.exception(e);
			throw new RuntimeException(
					"Registrieren des Smartphones fehlgeschlagen.", e);
		}

		Broadcasts.sendSmartphoneCreatedBroadcast(context);
		NotificationUtil.closeNotification(context, 0);
	};

	@Override
	public void onUnregistered(final Context context) {
		LogEx.warning("GCM unregister happened.");
		LogEx.info("Removing user Account. Login required!");
		AlarmApp.setUser(new AnonymusUserData());

		NotificationUtil
				.notifyUser(
						context,
						"Push-Dienst deaktiviert",
						"Google hat den Push-Dienst auf Ihrem Smartphone deaktiviert. Sie müssen sich erneut anmelden, um die AlarmApp weiterhin zu verweden",
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
		final Alarm alarm = AlarmData.create(extras);

		LogEx.info("AlarmState is " + alarm.getState().getName());

		if (isNewAlarm(alarm)) {
			LogEx.info("Alarm does not exist already. New Alarm.");

			alarm.setState(AlarmState.Delivered);
			alarm.save();
			IntentUtil.sendToSyncService(this, alarm);

			LogEx.verbose("Display Alarm Activity.");
			IntentUtil.startAudioPlayerService(this, alarm);
			IntentUtil.displayAlarmActivity(this, alarm);
			NotificationUtil.notifyUser(context, alarm, AlarmActivity.class);
		} else {

			try {
				Alarm updated_alarm = AlarmApp.getAuthWebClient()
						.getAlarmInformations(alarm.getOperationId());

				if (updated_alarm.getState().isNext(alarm.getState())) {
					updated_alarm.setState(alarm.getState());
				}

				updated_alarm.save();

				IntentUtil
						.displayAlarmActivity(GCMReceiver.this, updated_alarm);

				LogEx.info(updated_alarm.getBundle().toString());

				NotificationUtil.notifyUserWithSound(context, updated_alarm,
						AlarmActivity.class);

			} catch (WebException e) {
				LogEx.info("Laden der Alarminformationen fehlgeschlagen!");
				LogEx.exception(e);
			}

			LogEx.info("Alarm already exists. Loading Alarm Update.");
		}

	}

	private boolean isNewAlarm(final Alarm alarm) {
		return !AlarmApp.getAlarmStore().contains(alarm.getOperationId());
	}

	@Override
	public void onError(Context context, String errorId) {

		LogEx.exception("Failed. Error code is " + errorId);

	}

}