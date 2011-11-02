package org.alarmapp.activities;

import org.alarmapp.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class AlarmPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
	}
}
