package org.alarmapp.model.classes;

import java.util.Date;

import org.alarmapp.model.AlarmState;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.util.BundleUtil;
import org.alarmapp.util.DateUtil;
import org.alarmapp.util.Ensure;

import android.os.Bundle;

public class AlarmedUserData implements AlarmedUser {

	private static final String OPERATION_ID = "operation_id";
	private static final String ACK_DATE = "ack_date";
	private static final String STATE = "status";
	private static final String USER_ID = "user_id";
	private static final String LAST_NAME = "last_name";
	private static final String FIRST_NAME = "first_name";
	private String operationId;
	private String firstName;
	private String lastName;
	private String userId;
	private AlarmState state;
	private Date ackDate;

	private AlarmedUserData() {

	}

	public AlarmedUserData(String operation_id, String name, String userId,
			AlarmState state, Date ackDate) {
		super();
		String[] parts = name.split(", ");

		Ensure.valid(parts.length == 2);

		this.firstName = parts[1];
		this.lastName = parts[0];

		this.userId = userId;
		this.state = state;
		this.ackDate = ackDate;
		this.operationId = operation_id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getUserId() {
		return this.userId;
	}

	public AlarmState getAlarmState() {
		return this.state;
	}

	public Date getAcknowledgedDate() {
		return this.ackDate;
	}

	public static boolean isAlarmedUserDataBundle(Bundle b) {
		return BundleUtil.containsAll(b, ACK_DATE, FIRST_NAME, LAST_NAME,
				OPERATION_ID, STATE, USER_ID);
	}

	public static AlarmedUserData create(Bundle bundle) {
		Ensure.bundleHasKeys(bundle, OPERATION_ID, USER_ID, STATE, FIRST_NAME,
				LAST_NAME, ACK_DATE);

		AlarmedUserData a = new AlarmedUserData();
		a.ackDate = DateUtil.parse(bundle.getString(ACK_DATE));
		a.firstName = bundle.getString(FIRST_NAME);
		a.lastName = bundle.getString(LAST_NAME);
		a.state = AlarmState.create(bundle.getString(STATE));
		a.userId = bundle.getString(USER_ID);
		a.operationId = bundle.getString(OPERATION_ID);

		return a;
	}

	public Bundle getBundle() {
		Bundle bundle = new Bundle();
		bundle.putString(FIRST_NAME, this.firstName);
		bundle.putString(LAST_NAME, this.lastName);
		bundle.putString(USER_ID, this.userId);
		bundle.putString(STATE, this.state.getName());
		bundle.putString(ACK_DATE, DateUtil.format(this.ackDate));
		bundle.putString(OPERATION_ID, this.operationId);
		return bundle;
	}

	public String getOperationId() {
		return this.operationId;
	}

	public Date getAcknowledgeDate() {

		return this.ackDate;
	}

}
