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

package org.alarmapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import android.os.Bundle;

public interface AlarmedUser extends Serializable, Bindable, Person {
	public String getFirstName();

	public String getLastName();

	public String getId();

	public String getFullName();

	public AlarmState getAlarmState();

	public String getOperationId();

	public Date getAcknowledgeDate();

	public List<WayPoint> getPositions();

	public Bundle getBundle();

	public boolean hasAccepted();

	public boolean hasRejected();

	public BindableConverter<? extends AlarmedUser> getConverter();
}
