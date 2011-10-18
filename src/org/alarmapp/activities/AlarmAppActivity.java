package org.alarmapp.activities;

import org.alarmapp.Actions;
import org.alarmapp.Controller;
import org.alarmapp.R;
import org.alarmapp.services.SyncService;
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
		} else {

			Intent intent = new Intent(this, SyncService.class);
			intent.putExtra("text", "Einsatz für die Feuerwehr Kleinkahl");
			intent.putExtra("title", "Einsatz");
			intent.putExtra("operation_id", "25008");
			intent.putExtra("alarmed", "2011-10-17 14:29:12");
			intent.putExtra("groups",
					"First Responder, Fahrer, Atemschutzträger");
			intent.putExtra("fire_fighters", "5");

			intent.setAction(Actions.UPDATE_ALARM_STATUS);
			this.startService(intent);
		}
		setContentView(R.layout.main);
	}

	private boolean isUserAvailable() {
		return Controller.getUser(this) != null;
	}
}