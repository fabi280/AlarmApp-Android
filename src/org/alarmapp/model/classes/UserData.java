package org.alarmapp.model.classes;

import org.alarmapp.model.AuthToken;
import org.alarmapp.model.FireDepartment;
import org.alarmapp.model.User;

public class UserData implements User {

	private static final long serialVersionUID = 1L;

	private String firstName;
	private String lastName;
	private FireDepartment department;
	private AuthToken authToken;
	private int id;

	public UserData(int id, String firstName, String lastName,
			FireDepartment department) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.department = department;
		this.id = id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public AuthToken getAuthToken() {
		return this.authToken;
	}

	public void setAuthToken(AuthToken token) {
		this.authToken = token;
	}

	public String getLastName() {
		return this.lastName;
	}

	public FireDepartment getFireDepartment() {
		return this.department;
	}

	public int getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return String.format("%d - %s, %s, %s", id, lastName, firstName,
				department);
	}

	public boolean canViewAlarmStatus() {
		return true;
	}

	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}

}
