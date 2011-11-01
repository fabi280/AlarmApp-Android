package org.alarmapp.model.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmStore;
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
}
