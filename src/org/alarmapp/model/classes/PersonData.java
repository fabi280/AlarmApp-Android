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

import java.io.Serializable;

import org.alarmapp.model.AuthToken;
import org.alarmapp.model.Bindable;
import org.alarmapp.model.BindableConverter;
import org.alarmapp.model.FireDepartment;
import org.alarmapp.model.User;
import org.alarmapp.util.BundleUtil;
import org.alarmapp.util.Ensure;

import android.os.Bundle;

public class PersonData implements User, Bindable, Serializable {

	private static final long serialVersionUID = 1L;

	private static final String EMAIL = "email";
	private static final String PASSWORD = "password";
	private static final String USERNAME = "username";
	private static final String LAST_NAME = "lastName";
	private static final String FIRST_NAME = "firstName";
	private static final String ID = "id";
	private String id;
	private String firstName;
	private String lastName;
	private String username;
	private String email;
	private FireDepartment firedepartment;
	private String password;

	public PersonData(String id, String firstName, String lastName,
			String username, String email, String password) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public String getId() {
		return this.id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	@Override
	public String toString() {
		return getFullName() + "(" + getId() + ")";
	}

	public Bundle getBundle() {
		return converter.convert(this);
	}

	public static PersonData create(Bundle b) {
		return converter.convert(b);
	}

	private static BindableConverter<PersonData> converter = new BindableConverter<PersonData>() {

		public PersonData convert(Bundle b) {
			Ensure.valid(canConvert(b));
			return new PersonData(b.getString(ID), b.getString(FIRST_NAME),
					b.getString(LAST_NAME), b.getString(USERNAME),
					b.getString(EMAIL), b.getString(PASSWORD));
		}

		public Bundle convert(PersonData obj) {
			Ensure.notNull(obj);

			Bundle b = new Bundle();
			b.putString(ID, obj.id);
			b.putString(FIRST_NAME, obj.firstName);
			b.putString(LAST_NAME, obj.lastName);
			b.putString(PASSWORD, obj.password);
			b.putString(EMAIL, obj.email);
			b.putString(USERNAME, obj.username);

			return b;
		}

		public boolean canConvert(Bundle b) {
			return BundleUtil.containsAll(b, ID, FIRST_NAME, LAST_NAME,
					USERNAME, PASSWORD, EMAIL);
		}
	};

	public BindableConverter<PersonData> getConverter() {
		return converter;
	}

	public FireDepartment getFireDepartment() {
		return this.firedepartment;
	}

	public void setFireDepartment(FireDepartment fd) {
		this.firedepartment = fd;
	}

	public AuthToken getAuthToken() {
		throw new UnsupportedOperationException(
				"Don't call getAuthToken on a anonymus user");
	}

	public void setAuthToken(AuthToken token) {
		throw new UnsupportedOperationException(
				"Don't call setAuthToken on a anonymus user");
	}

	public boolean canViewAlarmStatus() {
		return false;
	}

	public boolean hasDepartment() {
		return this.firedepartment != null;
	}

	public boolean hasAuthToken() {
		return false;
	}

	public boolean isLoggedIn() {
		return false;
	}

	public boolean hasAccount() {
		return true;
	}

	public boolean canCreateAlarms() {
		return false;
	}

}
