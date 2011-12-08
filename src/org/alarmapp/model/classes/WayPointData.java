package org.alarmapp.model.classes;

import java.util.Date;

import org.alarmapp.model.BindableConverter;
import org.alarmapp.model.LonLat;
import org.alarmapp.model.PositionMeasurementMethod;
import org.alarmapp.model.WayPoint;
import org.alarmapp.util.BundleUtil;
import org.alarmapp.util.DateUtil;
import org.alarmapp.util.Ensure;

import android.os.Bundle;

public class WayPointData implements WayPoint {

	private static final String OPERATION_ID = "operation_id";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String MEASUREMENT_METHOD = "measurement_method";
	private static final String PRECISION = "precision";
	private static final String DIRECTION = "direction";
	private static final String SPEED = "speed";
	private static final String LON = "lon";
	private static final String LAT = "lat";
	private static final String DATE = "date";

	public WayPointData(String OperationId, LonLat position, float speed,
			float direction, float precision,
			PositionMeasurementMethod measurementMethod, Date date) {
		this.operationId = OperationId;
		this.position = position;
		this.speed = speed;
		this.direction = direction;
		this.precision = precision;
		this.measurementMethod = measurementMethod;
		this.date = date;
	}

	private LonLat position;
	private float speed;
	private float direction;
	private float precision;
	private java.util.Date date;
	private String operationId;
	private PositionMeasurementMethod measurementMethod;

	public LonLat getPosition() {
		return position;
	}

	public float getSpeed() {
		return speed;
	}

	public float getDirection() {
		return direction;
	}

	public float getPrecision() {
		return precision;
	}

	public PositionMeasurementMethod getMeasurementMethod() {
		return measurementMethod;
	}

	public Date getMeasureDateTime() {
		return date;
	}

	public static WayPointData create(Bundle bundle) {
		Ensure.bundleHasKeys(bundle, DATE, OPERATION_ID, LAT, LON, SPEED,
				DIRECTION, PRECISION, MEASUREMENT_METHOD);

		LonLat pos = new LonLatData(bundle.getFloat(LON), bundle.getFloat(LAT));
		PositionMeasurementMethod src = PositionMeasurementMethod.Create(bundle
				.getString(MEASUREMENT_METHOD));
		Date date = DateUtil.parse(bundle.getString(DATE));
		return new WayPointData(bundle.getString(OPERATION_ID), pos,
				bundle.getFloat(SPEED), bundle.getFloat(DIRECTION),
				bundle.getFloat(PRECISION), src, date);

	}

	public static boolean isWayPointBundle(Bundle bundle) {
		return BundleUtil.containsAll(bundle, OPERATION_ID, DATE, LAT, LON,
				SPEED, DIRECTION, PRECISION, MEASUREMENT_METHOD);
	}

	public Bundle getBundle() {
		Bundle b = new Bundle();
		b.putString(OPERATION_ID, this.operationId);
		b.putString(DATE, DateUtil.format(this.date));
		b.putFloat(LAT, position.getLatitude());
		b.putFloat(LON, position.getLongitude());
		b.putFloat(SPEED, speed);
		b.putFloat(DIRECTION, direction);
		b.putFloat(PRECISION, precision);
		b.putString(MEASUREMENT_METHOD, measurementMethod.toString());
		return b;
	}

	public String getOperationId() {
		return this.operationId;
	}

	public BindableConverter<WayPointData> getConverter() {
		return new BindableConverter<WayPointData>() {

			public boolean canConvert(Bundle b) {
				return WayPointData.isWayPointBundle(b);
			}

			public Bundle convert(WayPointData obj) {
				return obj.getBundle();
			}

			public WayPointData convert(Bundle b) {
				return WayPointData.create(b);
			}
		};
	}
}
