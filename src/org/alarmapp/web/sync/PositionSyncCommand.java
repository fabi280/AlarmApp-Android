/*
 * Copyright (C) 2011-2012 AlarmApp.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
