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
import org.alarmapp.Broadcasts;
import org.alarmapp.behaviour.IPushMessageHandler;
import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.model.classes.AlarmedUserData;
import org.alarmapp.util.Ensure;
import org.alarmapp.util.LogEx;

import android.content.Context;
import android.os.Bundle;

/**
 * @author Frank Englert
 * 
 *         This class handles the AlarmStatusPushMessage by sending an
 *         appropriate broadcast to all receivers that are interested in an
 *         alarm status change.
 */
public class AlarmStatusPushMessageHandler implements IPushMessageHandler {

	public void handleMessage(Context context, Bundle extras) {
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

}
