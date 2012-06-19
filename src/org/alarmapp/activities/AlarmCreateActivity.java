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
import org.alarmapp.model.AlarmGroup;
import org.alarmapp.util.IntentUtil;
import org.alarmapp.util.LogEx;
import org.alarmapp.web.WebException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
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
			// TODO Gruppen Laden bei MainActivityStart nicht bei
			// AlarmCreateActivity start
			alarmGroups = AlarmApp.getAuthWebClient().getAlarmGroups();
			ArrayAdapter<AlarmGroup> adapter = new ArrayAdapter<AlarmGroup>(
					this, android.R.layout.simple_spinner_item, alarmGroups);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
			String title = etAlarmTitle.getText().toString();
			String text = etAlarmText.getText().toString();
			String adress = etAdress.getText().toString();
			int alarmGroup = spAlarmGroup.getSelectedItemPosition();
			LogEx.info("Alarmierung ausgelöst " + text
					+ alarmGroups.get(alarmGroup).getName());
			try {
				// TODO spinner einfügen
				// in Thread auslagern
				AlarmApp.getAuthWebClient().performAlarm(
						alarmGroups.get(alarmGroup).getGroupID(), title, text);
				// TODO bestätigung anzeigen und activity schließen
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
