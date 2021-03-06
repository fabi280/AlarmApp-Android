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

package org.alarmapp.util.geo;

import java.util.Comparator;

import org.alarmapp.model.LonLat;

public class LatComparator implements Comparator<LonLat> {

	private LatComparator() {

	}

	private static LatComparator instance = null;

	public static LatComparator getInstance() {
		if (instance == null)
			instance = new LatComparator();
		return instance;
	}

	public int compare(LonLat lhs, LonLat rhs) {
		return (int) (1E6 * (lhs.getLatitude() - rhs.getLatitude()));
	}

}
