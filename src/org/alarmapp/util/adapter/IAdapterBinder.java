package org.alarmapp.util.adapter;

import android.view.View;
import android.view.ViewGroup;

public interface IAdapterBinder<T> {
	public View getView(T item, View convertView, ViewGroup parent);
}
