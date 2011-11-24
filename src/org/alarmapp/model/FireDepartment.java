package org.alarmapp.model;

import java.io.Serializable;

public interface FireDepartment extends Serializable {
	public int getId();

	public LonLat getPosition();

	public void setPosition(LonLat newPosition);

	public String getName();
}
