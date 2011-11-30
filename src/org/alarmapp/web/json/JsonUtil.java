package org.alarmapp.web.json;

import java.util.ArrayList;

import org.alarmapp.model.AlarmState;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.model.FireDepartment;
import org.alarmapp.model.LonLat;
import org.alarmapp.model.PositionMeasurementMethod;
import org.alarmapp.model.User;
import org.alarmapp.model.WayPoint;
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

	public static JsonResult<User> parseLoginResult(String jsonDocument)
			throws JSONException {
		JSONObject obj = new JSONObject(jsonDocument);
		if (!obj.getString("result").equals("ok")) {
			return new JsonResult<User>(false, null);
		}

		JSONObject user = obj.getJSONObject("user");
		JSONObject dept = obj.getJSONObject("fire_department");

		FireDepartment fireDepartment = new FireDepartmentData(
				dept.getInt("id"), dept.getString("name"),
				(float) dept.getDouble("lon"), (float) dept.getDouble("lat"));

		User userObj = new UserData(user.getInt("id"),
				user.getString("first_name"), user.getString("last_name"),
				fireDepartment);

		return new JsonResult<User>(true, userObj);
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
}
