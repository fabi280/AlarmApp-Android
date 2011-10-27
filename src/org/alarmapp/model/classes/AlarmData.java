package org.alarmapp.model.classes;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmState;
import org.alarmapp.util.BundleUtil;
import org.alarmapp.util.DateUtil;
import org.alarmapp.util.Ensure;
import org.alarmapp.util.LogEx;

import android.os.Bundle;

public class AlarmData implements Alarm {

	private static final String OPERATION_STATUS = "operation_status";

	private static final String TEXT = "text";

	private static final String TITLE = "title";

	private static final String ALARMED = "alarmed";

	private static final String OPERATION_ID = "operation_id";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String operation_id;
	private Date alarmed;
	private String title;
	private String text;
	private AlarmState state = AlarmState.Delivered;
	private HashMap<String, String> extraValues = new HashMap<String, String>();

	private static HashSet<String> keySet = new HashSet<String>() {
		private static final long serialVersionUID = 1L;

		{
			add(OPERATION_ID);
			add(ALARMED);
			add(TITLE);
			add(TEXT);
			add(OPERATION_STATUS);
		}
	};

	public Bundle getBundle() {
		Bundle b = new Bundle();
		b.putString(TITLE, this.title);
		b.putString(TEXT, this.text);
		b.putString(ALARMED, DateUtil.format(this.alarmed));
		b.putString(OPERATION_ID, this.operation_id);
		b.putInt(OPERATION_STATUS, state.getId());

		LogEx.verbose("AlarmData Extra status is " + b.getInt(OPERATION_STATUS));

		for (String key : extraValues.keySet())
			b.putString(key, extraValues.get(key));

		return b;
	}

	private AlarmData() {

	}

	public AlarmData(String operationId, Date alarmed, String title,
			String text, AlarmState state, HashMap<String, String> extras) {
		this.operation_id = operationId;
		this.state = state;
		this.text = text;
		this.title = title;
		this.alarmed = alarmed;

		if (extras != null)
			this.extraValues = new HashMap<String, String>(extras);
		else
			this.extraValues = new HashMap<String, String>();
	}

	public static boolean isAlarmDataBundle(Bundle extra) {
		return BundleUtil
				.containsAll(extra, OPERATION_ID, TITLE, TEXT, ALARMED);
	}

	public static AlarmData create(Bundle extra) {

		AlarmData a = new AlarmData();

		a.operation_id = extra.getString(OPERATION_ID);
		a.alarmed = DateUtil.parse(extra.getString(ALARMED));
		a.title = extra.getString(TITLE);
		a.text = extra.getString(TEXT);

		LogEx.verbose("AlarmData Extra status is "
				+ extra.getInt(OPERATION_STATUS));

		if (extra.containsKey(OPERATION_STATUS))
			a.state = AlarmState.create(extra.getInt(OPERATION_STATUS));

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
