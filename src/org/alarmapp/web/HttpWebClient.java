package org.alarmapp.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.alarmapp.model.AuthToken;
import org.alarmapp.model.FireDepartment;
import org.alarmapp.model.User;
import org.alarmapp.model.classes.AuthTokenData;
import org.alarmapp.model.classes.FireDepartmentData;
import org.alarmapp.model.classes.UserData;
import org.alarmapp.util.LogEx;
import org.alarmapp.web.http.HttpUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpWebClient implements WebClient {

	private static final String AUTH_TOKEN_INVALID = "Das vom Webservice erzeugte Authentifizierungstoken ist nicht gültig.";
	private static String WEBSERVICE_URL = "http://alarmnotificationservice.appspot.com/";
	private static String JSON_ERROR = "Fehler beim Verarbeiten der Web-Server-Antwort.";

	private Date parseDate(String data) {
		try {
			String date = data.substring(0, data.indexOf('.'));
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			return formatter.parse(date);
		} catch (ParseException e) {
			LogEx.warning("Failed to parse the date string " + data
					+ ". Using now.");
			return new Date();
		}
	}

	private AuthToken getAuthToken(String name, String password)
			throws WebException {

		HashMap<String, String> data = new HashMap<String, String>();
		data.put("username", name);
		data.put("password", password);
		data.put("purpose", "AlarmApp Android");

		String response = HttpUtil.request(WEBSERVICE_URL
				+ "web_service/auth_token/generate/", data, null);

		LogEx.verbose("auth_token/generate returned " + response);

		try {
			JSONObject json = new JSONObject(response);
			AuthToken auth = new AuthTokenData(json.getString("auth_token"),
					parseDate(json.getString("expired")));

			LogEx.verbose("Assigned Auth Token: " + auth);

			return auth;
		} catch (JSONException e) {
			throw new WebException(JSON_ERROR, e);
		}
	}

	public boolean checkAuthentication(AuthToken token) throws WebException {
		try {
			HashMap<String, String> headers = new HashMap<String, String>();
			headers.put("Authorization", "Token " + token.GetToken());

			String response = HttpUtil.request(WEBSERVICE_URL
					+ "web_service/auth_token/check/", null, headers);
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

		String response = HttpUtil.request(WEBSERVICE_URL
				+ "web_service/login/", data, null);

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
}
