package org.alarmapp.activities.list_adapters;

import java.util.List;

import org.alarmapp.R;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.util.adapter.AbstractArrayAdapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AlarmedUserAdapter extends AbstractArrayAdapter<AlarmedUser> {

	public AlarmedUserAdapter(Activity context, List<AlarmedUser> users) {
		super(context, R.layout.list_layout_alarm_status, users);
	}

	@Override
	public View getView(AlarmedUser item, View row, ViewGroup parent) {
		TextView tvName = (TextView) row.findViewById(R.id.tvTitle);
		// TextView status = (TextView) row.findViewById(R.id.tvSubtitle);
		ImageView ivStatus = (ImageView) row.findViewById(R.id.ivStatus);

		tvName.setText(item.getFullName());
		// status.setText(item.getAlarmState().name());
		ivStatus.setImageLevel(item.getAlarmState().getId());

		return (row);
	}
}
