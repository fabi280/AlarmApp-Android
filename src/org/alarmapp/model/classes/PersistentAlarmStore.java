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

package org.alarmapp.model.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmStore;
import org.alarmapp.util.LogEx;
import org.alarmapp.util.store.PersistentMap;

import android.content.Context;

public class PersistentAlarmStore implements AlarmStore {

	private final PersistentMap<String, Alarm> alarmStore;

	public PersistentAlarmStore(Context ctxt) {
		this.alarmStore = PersistentMap
				.restoreOrCreateEmpty(ctxt, "alarmstore");
	}

	public boolean contains(String alarmId) {
		return alarmStore.containsKey(alarmId);
	}

	public void put(Alarm alarm) {
		alarmStore.put(alarm.getOperationId(), alarm);
	}

	public Alarm get(String operationId) {
		return alarmStore.get(operationId);
	}

	public void save() {
		alarmStore.sync();

	}

	private Comparator<Alarm> alarmComparer = new Comparator<Alarm>() {

		public int compare(Alarm lhs, Alarm rhs) {
			return rhs.getAlarmed().compareTo(lhs.getAlarmed());
		}

	};

	public List<Alarm> getLastAlarms() {
		ArrayList<Alarm> alarms = new ArrayList<Alarm>(alarmStore.values());
		Collections.sort(alarms, alarmComparer);
		return alarms;
	}

	public void remove(Alarm alarm) {
		Alarm result = this.alarmStore.remove(alarm.getOperationId());
		this.save();

		if (result != null) {
			LogEx.info("Der Alarm " + result.getOperationId()
					+ " wurde gelöscht");
		}
	}
}
