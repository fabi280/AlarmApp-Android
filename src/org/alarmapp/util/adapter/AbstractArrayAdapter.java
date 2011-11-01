package org.alarmapp.util.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public abstract class AbstractArrayAdapter<T> extends ArrayAdapter<T> {

	private final int layoutId;
	private final Activity activity;

	public AbstractArrayAdapter(Activity activity, int layoutId, List<T> items) {
		super(activity, layoutId, items);

		this.layoutId = layoutId;
		this.activity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			row = inflater.inflate(layoutId, parent, false);
		}

		T item = getItem(position);

		return getView(item, row, parent);
	}

	public abstract View getView(T item, View convertView, ViewGroup parent);
}
