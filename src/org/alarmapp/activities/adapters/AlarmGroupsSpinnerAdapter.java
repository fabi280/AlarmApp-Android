/*
 * Copyright (C) 2011 AlarmApp.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.alarmapp.activities.adapters;

import java.util.List;

import org.alarmapp.model.AlarmGroup;
import org.alarmapp.util.adapter.AbstractArrayAdapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AlarmGroupsSpinnerAdapter extends AbstractArrayAdapter<AlarmGroup> {

	public AlarmGroupsSpinnerAdapter(Activity context,
			List<AlarmGroup> alarmGroups) {
		super(context, android.R.layout.simple_spinner_item, alarmGroups);
	}

	@Override
	public View getView(AlarmGroup item, View convertView, ViewGroup parent) {
		TextView tvTitle = (TextView) convertView
				.findViewById(android.R.id.text1);
		tvTitle.setText(item.getName());

		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO: das Layout noch anpassen, dass das ganze auch nach was aussieht
		View row = super.getView(position, convertView, parent);
		TextView tvTitle = (TextView) row.findViewById(android.R.id.text1);
		tvTitle.setTextSize(30.0f);
		tvTitle.setText(getItem(position).getName());
		return row;
	}
}
