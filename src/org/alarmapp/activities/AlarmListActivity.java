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

package org.alarmapp.activities;

import org.alarmapp.AlarmApp;
import org.alarmapp.R;
import org.alarmapp.model.Alarm;
import org.alarmapp.util.IntentUtil;
import org.alarmapp.util.adapter.BinderAdapter;
import org.alarmapp.util.adapter.IAdapterBinder;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AlarmListActivity extends ListActivity {

	private IAdapterBinder<Alarm> binder = new IAdapterBinder<Alarm>() {

		public View getView(Alarm item, View row, ViewGroup parent) {
			TextView tvTitle = (TextView) row.findViewById(R.id.tvTitle);
			TextView tvText = (TextView) row.findViewById(R.id.tvText);
			ImageView ivStatus = (ImageView) row.findViewById(R.id.ivStatus);

			ivStatus.setImageLevel(item.getState().getId());
			tvTitle.setText(item.getTitle());
			tvText.setText(item.getAlarmed().toLocaleString());

			return row;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_list);

		this.getListView().setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> view, View row, int pos,
					long arg3) {
				Alarm a = (Alarm) view.getItemAtPosition(pos);
				IntentUtil.displayAlarmActivity(AlarmListActivity.this, a);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

		BinderAdapter<Alarm> adapter = new BinderAdapter<Alarm>(this,
				R.layout.list_layout_alarm_list, binder, AlarmApp
						.getAlarmStore().getLastAlarms());
		setListAdapter(adapter);
	}
}
