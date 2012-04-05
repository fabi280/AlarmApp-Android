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

import org.alarmapp.model.LonLat;
import org.alarmapp.util.Ensure;

public class LonLatData implements LonLat {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final float lon;
	private final float lat;

	public LonLatData(float lon, float lat) {
		this.lat = lat;
		this.lon = lon;
	}

	public float getLongitude() {
		return lon;
	}

	public float getLatitude() {
		return lat;
	}

	public float calculateSphereDistance(LonLat other) {
		Ensure.notNull(other);

		double earthRadius = 3958.75;
		double dLat = Math.toRadians(this.lat - other.getLatitude());
		double dLng = Math.toRadians(this.lon - other.getLongitude());
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(this.lat))
				* Math.cos(Math.toRadians(other.getLatitude()))
				* Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return new Float(dist * meterConversion).floatValue();

	}

	@Override
	public String toString() {
		return Float.toString(this.lon) + "L, " + Float.toString(this.lat)
				+ "B";
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof LonLat)
			return this.equals((LonLat) o);
		return false;
	}

	public boolean equals(LonLat other) {
		return other != null
				&& Math.abs(other.getLatitude() - this.getLatitude()) < 0.00001
				&& Math.abs(other.getLongitude() - this.getLongitude()) < 0.00001;
	}
}
