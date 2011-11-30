package org.alarmapp.web;

public interface WebResult<T> {
	public boolean wasSuccessful();

	public T getValue();
}
