package org.alarmapp.web;

import java.util.Collection;

import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.model.AuthToken;
import org.alarmapp.model.WayPoint;

public interface AuthWebClient {
	public AuthToken getAuthToken();

	public boolean checkAuthentication() throws WebException;

	public void createSmartphone(String registrationId, String deviceId,
			String name, String platform, String version) throws WebException;

	public void setAlarmStatus(Alarm alarm) throws WebException;

	public Collection<AlarmedUser> getAlarmStatus(String operation_id)
			throws WebException;

	public void addAlarmStatusPosition(WayPoint wayPoint) throws WebException;
}
