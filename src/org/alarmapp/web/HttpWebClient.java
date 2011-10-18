package org.alarmapp.web;

import java.util.HashMap;

import org.alarmapp.model.Alarm;
import org.alarmapp.model.AuthToken;
import org.alarmapp.model.FireDepartment;
import org.alarmapp.model.User;
import org.alarmapp.model.classes.AuthTokenData;
import org.alarmapp.model.classes.FireDepartmentData;
import org.alarmapp.model.classes.UserData;
import org.alarmapp.util.DateUtil;
import org.alarmapp.util.LogEx;
import org.alarmapp.web.http.HttpUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpWebClient implements WebClient {

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
			JSONObject obj = new JSONObject(response);
			if (!obj.getString("result").equals("ok")) {
				throw new WebException(
						"Login fehlgeschlagen. Bitte überprüfen Sie ihren Benutzernamen und Ihr Passwort und stellen Sie sicher, dass Ihr Account aktiviert wurde.");
			}

			JSONObject user = obj.getJSONObject("user");
			JSONObject dept = obj.getJSONObject("fire_department");

			FireDepartment fireDepartment = new FireDepartmentData(
					dept.getInt("id"), dept.getString("name"));

			User userObj = new UserData(user.getInt("id"),
					user.getString("first_name"), user.getString("last_name"),
					fireDepartment);

			LogEx.info("Authenticated as User " + userObj);

			userObj.SetAuthToken(this.getAuthToken(name, password));

			if (!this.checkAuthentication(userObj.GetAuthToken()))
				throw new WebException(AUTH_TOKEN_INVALID);

			return userObj;

		} catch (JSONException e) {
			throw new WebException(JSON_ERROR, e);
		}
	}

	public void createSmartphone(AuthToken token, String registrationId,
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

		// [{"pk": 26004, "model": "AlarmService.smartphone", "fields":
		// {"RegistrationId":
		// "APA91bFVprgJrcUTtvdN_IoZU5qpRf04sk-l1X5HS0gXHl3zlyh29nmuAbhdCwJVHXI4gbNnvZxKyQWjeL6l32mQ7Zsqa3Low6axWUakFUIv0CfgoRe-RoyPZdENRLokZo9ZLlZGRTSa",
		// "Name": "n1", "Platform": "android", "Version": "1.2.3.4", "Owner":
		// 4001, "UUID": "259748ba-d53b-47cf-b667-7beb3af26e7d"}}]

		LogEx.verbose("Create Smartphone returned " + response);
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
}
