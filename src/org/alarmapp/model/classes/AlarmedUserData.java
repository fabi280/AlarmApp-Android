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

package org.alarmapp.model.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.alarmapp.model.AlarmState;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.model.BindableConverter;
import org.alarmapp.model.WayPoint;
import org.alarmapp.util.BundleUtil;
import org.alarmapp.util.DateUtil;
import org.alarmapp.util.Ensure;

import android.os.Bundle;

public class AlarmedUserData implements AlarmedUser {

	private static final String FIREFIGHTER_POSITIONS = "positions";

	private static final long serialVersionUID = 1L;

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
	private ArrayList<WayPoint> positions;

	private AlarmedUserData() {

	}

	public AlarmedUserData(String operation_id, String name, String userId,
			AlarmState state, Date ackDate, ArrayList<WayPoint> positions) {
		super();
		String[] parts = name.split(", ");

		Ensure.valid(parts.length == 2);

		this.firstName = parts[1];
		this.lastName = parts[0];

		this.userId = userId;
		this.state = state;
		this.ackDate = ackDate;
		this.operationId = operation_id;
		this.positions = positions;
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

	@SuppressWarnings("unchecked")
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

		if (bundle.containsKey(FIREFIGHTER_POSITIONS)) {
			Serializable list = bundle.getSerializable(FIREFIGHTER_POSITIONS);
			if (list instanceof ArrayList<?>)
				a.positions = (ArrayList<WayPoint>) list;
		}

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
		bundle.putSerializable(FIREFIGHTER_POSITIONS, this.positions);
		return bundle;
	}

	public String getOperationId() {
		return this.operationId;
	}

	@Override
	public int hashCode() {
		return operationId.hashCode() + 31 * this.userId.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o != null && o instanceof AlarmedUser
				&& this.equals((AlarmedUser) o);
	}

	public boolean equals(AlarmedUser other) {
		if (other == null)
			return false;

		return this.operationId.equals(other.getOperationId())
				&& this.userId.equals(other.getId());
	}

	public Date getAcknowledgeDate() {

		return this.ackDate;
	}

	public String getFullName() {
		return this.lastName + ", " + this.firstName;
	}

	@Override
	public String toString() {
		return this.getFullName() + " Operation #" + this.getOperationId()
				+ ", state = " + this.state;
	}

	public List<WayPoint> getPositions() {
		if (this.positions != null)
			return Collections.unmodifiableList(this.positions);
		return new ArrayList<WayPoint>();
	}

	public boolean hasAccepted() {
		return this.state == AlarmState.Accepted;
	}

	public boolean hasRejected() {
		return this.state == AlarmState.Rejeced;
	}

	public static Comparator<AlarmedUser> StatusComparator = new Comparator<AlarmedUser>() {

		public int compare(AlarmedUser lhs, AlarmedUser rhs) {

			return (lhs.getAlarmState().getId() - rhs.getAlarmState().getId())
					* 1000 + lhs.getFullName().compareTo(rhs.getFullName());
		}
	};

	public BindableConverter<AlarmedUserData> getConverter() {
		return new BindableConverter<AlarmedUserData>() {

			public AlarmedUserData convert(Bundle b) {
				return AlarmedUserData.create(b);
			}

			public Bundle convert(AlarmedUserData obj) {
				return obj.getBundle();
			}

			public boolean canConvert(Bundle b) {
				return isAlarmedUserDataBundle(b);
			}
		};

	};

	public String getId() {
		return this.userId;
	}
}
