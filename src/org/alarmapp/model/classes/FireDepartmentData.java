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
