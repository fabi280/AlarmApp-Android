package org.alarmapp;

import org.alarmapp.util.LogEx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AlarmAppActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogEx.verbose("Hello!");

		if (!isUserAvailable()) {
			startActivity(new Intent(this, LoginActivity.class));
			return;
		}

		setContentView(R.layout.main);
	}

	private boolean isUserAvailable() {
		return Controller.getUser() != null;
	}
}