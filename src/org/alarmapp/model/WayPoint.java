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

import android.os.Bundle;

public interface WayPoint extends Serializable, Bindable {

	public LonLat getPosition();

	public float getSpeed();

	public float getDirection();

	public float getPrecision();

	public Date getMeasureDateTime();

	public String getOperationId();

	public PositionMeasurementMethod getMeasurementMethod();

	public Bundle getBundle();

	public BindableConverter<? extends WayPoint> getConverter();
}
