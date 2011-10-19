package org.alarmapp.model;

import java.io.Serializable;

public interface User extends Serializable {

	public String getFirstName();

	public String getLastName();

	public FireDepartment getFireDepartment();

	public AuthToken getAuthToken();

	public void setAuthToken(AuthToken token);

	public int getId();

	public boolean canViewAlarmStatus();
}
