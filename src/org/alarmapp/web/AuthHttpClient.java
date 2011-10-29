package org.alarmapp.web;

import java.util.Collection;

import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.model.AuthToken;
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

}
