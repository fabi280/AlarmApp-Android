package org.alarmapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import android.os.Bundle;

public interface Alarm extends Serializable {
	public String getTitle();

	public String getText();

	public String getOperationId();

	public Date getAlarmed();

	public Map<String, String> getAdditionalValues();

	public boolean isFinal();

	public AlarmState getState();

	public void setState(AlarmState state);

	public Bundle getBundle();
}
