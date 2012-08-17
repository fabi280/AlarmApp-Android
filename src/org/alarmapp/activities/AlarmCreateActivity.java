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

package org.alarmapp.activities;

import java.util.List;

import org.alarmapp.AlarmApp;
import org.alarmapp.R;
import org.alarmapp.activities.adapters.AlarmGroupsSpinnerAdapter;
import org.alarmapp.model.AlarmGroup;
import org.alarmapp.util.IntentUtil;
import org.alarmapp.util.LogEx;
import org.alarmapp.web.WebException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AlarmCreateActivity extends Activity {

	private EditText etAlarmTitle;
	private EditText etAlarmText;
	private Spinner spAlarmGroup;
	private EditText etAdress;
	private TextView tvAdress;
	private Button btCreateAlarm;

	private List<AlarmGroup> alarmGroups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.alarm_create);

		LogEx.verbose("AlarmCreateActivity");

		this.etAdress = (EditText) findViewById(R.id.etAdress);
		this.tvAdress = (TextView) findViewById(R.id.lAdresse);
		this.etAlarmText = (EditText) findViewById(R.id.etAlarmText);
		this.etAlarmTitle = (EditText) findViewById(R.id.etAlarmTitel);
		this.btCreateAlarm = (Button) findViewById(R.id.bAlarmCreate);
		this.spAlarmGroup = (Spinner) findViewById(R.id.sAlarmGroup);

		// TODO: wegen nicht benutzung erstmal ausgeblendet
		etAdress.setVisibility(etAdress.GONE);
		tvAdress.setVisibility(tvAdress.GONE);

		btCreateAlarm.setOnClickListener(alarmCreateListener);
		try {
			alarmGroups = AlarmApp.getAuthWebClient().getAlarmGroups();
			AlarmGroupsSpinnerAdapter adapter = new AlarmGroupsSpinnerAdapter(
					this, alarmGroups);
			spAlarmGroup.setAdapter(adapter);
			spAlarmGroup.setSelection(0);

		} catch (WebException e) {
			LogEx.exception(e);
		}
	}

	private final OnClickListener alarmCreateListener = new OnClickListener() {

		public void onClick(View v) {
			createAlarm();
		}
	};

	private void createAlarm() {
		if (etAlarmTitle.getText().length() != 0) {
			String text = etAlarmTitle.getText().toString();
			String title = etAlarmText.getText().toString();
			String adress = etAdress.getText().toString();
			int alarmGroup = spAlarmGroup.getSelectedItemPosition();

			LogEx.info("Alarmierung ausgelöst " + text
					+ alarmGroups.get(alarmGroup).getName());
			try {
				AlarmApp.getAuthWebClient().performAlarm(
						alarmGroups.get(alarmGroup).getCode(), title, text);
			} catch (WebException e) {
				LogEx.exception(e);
				Toast t = Toast.makeText(this, "Fehler bei der Alarmierung ("
						+ e.getMessage() + ")", 10);
				t.show();
			}

		} else {
			Toast t = Toast.makeText(this,
					"Sie müssen mindestens den Alarmtitel angeben!", 10);
			t.show();
		}
	}

	@Override
	public void onBackPressed() {
		IntentUtil.displayMainActivity(this);
	}
}
