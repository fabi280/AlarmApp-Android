package org.alarmapp.activities;

import java.util.Date;
import java.util.HashMap;

import org.alarmapp.Controller;
import org.alarmapp.R;
import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmState;
import org.alarmapp.model.classes.AlarmData;
import org.alarmapp.util.IntentUtil;
import org.alarmapp.util.LogEx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogEx.verbose("Hello!");

		if (!isUserAvailable()) {
			startActivity(new Intent(this, LoginActivity.class));
			return;
		} else {

			HashMap<String, String> extra = new HashMap<String, String>();
			extra.put("groups", "Probealarm");
			extra.put("fire_fighter_count", "13");
			Alarm a = new AlarmData("1", new Date(), "Probealarm",
					"Probealarm für die Feuerwehr Kleinkahl",
					AlarmState.Accepted, extra);

			IntentUtil.createDisplayAlarmIntent(this, a);

		}
		setContentView(R.layout.main);
	}

	private boolean isUserAvailable() {
		return Controller.getUser(this) != null;
	}
}