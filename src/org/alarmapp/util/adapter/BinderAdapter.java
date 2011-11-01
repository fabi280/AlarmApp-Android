package org.alarmapp.util.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public class BinderAdapter<T> extends AbstractArrayAdapter<T> {

	IAdapterBinder<T> binder;

	public BinderAdapter(Activity activity, int layoutId,
			IAdapterBinder<T> binder, List<T> items) {
		super(activity, layoutId, items);
		this.binder = binder;
	}

	@Override
	public View getView(T item, View convertView, ViewGroup parent) {
		return binder.getView(item, convertView, parent);
	}

}
