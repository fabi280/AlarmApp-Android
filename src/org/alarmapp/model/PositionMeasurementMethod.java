package org.alarmapp.model;

public enum PositionMeasurementMethod {
	WLAN("WLAN"), GPS("GPS"), UNKNWON("?");

	private final String type;

	PositionMeasurementMethod(String type) {
		this.type = type;
	}

	public static PositionMeasurementMethod Create(String kind) {
		if (kind.equals(WLAN.type))
			return WLAN;
		else if (kind.equals(GPS.type))
			return GPS;
		throw new IllegalArgumentException("The kind " + kind + " is unknown.");
	}

	@Override
	public String toString() {
		return type;
	}
}
