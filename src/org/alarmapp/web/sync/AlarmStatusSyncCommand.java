package org.alarmapp.web.sync;

import org.alarmapp.model.Alarm;
import org.alarmapp.util.LogEx;
import org.alarmapp.web.AuthWebClient;
import org.alarmapp.web.WebException;

public class AlarmStatusSyncCommand implements SyncCommand {

	public AlarmStatusSyncCommand(Alarm alarm) {
		super();
		this.alarm = alarm;
	}

	private final Alarm alarm;

	public void Execute(AuthWebClient client) throws WebException {
		LogEx.verbose("Set Alarm Status to " + alarm.getState());
		client.setAlarmStatus(alarm);
	}

}
