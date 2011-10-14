package org.alarmapp.model.classes;

import org.alarmapp.model.FireDepartment;

public class FireDepartmentData implements FireDepartment {

	public FireDepartmentData(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	private int id;
	public String name;

	public int GetId() {
		return this.id;
	}

	public String GetName() {
		return this.name;
	}

	@Override
	public String toString() {
		return String.format("Feuerwehr %s(%d)", name, id);
	}
}
