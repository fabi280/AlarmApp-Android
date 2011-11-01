package org.alarmapp.model;

import java.util.List;

public interface AlarmStore {
	public boolean contains(String alarmId);

	public void put(Alarm alarm);

	public Alarm get(String operationId);

	public List<Alarm> getLastAlarms();

	public void save();
}
