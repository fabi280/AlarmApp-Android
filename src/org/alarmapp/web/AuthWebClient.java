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
import org.alarmapp.model.WayPoint;

public interface AuthWebClient {
	public AuthToken getAuthToken();

	public boolean checkAuthentication() throws WebException;

	public boolean createSmartphone(String registrationId, String deviceId,
			String name, String platform, String version) throws WebException;

	public void unregisterSmartphone(String uuid) throws WebException;

	public void setAlarmStatus(Alarm alarm) throws WebException;

	public Collection<AlarmedUser> getAlarmStatus(String operation_id)
			throws WebException;

	public void addAlarmStatusPosition(WayPoint wayPoint) throws WebException;

	public void performAlarm(String code, String alarmTitle, String alarmText)
			throws WebException;

	public List<AlarmGroup> getAlarmGroups() throws WebException;

	public Alarm getAlarmInformations(String operation_id) throws WebException;
}
