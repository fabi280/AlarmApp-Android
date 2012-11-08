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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmGroup;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.model.AuthToken;
import org.alarmapp.model.User;
import org.alarmapp.model.WayPoint;
import org.alarmapp.model.classes.AlarmGroupData;
import org.alarmapp.model.classes.AuthTokenData;
import org.alarmapp.model.classes.PersonData;
import org.alarmapp.util.DateUtil;
import org.alarmapp.util.LogEx;
import org.alarmapp.web.http.HttpUtil;
import org.alarmapp.web.json.JsonResult;
import org.alarmapp.web.json.JsonUtil;
import org.alarmapp.web.json.WebResult;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpWebClient implements WebClient {

	private static final String USER_ACCOUNT_NOT_CREATED = "Das Erzeugen des Benutzers ist fehlgeschlagen.";
	private static final String AUTH_TOKEN_INVALID = "Das vom Webservice erzeugte Authentifizierungstoken ist nicht gültig.";
	private static String WEBSERVICE_URL = "http://alarmnotificationservice.appspot.com/";
	private static String JSON_ERROR = "Fehler beim Verarbeiten der Web-Server-Antwort.";

	private static String url(String relativePart) {
		return WEBSERVICE_URL + relativePart;
	}

	private AuthToken getAuthToken(String name, String password)
			throws WebException {

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("username", name);
		data.put("password", password);
		data.put("purpose", "AlarmApp Android");

		String response = HttpUtil.request(
				url("web_service/auth_token/generate/"), data, null);

		LogEx.verbose("auth_token/generate returned " + response);

		try {
			JSONObject json = new JSONObject(response);
			AuthToken auth = new AuthTokenData(json.getString("auth_token"),
					DateUtil.parse(json.getString("expired")));

			LogEx.verbose("Assigned Auth Token: " + auth);

			return auth;
		} catch (JSONException e) {
			throw new WebException(JSON_ERROR, e);
		}
	}

	private HashMap<String, String> createAuthHeader(AuthToken auth) {
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "Token " + auth.GetToken());
		return headers;
	}

	public boolean checkAuthentication(AuthToken token) throws WebException {
		try {
			String response = HttpUtil.request(
					url("web_service/auth_token/check/"), null,
					createAuthHeader(token));

			LogEx.verbose("auth_token/check returned" + response);
			JSONObject obj = new JSONObject(response);
			return obj.getString("result").equals("ok");
		} catch (JSONException e) {
			throw new WebException(JSON_ERROR, e);
		}
	}

	public User login(String name, String password) throws WebException {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("username", name);
		data.put("password", password);

		String response = HttpUtil.request(url("web_service/login/"), data,
				null);

		LogEx.verbose("login returned " + response);

		try {
			JsonResult<User> login = JsonUtil.parseLoginResult(response);

			if (!login.wasSuccessful()) {
				if (login.getErrorTag().equals("user_department_missing"))
					throw new FireDepartmentMissingException(
							"Sie sind kein Mitglied einer Feuerwehr!");
				if (login.getErrorTag().equals("user_department_not_active"))
					throw new FireDepartmentNotActiveException(
							"Ihre Feuerwehr ist nicht aktiviert!");

				throw new WebException(
						"Bitte überprüfen Sie ihren Benutzernamen und Ihr Passwort und stellen Sie sicher, dass Ihr Account aktiviert wurde.");
			}

			User userObj = login.getValue();
			LogEx.info("Authenticated as User " + userObj);

			userObj.setAuthToken(this.getAuthToken(name, password));

			if (!this.checkAuthentication(userObj.getAuthToken()))
				throw new WebException(AUTH_TOKEN_INVALID);

			return userObj;

		} catch (JSONException e) {
			throw new WebException(JSON_ERROR, e);
		}
	}

	public boolean createSmartphone(AuthToken token, String registrationId,
			String deviceId, String name, String platform, String version)
			throws WebException {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("uuid", deviceId);
		data.put("version", version);
		data.put("platform", platform);
		data.put("name", name);
		data.put("registrationid", registrationId);

		String response = HttpUtil.request(
				url("web_service/smartphone/create/"), data,
				createAuthHeader(token));

		LogEx.verbose("Create Smartphone returned " + response);

		try {
			return JsonUtil.parseSmartphoneCreateResult(response);
		} catch (Exception ex) {
			throw new WebException(
					"Registrieren des Smartphones fehlgeschlagen", ex);
		}
	}

	public void unregisterSmartphone(AuthToken token, String uuid)
			throws WebException {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("uuid", uuid);
		String response = HttpUtil.request(
				url("web_service/smartphone/remove/"), data,
				createAuthHeader(token));
		LogEx.verbose("Smartphone abgemeldet.");
	}

	public void setAlarmStatus(AuthToken authToken, Alarm alarm)
			throws WebException {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("Status", alarm.getState().getName());
		String response = HttpUtil.request(
				url("web_service/alarm_notification/" + alarm.getOperationId()
						+ "/"), data, createAuthHeader(authToken));
		LogEx.verbose("Set alarm state returned " + response);
	}

	public Collection<AlarmedUser> getAlarmStatus(AuthToken authToken,
			String operation_id) throws WebException {

		String response = HttpUtil
				.request(url("web_service/operation/" + operation_id
						+ "/status/"), null, createAuthHeader(authToken));

		try {
			return JsonUtil.parseAlarmedUserList(response, operation_id);
		} catch (JSONException e) {
			throw new WebException(JSON_ERROR, e);
		}
	}

	private AuthToken token;

	public AuthToken getAuth() {
		return this.token;
	}

	public void setAuth(AuthToken token) {
		this.token = token;
	}

	public void addAlarmStatusPosition(AuthToken authToken, WayPoint position)
			throws WebException {
		HashMap<String, String> data = new HashMap<String, String>();

		LogEx.verbose("Date string is"
				+ DateUtil.isoFormat(position.getMeasureDateTime()));

		data.put("date", DateUtil.isoFormat(position.getMeasureDateTime()));
		data.put("lat", Float.toString(position.getPosition().getLatitude()));
		data.put("lon", Float.toString(position.getPosition().getLongitude()));
		data.put("direction", Float.toString(position.getDirection()));
		data.put("speed", Float.toString(position.getSpeed()));
		data.put("precision", Float.toString(position.getPrecision()));
		data.put("source", position.getMeasurementMethod().toString());

		String response = HttpUtil.request(
				url("web_service/alarm_notification/"
						+ position.getOperationId() + "/add_position/"), data,
				createAuthHeader(authToken));
		LogEx.verbose("Set alarm state returned " + response);
	}

	public WebResult checkUserName(String username) throws WebException {

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("user", username);
		String response = HttpUtil.request(
				url("web_service/username/not_used/"), data, null);
		try {
			return JsonUtil.parseResult(response);
		} catch (JSONException ex) {
			throw new WebException(JSON_ERROR, ex);
		}
	}

	public WebResult checkEmailAdress(String email) throws WebException {

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("email", email);
		String response = HttpUtil.request(url("web_service/email/not_used/"),
				data, null);
		try {
			return JsonUtil.parseResult(response);
		} catch (JSONException ex) {
			throw new WebException(JSON_ERROR, ex);
		}
	}

	public PersonData createUser(String username, String firstName,
			String lastName, String email, String password,
			String passwordConfirmation) throws WebException {

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("user", username);
		data.put("email", email);
		data.put("first_name", firstName);
		data.put("last_name", lastName);
		data.put("password", password);
		data.put("password2", passwordConfirmation);

		String response = HttpUtil.request(url("web_service/account/create/"),
				data, null);

		try {
			JsonResult<String> user_id = JsonUtil
					.parseCreateUserResult(response);

			if (!user_id.wasSuccessful())
				throw new WebException(USER_ACCOUNT_NOT_CREATED);

			return new PersonData(user_id.getValue(), firstName, lastName,
					username, email, password);

		} catch (JSONException e) {
			throw new WebException(JSON_ERROR, e);
		}
	}

	public void getAccountStatus(String userId) throws WebException {
		// TODO Auto-generated method stub

	}

	public List<String> getFiredepartmentList(String term) throws WebException {
		String response;
		try {
			response = HttpUtil.request(
					url("ajax/department_list?term=" + term), null, null);
			return JsonUtil.parseGetFireDepartmentResult(response);

		} catch (JSONException e) {
			throw new WebException(JSON_ERROR, e);
		}

	}

	public WebResult joinFireDepartment(PersonData person, String firedepartment)
			throws WebException {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("username_or_email", person.getEmail());
		data.put("password", person.getPassword());
		data.put("department_name", firedepartment);

		String response = HttpUtil.request(
				url("web_service/fire_department/join/"), data, null);

		try {
			return JsonUtil.parseResult(response);
		} catch (JSONException e) {
			LogEx.exception(e);
			throw new WebException(JSON_ERROR, e);
		}
	}

	public WebResult performAlarm(AuthToken token, String code, String title,
			String message) throws WebException {
		HashMap<String, String> data = new HashMap<String, String>();
		data.put("code", code);
		data.put("title", title);
		data.put("text", message);
		String response = HttpUtil.request(url("/web_service/alarm/create/"),
				data, this.createAuthHeader(token));
		try {
			return JsonUtil.parseResult(response);
		} catch (JSONException e) {
			LogEx.exception(e);
			throw new WebException(JSON_ERROR, e);
		}
	}

	public List<AlarmGroup> getAlarmGroups(AuthToken token) throws WebException {
		ArrayList<AlarmGroup> stub = new ArrayList<AlarmGroup>();
		stub.add(new AlarmGroupData("Probealarm", "constialarm"));
		stub.add(new AlarmGroupData("Testalarm", "constialarm"));
		stub.add(new AlarmGroupData("First Responder", "constialarm"));

		// TODO: request an den Server schicken, um die Daten zu holen

		return stub;
	}

	public Alarm getAlarmInformations(AuthToken token, String operation_id)
			throws WebException {
		String response = HttpUtil.request(url(String.format(
				"/web_service/operation/%s/info/", operation_id)), null,
				createAuthHeader(token));
		LogEx.info(response);
		try {
			return JsonUtil.parseGetAlarmInformationsResult(response);
		} catch (JSONException e) {
			LogEx.info("getAlarmInformations request fehlgeschlagen.");
			LogEx.exception(e);
			throw new WebException(JSON_ERROR, e);
		}
	}
}
