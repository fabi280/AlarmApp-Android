/*
 * Copyright (C) 2011 AlarmApp.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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

/**
 * @author Frank Englert
 * 
 */
public class AnonymusUserData implements User {

	private static final long serialVersionUID = 1L;

	public String getFirstName() {
		return "unbekannt";
	}

	public String getLastName() {
		return "unbekannt";
	}

	public String getFullName() {
		return "unbekannt";
	}

	public FireDepartment getFireDepartment() {
		throw new UnsupportedOperationException(
				"Don't call getFireDepartment on a anonymus user");
	}

	public AuthToken getAuthToken() {
		throw new UnsupportedOperationException(
				"Don't call getFireDepartment on a anonymus user");
	}

	public void setAuthToken(AuthToken token) {
		throw new UnsupportedOperationException(
				"Don't call getFireDepartment on a anonymus user");
	}

	public String getId() {
		throw new UnsupportedOperationException(
				"Don't call getFireDepartment on a anonymus user");
	}

	public boolean canViewAlarmStatus() {
		return false;
	}

	public boolean hasDepartment() {
		return false;
	}

	public boolean hasAuthToken() {
		return false;
	}

	public boolean isLoggedIn() {
		return false;
	}

	public boolean hasAccount() {
		return false;
	}

}
