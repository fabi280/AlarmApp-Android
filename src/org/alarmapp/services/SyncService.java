package org.alarmapp.services;

import org.alarmapp.Actions;
import org.alarmapp.AlarmApp;
import org.alarmapp.model.Alarm;
import org.alarmapp.model.classes.AlarmData;
import org.alarmapp.util.LogEx;
import org.alarmapp.util.LooperThread;
import org.alarmapp.web.WebException;

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
			LogEx.info("Adding Work Item to Looper Queue");
			looper.addWorkItem(buildUpdateAlarmStatusRunnable(bundle));
		}

		return START_NOT_STICKY;
	}

	private Runnable buildUpdateAlarmStatusRunnable(Bundle extras) {
		final Alarm alarm = AlarmData.create(extras);

		return new Runnable() {

			public void run() {
				try {
					boolean operationSuccessful = false;
					int count = 1;
					while (!operationSuccessful) {

						try {
							LogEx.verbose("Set Alarm Status to "
									+ alarm.getState());
							AlarmApp.getWebClient().setAlarmStatus(alarm);
							operationSuccessful = true;
						} catch (WebException e) {
							if (e.isPermanentFailure()) {
								LogEx.exception(
										"Failed to update the alarm status. Aborting.",
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
		// TODO Auto-generated method stub
		return mBinder;
	}

}
