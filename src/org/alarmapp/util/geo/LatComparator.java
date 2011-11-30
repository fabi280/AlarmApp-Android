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
