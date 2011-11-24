package org.alarmapp.web.sync;

import org.alarmapp.model.WayPoint;
import org.alarmapp.util.LogEx;
import org.alarmapp.web.AuthWebClient;
import org.alarmapp.web.WebException;

public class PositionSyncCommand implements SyncCommand {

	public PositionSyncCommand(WayPoint waypoint) {
		this.wayPoint = waypoint;
	}

	private final WayPoint wayPoint;

	public void Execute(AuthWebClient client) throws WebException {
		LogEx.verbose("Posting the position " + wayPoint.getPosition()
				+ " to the web service");
		client.addAlarmStatusPosition(wayPoint);
	}
}
