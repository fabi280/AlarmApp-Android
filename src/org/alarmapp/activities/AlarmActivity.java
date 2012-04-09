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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alarmapp.AlarmApp;
import org.alarmapp.R;
import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmState;
import org.alarmapp.model.classes.AlarmData;
import org.alarmapp.util.ActivityUtil;
import org.alarmapp.util.Ensure;
import org.alarmapp.util.IntentUtil;
import org.alarmapp.util.LogEx;
import org.alarmapp.util.ParserUtil;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AlarmActivity extends Activity {

	private ListView lvOperationDetails;
	private TextView tvTitle;
	private TextView tvText;
	private Button btAccept;
	private Button btReject;
	private Button btSwitchToStatus;
	private LinearLayout llButtonBar;
	private Alarm alarm;

	private final HashMap<String, String> extraNames = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("ff_count", "Einsatzkräfte:");
			put("alarmed", "Alarmzeit:");
			put("groups", "Alarmgruppen:");
		}
	};

	private OnClickListener onUpdateAlarmStatusClick(final AlarmState newState) {
		return new OnClickListener() {
			public void onClick(View v) {

				cancelNotification();
				IntentUtil.stopAudioPlayerService(AlarmActivity.this);

				alarm.setState(newState);
				Alarm storedAlarm = AlarmApp.getAlarmStore().get(
						alarm.getOperationId());
				storedAlarm.setState(newState);
				storedAlarm.save();
				IntentUtil.sendToSyncService(AlarmActivity.this, alarm);

				// if (newState == AlarmState.Accepted) {
				// IntentUtil.startPositionService(AlarmActivity.this, alarm);
				// }

				updateButtonBarVisibility();
			}

		};
	}

	/**
	 * 
	 */
	private void cancelNotification() {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager notificationManager = (NotificationManager) AlarmActivity.this
				.getSystemService(ns);
		notificationManager.cancel(ParserUtil.parseInt(alarm.getOperationId(),
				0));
	}

	private OnClickListener switchToClick = new OnClickListener() {
		public void onClick(View v) {
			LogEx.info("Clicked on the Switch to Alarm Status button");
			IntentUtil
					.displayAlarmStatusUpdateIntent(AlarmActivity.this, alarm);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LogEx.verbose("AlarmActivity onCreate");

		this.setContentView(R.layout.alarm_confirmation);
		this.lvOperationDetails = (ListView) findViewById(R.id.lvAlarmDetails);
		this.tvTitle = (TextView) findViewById(R.id.tvTitle);
		this.tvText = (TextView) findViewById(R.id.tvText);
		this.btAccept = (Button) findViewById(R.id.btAccept);
		this.btReject = (Button) findViewById(R.id.btReject);
		this.btSwitchToStatus = (Button) findViewById(R.id.btSwitchToStatus);
		this.llButtonBar = (LinearLayout) findViewById(R.id.llButtonBar);

		this.btAccept
				.setOnClickListener(onUpdateAlarmStatusClick(AlarmState.Accepted));

		this.btReject
				.setOnClickListener(onUpdateAlarmStatusClick(AlarmState.Rejeced));

		this.btSwitchToStatus.setOnClickListener(switchToClick);

		if (AlarmData.isAlarmDataBundle(savedInstanceState))
			alarm = AlarmData.create(savedInstanceState);
		else {
			Ensure.valid(AlarmData.isAlarmDataBundle(getIntent().getExtras()));
			alarm = AlarmData.create(getIntent().getExtras());
		}

		displayAlarm();
	}

	@Override
	protected void onResume() {
		super.onResume();

		LogEx.verbose("AlarmActivity onResume. State is " + alarm.getState());

		if (alarm.getState().isUserActionRequired()) {
			makeActivityVisible();
		}
	}

	private void setButtonVisibility(final int visibility,
			final Button... buttons) {
		runOnUiThread(new Runnable() {

			public void run() {
				for (Button b : buttons)
					b.setVisibility(visibility);
			}
		});
	}

	private void setButtonBarVisibility(final int visibility) {
		runOnUiThread(new Runnable() {

			public void run() {
				llButtonBar.setVisibility(visibility);
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		LogEx.verbose("AlarmActivity onSaveInstance "
				+ this.alarm.getOperationId());
		outState.putAll(this.alarm.getBundle());
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();

		LogEx.verbose("AlarmActivity onPause");
	}

	private void putEntryToList(List<Map<String, String>> items, String key,
			String value) {

		// Only display Keys with Human readable Names
		if (this.extraNames.containsKey(key)) {
			HashMap<String, String> m = new HashMap<String, String>();
			m.put("key", this.extraNames.get(key));
			m.put("value", value);
			items.add(m);
		}
	}

	private void displayAlarm() {

		LogEx.info("Displaying the alarm!");

		if (alarm.getState().isUserActionRequired()) {
			makeActivityVisible();
		}

		List<Map<String, String>> items = new ArrayList<Map<String, String>>();

		for (Map.Entry<String, String> item : alarm.getAdditionalValues()
				.entrySet()) {
			putEntryToList(items, item.getKey(), item.getValue());
		}
		java.text.DateFormat dateFormatter = DateFormat.getTimeFormat(this);
		putEntryToList(items, "alarmed",
				dateFormatter.format(alarm.getAlarmed()));

		SimpleAdapter adapter = new SimpleAdapter(this, items,
				R.layout.list_layout, new String[] { "key", "value" },
				new int[] { R.id.tvTitle, R.id.tvSubtitle });
		this.lvOperationDetails.setAdapter(adapter);
		this.lvOperationDetails.smoothScrollToPosition(0);

		this.tvTitle.setText(alarm.getTitle());
		this.tvText.setText(alarm.getText());

		updateButtonBarVisibility();
	}

	private void updateButtonBarVisibility() {
		if (alarm.getState().isFinal()) {
			setButtonVisibility(View.GONE, btAccept, btReject);

			if (alarm.isAlarmStatusViewer()) {
				setButtonVisibility(View.VISIBLE, btSwitchToStatus);
				setButtonBarVisibility(View.VISIBLE);
			} else
				setButtonBarVisibility(View.GONE);

		} else {
			setButtonBarVisibility(View.VISIBLE);
			setButtonVisibility(View.VISIBLE, btAccept, btReject);
			setButtonVisibility(View.GONE, btSwitchToStatus);
		}
	}

	private void makeActivityVisible() {
		ActivityUtil.makeVisible(this);
	}

}
