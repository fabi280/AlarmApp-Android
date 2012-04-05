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

package org.alarmapp.services;

import org.alarmapp.Actions;
import org.alarmapp.AlarmApp;
import org.alarmapp.model.classes.AlarmData;
import org.alarmapp.model.classes.WayPointData;
import org.alarmapp.util.LogEx;
import org.alarmapp.util.LooperThread;
import org.alarmapp.web.WebException;
import org.alarmapp.web.sync.AlarmStatusSyncCommand;
import org.alarmapp.web.sync.PositionSyncCommand;
import org.alarmapp.web.sync.SyncCommand;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

public class SyncService extends Service {

	private final IBinder mBinder = new LocalBinder();
	private LooperThread looper = null;

	@Override
	public void onCreate() {
		super.onCreate();

		LogEx.verbose("Sync Service on Create called");

		looper = new LooperThread();
		looper.start();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogEx.verbose("Received start id " + startId + ": " + intent);

		Bundle bundle = intent.getExtras();
		if (intent.getAction().equals(Actions.UPDATE_ALARM_STATUS)) {
			LogEx.info("Adding AlarmStatus update to Looper Queue");
			looper.addWorkItem(buildUpdateAlarmStatusRunnable(new AlarmStatusSyncCommand(
					AlarmData.create(bundle))));
		} else if (intent.getAction().equals(Actions.START_TRACKING)) {
			LogEx.info("Adding Position to Looper Queue");
			looper.addWorkItem(buildUpdateAlarmStatusRunnable(new PositionSyncCommand(
					WayPointData.create(bundle))));
		}

		return START_NOT_STICKY;
	}

	private Runnable buildUpdateAlarmStatusRunnable(
			final SyncCommand syncCommand) {

		return new Runnable() {

			public void run() {
				try {
					boolean operationSuccessful = false;
					int count = 1;
					while (!operationSuccessful) {

						try {
							syncCommand.Execute(AlarmApp.getAuthWebClient());
							operationSuccessful = true;
						} catch (WebException e) {
							if (e.isPermanentFailure()) {
								LogEx.exception(
										"Failed to perform the synchronization. Aborting.",
										e);
								break;
							}
							LogEx.warning("Failed to perform Webservice Operation. Retry");
							Thread.sleep(count * 2000);
						}
						count++;
					}
				} catch (InterruptedException e1) {

					e1.printStackTrace();
				}
			}
		};
	}

	public class LocalBinder extends Binder {
		SyncService getService() {
			return SyncService.this;
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

}
