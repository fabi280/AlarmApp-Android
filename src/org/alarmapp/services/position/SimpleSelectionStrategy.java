package org.alarmapp.services.position;

import java.util.Date;

import android.location.Location;

public class SimpleSelectionStrategy implements PositionSelectionStrategy {

	public boolean isPositionRelevant(Location location) {
		Date now = new Date();

		long ageInMs = now.getTime() - location.getTime();

		if (ageInMs > 2 * 60 * 1000) {
			return false;
		}

		return true;
	}

	public boolean isPositionRelevant(Location lastLocation,
			Location currentLocation) {

		if (hasMovedUnder100m(lastLocation, currentLocation)) {
			return false;
		}

		if (hasTurnedUnder10Degree(lastLocation, currentLocation)) {
			return false;
		}

		return true;
	}

	private boolean hasMovedUnder100m(Location lastLocation,
			Location currentLocation) {
		return Math.abs(lastLocation.distanceTo(currentLocation)) < 100;
	}

	private boolean hasTurnedUnder10Degree(Location lastLocation,
			Location currentLocation) {
		return Math.abs(lastLocation.getBearing()
				- currentLocation.getBearing()) < 10;
	}
}
