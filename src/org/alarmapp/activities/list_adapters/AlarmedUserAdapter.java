package org.alarmapp.activities.list_adapters;

import java.util.List;

import org.alarmapp.R;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.util.LogEx;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AlarmedUserAdapter extends ArrayAdapter<AlarmedUser> {

	private final Activity context;

	public AlarmedUserAdapter(Activity context, List<AlarmedUser> users) {
		super((Context) context, R.layout.list_layout, users);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			row = inflater.inflate(R.layout.list_layout, parent, false);
		}

		TextView tvName = (TextView) row.findViewById(R.id.tvTitle);
		TextView status = (TextView) row.findViewById(R.id.tvSubtitle);

		tvName.setText(getItem(position).getFullName());

		status.setText(getItem(position).getAlarmState().name());
		LogEx.info("Item at pos " + position + " is "
				+ getItem(position).getLastName());

		return (row);

	}
}
