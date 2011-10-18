package org.alarmapp.model;

import java.io.Serializable;

public interface User extends Serializable {

	public String GetFirstName();

	public String GetLastName();

	public FireDepartment GetFireDepartment();

	public AuthToken GetAuthToken();

	public void SetAuthToken(AuthToken token);

	public int GetId();
}
