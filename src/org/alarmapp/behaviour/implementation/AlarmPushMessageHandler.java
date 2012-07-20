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

package org.alarmapp.behaviour.implementation;

import org.alarmapp.AlarmApp;
import org.alarmapp.activities.AlarmActivity;
import org.alarmapp.behaviour.IPushMessageHandler;
import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmState;
import org.alarmapp.model.classes.AlarmData;
import org.alarmapp.util.Ensure;
import org.alarmapp.util.IntentUtil;
import org.alarmapp.util.LogEx;
import org.alarmapp.util.NotificationUtil;

import android.content.Context;
import android.os.Bundle;

/**
 * @author Frank Englert
 * 
 *         This class handles an alarm push message by starting the Alarm
 *         Activity and by sending an receipt confirmation.
 * 
 */
public class AlarmPushMessageHandler implements IPushMessageHandler {

	public void handleMessage(Context context, Bundle extras) {
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
		IntentUtil.displayAlarmActivity(context, alarm);
		IntentUtil.startAudioPlayerService(context, alarm);

		IntentUtil.sendToSyncService(context, alarm);
	}

}
