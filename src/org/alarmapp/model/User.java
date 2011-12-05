package org.alarmapp.model;

import java.io.Serializable;

public interface User extends Serializable, Person {

	public String getFirstName();

	public String getLastName();

	public String getFullName();

	public FireDepartment getFireDepartment();

	public AuthToken getAuthToken();

	public void setAuthToken(AuthToken token);

	public String getId();

	public boolean canViewAlarmStatus();
}
