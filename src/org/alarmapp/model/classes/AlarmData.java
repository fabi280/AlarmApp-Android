package org.alarmapp.model.classes;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmState;
import org.alarmapp.util.DateUtil;
import org.alarmapp.util.Ensure;
import org.alarmapp.util.LogEx;

import android.os.Bundle;

public class AlarmData implements Alarm {

	private String operation_id;
	private Date alarmed;
	private String title;
	private String text;
	private AlarmState state = AlarmState.Delivered;
	private HashMap<String, String> extraValues = new HashMap<String, String>();

	private static Set<String> keySet = new HashSet<String>() {
		{
			add("operation_id");
			add("alarmed");
			add("title");
			add("text");
		}
	};

	public Bundle getBundle() {
		Bundle b = new Bundle();
		b.putString("title", this.title);
		b.putString("text", this.text);
		b.putString("alarmed", DateUtil.format(this.alarmed));
		b.putString("operation_id", this.operation_id);
		b.putInt("operation_status", state.getId());

		LogEx.verbose("AlarmData Extra status is "
				+ b.getInt("operation_status"));

		for (String key : extraValues.keySet())
			b.putString(key, extraValues.get(key));

		return b;
	}

	public static AlarmData Create(Bundle extra) {

		AlarmData a = new AlarmData();
		a.operation_id = extra.getString("operation_id");
		a.alarmed = DateUtil.parse(extra.getString("alarmed"));
		a.title = extra.getString("title");
		a.text = extra.getString("text");

		LogEx.verbose("AlarmData Extra status is "
				+ extra.getInt("operation_status"));

		if (extra.containsKey("operation_status"))
			a.state = AlarmState.create(extra.getInt("operation_status"));

		for (String key : extra.keySet())
			if (isExtra(key))
				a.extraValues.put(key, extra.getString(key));
		return a;
	}

	private static boolean isExtra(String key) {
		return !keySet.contains(key);
	}

	public String getTitle() {
		return this.title;
	}

	public String getText() {
		return this.text;
	}

	public String getOperationId() {
		return this.operation_id;
	}

	public Date getAlarmed() {
		return this.alarmed;
	}

	public Map<String, String> getAdditionalValues() {
		return Collections.unmodifiableMap(this.extraValues);
	}

	public boolean isFinal() {

		return this.state.isFinal();
	}

	public AlarmState getState() {

		return this.state;
	}

	public void setState(AlarmState newState) {
		if (this.state == newState)
			return;

		Ensure.valid(this.state.isNext(newState));

		LogEx.info("Operation" + this.getOperationId() + " has now State "
				+ this.state.getName());

		this.state = newState;
	}
}
