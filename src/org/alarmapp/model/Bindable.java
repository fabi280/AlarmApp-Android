package org.alarmapp.model;

import android.os.Bundle;

public interface Bindable {
	public Bundle getBundle();

	public BindableConverter<? extends Bindable> getConverter();
}
