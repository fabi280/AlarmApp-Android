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

import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.model.AuthToken;
import org.alarmapp.model.WayPoint;
import org.alarmapp.util.Ensure;

public class AuthHttpClient implements AuthWebClient {

	private final WebClient client;
	private final AuthToken token;

	public AuthHttpClient(WebClient client, AuthToken token) {
		super();
		Ensure.notNull(client);
		Ensure.notNull(token);

		this.client = client;
		this.token = token;
	}

	public AuthToken getAuthToken() {
		return this.token;
	}

	public boolean checkAuthentication() throws WebException {
		return client.checkAuthentication(this.token);
	}

	public void createSmartphone(String registrationId, String deviceId,
			String name, String platform, String version) throws WebException {
		client.createSmartphone(token, registrationId, deviceId, name,
				platform, version);
	}

	public void setAlarmStatus(Alarm alarm) throws WebException {
		client.setAlarmStatus(token, alarm);

	}

	public Collection<AlarmedUser> getAlarmStatus(String operation_id)
			throws WebException {
		return client.getAlarmStatus(token, operation_id);
	}

	public void addAlarmStatusPosition(WayPoint wayPoint) throws WebException {
		client.addAlarmStatusPosition(token, wayPoint);
	}

}
