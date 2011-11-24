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
}
