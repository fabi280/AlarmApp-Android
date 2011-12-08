package org.alarmapp.services;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.alarmapp.Actions;
import org.alarmapp.AlarmApp;
import org.alarmapp.model.Alarm;
import org.alarmapp.model.LonLat;
import org.alarmapp.model.PositionMeasurementMethod;
import org.alarmapp.model.WayPoint;
import org.alarmapp.model.classes.AlarmData;
import org.alarmapp.model.classes.LonLatData;
import org.alarmapp.model.classes.WayPointData;
import org.alarmapp.services.position.PositionSelectionStrategy;
import org.alarmapp.services.position.SimpleSelectionStrategy;
import org.alarmapp.util.IntentUtil;
import org.alarmapp.util.LogEx;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

public class PositionService extends Service {

	private static final int MAX_TRACKING_TIME = 10 * 60 * 1000;
	private final IBinder mBinder = new LocalBinder();
	private LocationManager locationManager;
	private Alarm alarm;
	private Timer timer = new Timer();
	private boolean isStarted = false;
	private final PositionSelectionStrategy selector = new SimpleSelectionStrategy();
	private Location lastLocation;

	LocationListener locationListener = new LocationListener() {

		public void onLocationChanged(Location location) {

			LogEx.verbose("got position ");

			if (!selector.isPositionRelevant(location)) {
				LogEx.verbose("Position is not relevant");
				return;
			}
			if (lastLocation != null
					&& !selector.isPositionRelevant(lastLocation, location)) {
				LogEx.verbose("Position is not relevant compared to the last position");
				return;
			}
			lastLocation = location;
			LonLat position = new LonLatData((float) location.getLongitude(),
					(float) location.getLatitude());

			WayPoint wp = new WayPointData(alarm.getOperationId(), position,
					location.getSpeed(), location.getBearing(),
					location.getAccuracy(), PositionMeasurementMethod.GPS,
					new Date(location.getTime()));

			LogEx.verbose("Sending position " + position
					+ " to the Sync service.");
			IntentUtil.sendToSyncService(PositionService.this, wp);
		}

		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub

		}

		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub

		}

		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub

		}

	};

	@Override
	public void onCreate() {
		super.onCreate();

		LogEx.verbose("Position Service on Create called");
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogEx.verbose("Received start id " + startId + ": " + intent);

		if (intent.getAction().equals(Actions.START_TRACKING)) {
			Bundle bundle = intent.getExtras();
			this.alarm = AlarmData.create(bundle);

			startTracking();
		} else if (intent.getAction().equals(Actions.CANCEL_TRACKING)) {
			stopTracking();
		} else if (intent.getAction().equals(Actions.FINISHED_TRACKING)) {
			finishedTracking();
		}

		return START_NOT_STICKY;
	}

	private Intent getTrackingIntent(String action) {
		Intent intent = new Intent(this, PositionService.class);
		intent.setAction(action);
		return intent;
	}

	private void stopTracking() {
		if (!this.isStarted) {
			LogEx.warning("Tracking is already stopped. Nothing to do.");
			return;
		}
		LogEx.verbose("Stop Tracking");

		this.locationManager.removeUpdates(this.locationListener);

		this.isStarted = false;
	}

	private void startTracking() {
		if (this.isStarted) {
			LogEx.warning("Tracking already started. Nothing to do.");
			return;
		}

		LogEx.verbose("Start tracking");
		setupAbortTimer();
		setupFireDepartmentGeofence();
		setupPositionListener();

		this.isStarted = true;
	}

	private void finishedTracking() {
		LogEx.info("Finished tracking");
		stopTracking();
	}

	private void setupPositionListener() {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 100, this.locationListener);
	}

	private void setupAbortTimer() {

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				LogEx.verbose("Abort Tracking timer has expired. Sending CancelTrackingIntent");
				PositionService.this
						.startService(getTrackingIntent(Actions.CANCEL_TRACKING));
			}
		}, MAX_TRACKING_TIME);

	}

	private void setupFireDepartmentGeofence() {
		LogEx.verbose("Setting up fire departments geo fence");

		int geofenceRadius = Integer.parseInt(AlarmApp.getPreferences()
				.getString("firedepartment_geofence_radius", "150"));
		LonLat pos = AlarmApp.getUser().getFireDepartment().getPosition();

		Intent intent = getTrackingIntent(Actions.FINISHED_TRACKING);

		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
				0);

		locationManager.addProximityAlert(pos.getLatitude(),
				pos.getLongitude(), geofenceRadius, MAX_TRACKING_TIME,
				pendingIntent);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return this.mBinder;
	}

	public class LocalBinder extends Binder {
		PositionService getService() {
			return PositionService.this;
		}
	}

}
