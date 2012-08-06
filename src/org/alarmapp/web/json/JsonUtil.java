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

package org.alarmapp.web.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmGroup;
import org.alarmapp.model.AlarmState;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.model.FireDepartment;
import org.alarmapp.model.LonLat;
import org.alarmapp.model.PositionMeasurementMethod;
import org.alarmapp.model.User;
import org.alarmapp.model.WayPoint;
import org.alarmapp.model.classes.AlarmData;
import org.alarmapp.model.classes.AlarmedUserData;
import org.alarmapp.model.classes.FireDepartmentData;
import org.alarmapp.model.classes.LonLatData;
import org.alarmapp.model.classes.UserData;
import org.alarmapp.model.classes.WayPointData;
import org.alarmapp.util.DateUtil;
import org.alarmapp.util.LogEx;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	public static ArrayList<AlarmedUser> parseAlarmedUserList(
			String jsonDocument, String operationId) throws JSONException {
		ArrayList<AlarmedUser> result = new ArrayList<AlarmedUser>();

		JSONArray array = new JSONArray(jsonDocument);
		LogEx.debug("Length is " + array.length());
		for (int i = 0; i < array.length(); i++) {

			if (array.isNull(i))
				continue;

			JSONObject obj = array.getJSONObject(i);

			String fireFighter = obj.getString("fire_fighter");

			ArrayList<WayPoint> waypoints = null;
			if (obj.has("positions")) {
				waypoints = parsePositions(operationId,
						obj.getJSONArray("positions"));
			}

			result.add(new AlarmedUserData(operationId, fireFighter, obj
					.getString("user_id"), AlarmState.create(obj
					.getString("status_id")), DateUtil.parse(obj
					.getString("acknowledged")), waypoints));
		}

		return result;
	}

	public static boolean parseSmartphoneCreateResult(String jsonDocument)
			throws JSONException {

		// Content is
		// [{"pk": 26004, "model": "AlarmService.smartphone", "fields":
		// {"RegistrationId":
		// "APA91bFVprgJrcUTtvdN_IoZU5qpRf04sk-l1X5HS0gXHl3zlyh29nmuAbhdCwJVHXI4gbNnvZxKyQWjeL6l32mQ7Zsqa3Low6axWUakFUIv0CfgoRe-RoyPZdENRLokZo9ZLlZGRTSa",
		// "Name": "n1", "Platform": "android", "Version": "1.2.3.4", "Owner":
		// 4001, "UUID": "259748ba-d53b-47cf-b667-7beb3af26e7d"}}]

		JSONArray array = new JSONArray(jsonDocument);
		if (array.length() == 0) {
			return false;
		}

		JSONObject obj = (JSONObject) array.get(0);
		int pk = obj.getInt("pk");

		LogEx.verbose("Smartphone PK is " + pk);

		if (pk > 0) {
			return true;
		}

		return false;
	}

	public static JsonResult<User> parseLoginResult(String jsonDocument)
			throws JSONException {
		JSONObject obj = new JSONObject(jsonDocument);
		if (isErrorResult(obj)) {
			return parseErrorResult(obj);
		}

		JSONObject user = obj.getJSONObject("user");
		JSONObject dept = obj.getJSONObject("fire_department");

		FireDepartment fireDepartment = new FireDepartmentData(
				dept.getInt("id"), dept.getString("name"),
				(float) dept.getDouble("lon"), (float) dept.getDouble("lat"));

		User userObj = new UserData(user.getString("id"),
				user.getString("first_name"), user.getString("last_name"),
				fireDepartment);

		return new JsonResult<User>(userObj);
	}

	public static WebResult parseResult(String jsonDocument)
			throws JSONException {
		JSONObject obj = new JSONObject(jsonDocument);
		if (isErrorResult(obj))
			return new WebResult(obj.getString("msg"),
					obj.getString("error_kind"));

		return WebResult.Successful;
	}

	public static JsonResult<String> parseCreateUserResult(String jsonDocument)
			throws JSONException {
		JSONObject obj = new JSONObject(jsonDocument);
		if (isErrorResult(obj)) {
			return parseErrorResult(obj);
		}

		return new JsonResult<String>(obj.getString("user_id"));

	}

	private static boolean isErrorResult(JSONObject obj) throws JSONException {
		return !obj.getString("result").equals("ok");
	}

	private static <T> JsonResult<T> parseErrorResult(JSONObject obj)
			throws JSONException {
		return new JsonResult<T>(obj.getString("error_kind"),
				obj.getString("msg"));
	}

	private static ArrayList<WayPoint> parsePositions(String operationId,
			JSONArray array) throws JSONException {

		ArrayList<WayPoint> result = new ArrayList<WayPoint>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);

			LonLat pos = new LonLatData((float) obj.getDouble("lon"),
					(float) obj.getDouble("lat"));
			result.add(new WayPointData(operationId, pos, (float) obj
					.getDouble("speed"), (float) obj.getDouble("direction"), 0,
					PositionMeasurementMethod.UNKNWON, DateUtil.parseIso(obj
							.getString("date"))));
		}
		return result;
	}

	/**
	 * @param response
	 * @return
	 */
	public static List<String> parseGetFireDepartmentResult(String response)
			throws JSONException {
		JSONArray arr = new JSONArray(response);

		ArrayList<String> result = new ArrayList<String>();

		for (int i = 0; i < arr.length(); i++) {
			if (!arr.isNull(i))
				result.add(arr.getString(i));
		}
		return result;
	}

	public static List<AlarmGroup> parseGetAlarmGroupListResult(String response)
			throws JSONException {
		ArrayList<AlarmGroup> result = new ArrayList<AlarmGroup>();
		// TODO: parse the result
		return result;
	}

	public static Alarm parseGetAlarmInformationsResult(String response)
			throws JSONException {

		JSONObject jobj = new JSONObject(response);
		Date date = DateUtil.parse(jobj.getString("alarmed"));
		AlarmState state = AlarmState.create(jobj.getString("status"));

		// XXX: Achtung, hier müssen noch die Extras mit rein
		Alarm a = new AlarmData(jobj.getString("operation_id"), date,
				jobj.getString("title"), jobj.getString("text"), state, null);
		return a;
	}
}
