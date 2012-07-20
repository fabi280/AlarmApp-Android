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

import org.alarmapp.AlarmApp;
import org.alarmapp.R;
import org.alarmapp.util.IntentUtil;
import org.alarmapp.util.LogEx;
import org.alarmapp.util.RingtoneUtil;
import org.alarmapp.web.WebException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author Administrator
 * 
 */
public class NeedHelpActivity extends Activity {

	Button btNeedHelp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		LogEx.verbose("NeedHelpActivity");

		if (!isUserAvailable()) {
			IntentUtil.displayLoginActivity(this);
			this.finish();
			return;
		}

		setContentView(R.layout.need_help_activity);

		btNeedHelp = (Button) findViewById(R.id.btNeedHelp);
		btNeedHelp.setOnClickListener(onNeedHelpClick);

		if (!RingtoneUtil.doesAlarmDirExsist()) {

			LogEx.verbose("Install new ring tones");
			RingtoneUtil.installSwissphoneQuattro();
			RingtoneUtil.installMotorolaBMD();
		} else {
			LogEx.verbose("Ringtones already installed!");
		}

	}

	ProgressDialog progress;

	Runnable performHelpCall = new Runnable() {

		public void run() {
			try {
				LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				Location loc = lm
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);

				String message = "Hilfebedürftiger: "
						+ AlarmApp.getUser().getFullName();

				if (loc != null) {
					message += "Position: " + loc.getLatitude() + "/"
							+ loc.getLongitude();
				}

				AlarmApp.getAuthWebClient().performAlarm("help", "Hilferuf",
						message);

				// Todo: Capture all Broadcast events either AlarmStatusChange
				// or Alarmed
			} catch (WebException e) {
				LogEx.exception(e);
			} finally {
				if (NeedHelpActivity.this.progress != null) {
					NeedHelpActivity.this.progress.dismiss();
				}
			}
		}
	};

	private OnClickListener onNeedHelpClick = new OnClickListener() {

		public void onClick(View arg0) {
			LogEx.info("Pressed the need help button.");
			progress = ProgressDialog.show(NeedHelpActivity.this,
					"Hilferuf absetzen", "Bitte warten...");
			new Thread(performHelpCall).start();
		}
	};

	private boolean isUserAvailable() {
		return AlarmApp.getUser().isLoggedIn();
	}

}
