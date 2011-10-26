package org.alarmapp.model.classes;

import java.util.Map;

import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmStore;
import org.alarmapp.util.store.PersistentMap;

import android.content.Context;

public class PersistentAlarmStore implements AlarmStore {

	private final Map<String, Alarm> alarmStore;

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
}
