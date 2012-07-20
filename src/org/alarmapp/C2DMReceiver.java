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

import org.alarmapp.activities.LoginActivity;
import org.alarmapp.behaviour.IPushMessageHandler;
import org.alarmapp.behaviour.binding.Alarm;
import org.alarmapp.behaviour.binding.AlarmStatus;
import org.alarmapp.model.classes.AnonymusUserData;
import org.alarmapp.util.Device;
import org.alarmapp.util.LogEx;
import org.alarmapp.util.NotificationUtil;
import org.alarmapp.web.WebException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.c2dm.C2DMBaseReceiver;
import com.google.inject.Inject;

public class C2DMReceiver extends C2DMBaseReceiver {

	@Inject
	@Alarm
	IPushMessageHandler alarmMessageHandler;

	@Inject
	@AlarmStatus
	IPushMessageHandler alarmStatusMessageHandler;

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
			throw new RuntimeException(
					"Registrieren des Smartphones fehlgeschlagen.", e);
		}

		Broadcasts.sendSmartphoneCreatedBroadcast(context);
		NotificationUtil.closeNotification(context, 0);
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
				alarmMessageHandler.handleMessage(context, extras);
				// handleAlarmMessage(context, extras);
			}
			if (extras.getString("kind").equals("alarm_status")) {
				alarmStatusMessageHandler.handleMessage(context, extras);
			}
		}
	}

	@Override
	public void onError(Context context, String errorId) {

		LogEx.exception("Failed. Error code is " + errorId);

	}

}