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

import java.util.Date;
import java.util.List;

import org.alarmapp.AlarmApp;
import org.alarmapp.behaviour.IAlarmStatusChangeHandler;
import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmState;
import org.alarmapp.util.IntentUtil;

import android.content.Context;

/**
 * @author Administrator
 * 
 */
public class AlarmStatusChangeHandler implements IAlarmStatusChangeHandler {

	private Context context;

	public void handleStatusChange(Alarm alarm, AlarmState newState) {
		setStateToRecentOpenAlarms(alarm, newState);
	}

	private void setStateToRecentOpenAlarms(Alarm alarm, AlarmState newState) {
		List<Alarm> latestAlarms = AlarmApp.getAlarmStore().getLastAlarms();
		Date actualAlarmTime = alarm.getAlarmed();
		Date fiveMinutesBefore = actualAlarmTime;
		fiveMinutesBefore.setTime(actualAlarmTime.getTime() - 5 * 60 * 1000);
		for (Alarm a : latestAlarms) {

			if (a.getAlarmed().getTime() < fiveMinutesBefore.getTime()) {
				break;
			}

			if (!a.isFinal()) {

				alarm.setState(newState);
				Alarm storedAlarm = AlarmApp.getAlarmStore().get(
						a.getOperationId());
				storedAlarm.setState(newState);
				storedAlarm.save();
				IntentUtil.sendToSyncService(context, a);
			}
		}
		alarm.setState(newState);
	}

}
