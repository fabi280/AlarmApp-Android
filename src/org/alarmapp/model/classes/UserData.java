package org.alarmapp.model.classes;

import org.alarmapp.model.AuthToken;
import org.alarmapp.model.FireDepartment;
import org.alarmapp.model.User;

public class UserData implements User {

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

	public String GetFirstName() {
		return this.firstName;
	}

	public AuthToken GetAuthToken() {
		return this.authToken;
	}

	public void SetAuthToken(AuthToken token) {
		this.authToken = token;
	}

	public String GetLastName() {
		return this.lastName;
	}

	public FireDepartment GetFireDepartment() {
		return this.department;
	}

	public int GetId() {
		return this.id;
	}

	@Override
	public String toString() {
		return String.format("%d - %s, %s, %s", id, lastName, firstName,
				department);
	}

}
