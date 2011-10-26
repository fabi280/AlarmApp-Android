package org.alarmapp.model.classes;

import java.util.Date;

import org.alarmapp.model.AlarmState;
import org.alarmapp.model.AlarmedUser;

public class AlarmedUserData implements AlarmedUser {

	private String firstName;
	private String lastName;
	private String userId;
	private AlarmState state;
	private Date ackDate;

	public AlarmedUserData(String firstName, String lastName, String userId,
			AlarmState state, Date ackDate) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userId = userId;
		this.state = state;
		this.ackDate = ackDate;
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

}
