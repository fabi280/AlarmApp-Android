package org.alarmapp.model;

public interface AlarmStore {
	public boolean contains(String alarmId);

	public void put(Alarm alarm);
}
