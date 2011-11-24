package org.alarmapp.web.sync;

import org.alarmapp.web.AuthWebClient;
import org.alarmapp.web.WebException;

public interface SyncCommand {
	public void Execute(AuthWebClient client) throws WebException;
}
