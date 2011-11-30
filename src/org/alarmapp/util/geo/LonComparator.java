package org.alarmapp.util.geo;

import java.util.Comparator;

import org.alarmapp.model.LonLat;

public class LonComparator implements Comparator<LonLat> {

	private LonComparator() {

	}

	private static LonComparator instance = null;

	public static LonComparator getInstance() {
		if (instance == null)
			instance = new LonComparator();
		return instance;
	}

	public int compare(LonLat lhs, LonLat rhs) {

		return (int) (1E6 * (lhs.getLongitude() - rhs.getLongitude()));
	}

}
