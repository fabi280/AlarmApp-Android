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
import java.util.Map;
import java.util.Set;

import android.os.Bundle;

/**
 * Implementors of this interface should also implement a static function Alarm
 * Create(Bundle b)
 * 
 * @author frank
 * 
 */
public interface Alarm extends Serializable, Bindable {
	public String getTitle();

	public String getText();

	public String getOperationId();

	public Date getAlarmed();

	public Map<String, String> getAdditionalValues();

	public boolean isFinal();

	public boolean isAlarmStatusViewer();

	public AlarmState getState();

	public void setState(AlarmState state);

	public Set<AlarmedUser> getAlarmedUsers();

	public void setAlarmedUsers(Set<AlarmedUser> users);

	public void updateAlarmedUser(AlarmedUser user);

	public void save();

	public Bundle getBundle();

	public BindableConverter<? extends Alarm> getConverter();
}
