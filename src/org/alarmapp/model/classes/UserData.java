/*
 * Copyright (C) 2011-2012 AlarmApp.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
	private String id;

	// TODO: hier muss noch irgendwie die Permission hin vom Server
	private boolean canCreateAlarms = false;

	public UserData(String id, String firstName, String lastName,
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

	public String getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return String.format("%s - %s, %s, %s", id, lastName, firstName,
				department);
	}

	public boolean canViewAlarmStatus() {
		return true;
	}

	public String getFullName() {
		return getFirstName() + " " + getLastName();
	}

	public boolean hasDepartment() {
		return this.department != null;
	}

	public boolean hasAuthToken() {
		return this.authToken != null;
	}

	public boolean isLoggedIn() {
		return hasDepartment() && hasAuthToken();
	}

	public boolean hasAccount() {
		return true;
	}

	public boolean canCreateAlarms() {
		return canCreateAlarms;
	}

}
