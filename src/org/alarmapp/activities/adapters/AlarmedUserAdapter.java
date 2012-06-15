/*
 * Copyright (C) 2011-2012 AlarmApp.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.alarmapp.activities.adapters;

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
