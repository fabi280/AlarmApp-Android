package org.alarmapp.activities.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.alarmapp.AlarmApp;
import org.alarmapp.R;
import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.model.LonLat;
import org.alarmapp.model.WayPoint;
import org.alarmapp.model.classes.AlarmData;
import org.alarmapp.util.DateUtil;
import org.alarmapp.util.Ensure;
import org.alarmapp.util.geo.LatComparator;
import org.alarmapp.util.geo.LonComparator;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class AlarmMapActivity extends MapActivity {

	MapController controller;
	MapView map;
	Alarm alarm;
	private MarkerOverlay fireDepartmentOverlay;
	private MarkerOverlay fireFighterOverlay;

	private GeoPoint toGeoPoint(LonLat pos) {
		return new GeoPoint((int) (pos.getLatitude() * 1E6),
				(int) (pos.getLongitude() * 1E6));
	}

	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.alarm_map);

		Ensure.valid(AlarmData.isAlarmDataBundle(getIntent().getExtras()));

		this.alarm = AlarmData.create(getIntent().getExtras());
		this.map = (MapView) findViewById(R.id.mapview);
		this.controller = map.getController();

		this.map.setBuiltInZoomControls(true);

		initFiredepartmentOverlay();
		initFirefighterOverlay();

		displayFireDepartment();
		displayFireFighters();
	}

	private void initFirefighterOverlay() {
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.map_marker_blue);

		fireFighterOverlay = new MarkerOverlay(drawable, this);
		List<Overlay> mapOverlays = this.map.getOverlays();
		mapOverlays.add(fireFighterOverlay);
	}

	private void initFiredepartmentOverlay() {
		Drawable drawable = this.getResources().getDrawable(
				R.drawable.map_marker_red);
		fireDepartmentOverlay = new MarkerOverlay(drawable, this);
		List<Overlay> mapOverlays = this.map.getOverlays();
		mapOverlays.add(fireDepartmentOverlay);
	}

	private void displayFireDepartment() {
		LonLat pos = AlarmApp.getUser().getFireDepartment().getPosition();
		GeoPoint point = toGeoPoint(pos);

		controller.animateTo(point);
		controller.setZoom(17);
		map.invalidate();

		OverlayItem overlayitem = new OverlayItem(point, "Feuerwehrhaus",
				"Ziel");

		fireDepartmentOverlay.addOverlay(overlayitem);
	}

	private void displayFireFighters() {
		ArrayList<WayPoint> waypoints = new ArrayList<WayPoint>();
		ArrayList<LonLat> positions = new ArrayList<LonLat>();
		for (AlarmedUser user : alarm.getAlarmedUsers()) {

			if (user.getPositions().size() == 0)
				continue;

			WayPoint waypoint = user.getPositions().get(
					user.getPositions().size() - 1);

			waypoints.add(waypoint);
			positions.add(waypoint.getPosition());

			GeoPoint point = toGeoPoint(waypoint.getPosition());
			OverlayItem overlayitem = new OverlayItem(point,
					user.getFullName(), DateUtil.format(waypoint
							.getMeasureDateTime()));

			fireFighterOverlay.addOverlay(overlayitem);
		}
		positions.add(AlarmApp.getUser().getFireDepartment().getPosition());
		setZoomToFitAllPoints(positions);
	}

	private void setZoomToFitAllPoints(List<LonLat> positions) {
		if (positions.size() < 2)
			return;

		LonLat minLon = Collections.min(positions, LonComparator.getInstance());
		LonLat maxLon = Collections.max(positions, LonComparator.getInstance());

		LonLat minLat = Collections.min(positions, LatComparator.getInstance());
		LonLat maxLat = Collections.max(positions, LatComparator.getInstance());

		controller.zoomToSpan(
				(int) (1E6 * (maxLat.getLatitude() - minLat.getLatitude())),
				(int) (1E6 * (maxLon.getLongitude() - minLon.getLongitude())));
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
