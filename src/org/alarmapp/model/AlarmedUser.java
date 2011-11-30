package org.alarmapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import android.os.Bundle;

public interface AlarmedUser extends Serializable, Bundable {
	public String getFirstName();

	public String getLastName();

	public String getUserId();

	public String getFullName();

	public AlarmState getAlarmState();

	public String getOperationId();

	public Date getAcknowledgeDate();

	public List<WayPoint> getPositions();

	public Bundle getBundle();

	public boolean hasAccepted();

	public boolean hasRejected();
}
