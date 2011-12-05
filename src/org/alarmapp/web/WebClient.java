package org.alarmapp.web;

import java.util.Collection;

import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.model.AuthToken;
import org.alarmapp.model.Person;
import org.alarmapp.model.User;
import org.alarmapp.model.WayPoint;
import org.alarmapp.web.json.JsonResult;
import org.alarmapp.web.json.WebResult;

public interface WebClient {
	public User login(String name, String password) throws WebException;

	public AuthToken getAuth();

	public void setAuth(AuthToken token);

	public boolean checkAuthentication(AuthToken token) throws WebException;

	public void createSmartphone(AuthToken token, String registrationId,
			String deviceId, String name, String platform, String version)
			throws WebException;

	public void setAlarmStatus(AuthToken authToken, Alarm alarm)
			throws WebException;

	public void addAlarmStatusPosition(AuthToken authToken, WayPoint position)
			throws WebException;

	public WebResult checkUserName(String username) throws WebException;

	public WebResult checkEmailAdress(String email) throws WebException;

	public JsonResult<Person> createUser(String username, String firstName,
			String lastName, String email, String password,
			String passwordConfirmation) throws WebException;

	public void getAccountStatus(String userId) throws WebException;

	public Collection<AlarmedUser> getAlarmStatus(AuthToken authToken,
			String operation_id) throws WebException;
}
