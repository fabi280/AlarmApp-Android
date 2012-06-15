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

package org.alarmapp.web;

import java.util.Collection;
import java.util.List;

import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmGroup;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.model.AuthToken;
import org.alarmapp.model.User;
import org.alarmapp.model.WayPoint;
import org.alarmapp.model.classes.PersonData;
import org.alarmapp.web.json.WebResult;

public interface WebClient {
	public User login(String name, String password) throws WebException;

	public AuthToken getAuth();

	public void setAuth(AuthToken token);

	public boolean checkAuthentication(AuthToken token) throws WebException;

	public boolean createSmartphone(AuthToken token, String registrationId,
			String deviceId, String name, String platform, String version)
			throws WebException;

	public void unregisterSmartphone(AuthToken token, String id)
			throws WebException;

	public void setAlarmStatus(AuthToken authToken, Alarm alarm)
			throws WebException;

	public List<String> getFiredepartmentList(String term) throws WebException;

	public void addAlarmStatusPosition(AuthToken authToken, WayPoint position)
			throws WebException;

	public WebResult checkUserName(String username) throws WebException;

	public WebResult checkEmailAdress(String email) throws WebException;

	public PersonData createUser(String username, String firstName,
			String lastName, String email, String password,
			String passwordConfirmation) throws WebException;

	public void getAccountStatus(String userId) throws WebException;

	public Collection<AlarmedUser> getAlarmStatus(AuthToken authToken,
			String operation_id) throws WebException;

	public WebResult joinFireDepartment(PersonData person, String firedepartment)
			throws WebException;

	public WebResult performAlarm(AuthToken token, String code, String title,
			String message) throws WebException;

	public List<AlarmGroup> getAlarmGroups(AuthToken token) throws WebException;
}
