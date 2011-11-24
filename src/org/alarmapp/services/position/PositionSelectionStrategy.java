package org.alarmapp.services.position;

import android.location.Location;

public interface PositionSelectionStrategy {

	public boolean isPositionRelevant(Location location);

	public boolean isPositionRelevant(Location lastLocation,
			Location currentLocation);
}
