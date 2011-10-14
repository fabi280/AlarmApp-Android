package org.alarmapp.model;

public interface User {

	public String GetFirstName();

	public String GetLastName();

	public FireDepartment GetFireDepartment();

	public AuthToken GetAuthToken();

	public void SetAuthToken(AuthToken token);

	public int GetId();
}
