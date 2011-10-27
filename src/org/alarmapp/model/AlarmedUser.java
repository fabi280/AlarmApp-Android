package org.alarmapp.model;

import java.util.Date;

import android.os.Bundle;

public interface AlarmedUser {
	public String getFirstName();

	public String getLastName();

	public String getUserId();

	public AlarmState getAlarmState();

	public String getOperationId();

	public Date getAcknowledgeDate();

	public Bundle getBundle();
}
