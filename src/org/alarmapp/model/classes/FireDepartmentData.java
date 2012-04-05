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

import org.alarmapp.model.FireDepartment;
import org.alarmapp.model.LonLat;

public class FireDepartmentData implements FireDepartment {

	private static final long serialVersionUID = 1L;

	public FireDepartmentData(int id, String name, float longitude,
			float latitude) {
		super();
		this.id = id;
		this.name = name;
		this.position = new LonLatData(longitude, latitude);
	}

	private int id;
	private String name;
	private LonLat position;

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return String.format("Feuerwehr %s(%d)", name, id);
	}

	public LonLat getPosition() {
		return this.position;
	}

	public void setPosition(LonLat newPosition) {
		this.position = newPosition;
	}
}
