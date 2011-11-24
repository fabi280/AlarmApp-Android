package org.alarmapp.model;

import java.io.Serializable;

public interface LonLat extends Serializable {
	public float getLongitude();

	public float getLatitude();

	public float calculateSphereDistance(LonLat other);
}
