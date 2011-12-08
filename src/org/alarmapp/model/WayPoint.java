package org.alarmapp.model;

import java.io.Serializable;
import java.util.Date;

import android.os.Bundle;

public interface WayPoint extends Serializable, Bindable {

	public LonLat getPosition();

	public float getSpeed();

	public float getDirection();

	public float getPrecision();

	public Date getMeasureDateTime();

	public String getOperationId();

	public PositionMeasurementMethod getMeasurementMethod();

	public Bundle getBundle();

	public BindableConverter<? extends WayPoint> getConverter();
}
