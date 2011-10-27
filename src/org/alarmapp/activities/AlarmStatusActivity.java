package org.alarmapp.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alarmapp.Broadcasts;
import org.alarmapp.Controller;
import org.alarmapp.R;
import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.model.classes.AlarmData;
import org.alarmapp.model.classes.AlarmedUserData;
import org.alarmapp.util.Ensure;
import org.alarmapp.util.LogEx;
import org.alarmapp.web.WebException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class AlarmStatusActivity extends Activity {

	Alarm alarm;
	ListView lvAlarmedUsers;

	private final BroadcastReceiver alarmStatusReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			final AlarmedUser changedUser = AlarmedUserData.create(intent
					.getExtras());

			if (changedUser.getOperationId().equals(alarm.getOperationId())) {
				runOnUiThread(new Runnable() {

					public void run() {
						refreshAlarmStatusView(changedUser);
					}
				});
			}
		}

	};

	private Runnable loadAlarmStatus = new Runnable() {

		public void run() {
			try {
				final Collection<AlarmedUser> alarmedUsers = Controller
						.getWebClient()
						.getAlarmStatus(
								Controller.getUser(AlarmStatusActivity.this)
										.getAuthToken(), alarm.getOperationId());

				runOnUiThread(new Runnable() {
					public void run() {
						displayAlarmStatusView(alarmedUsers);

					}
				});

			} catch (WebException e) {
				LogEx.exception("Failed to load the user list", e);
			}

		}
	};

	private void putEntryToList(List<Map<String, String>> items, String key,
			String value) {

		HashMap<String, String> m = new HashMap<String, String>();
		m.put("name", key);
		m.put("state", value);
		items.add(m);

	}

	private List<Map<String, String>> itemRenderer(Collection<AlarmedUser> users) {
		List<Map<String, String>> items = new ArrayList<Map<String, String>>();
		for (AlarmedUser u : users) {
			putEntryToList(items, u.getUserId() + " " + u.getLastName() + ", "
					+ u.getFirstName(), u.getAlarmState().getName());
		}
		return items;
	}

	private Map<String, AlarmedUser> userMap;

	private void fillUserMap(Collection<AlarmedUser> users) {
		userMap = new HashMap<String, AlarmedUser>();

		for (AlarmedUser user : users)
			userMap.put(user.getUserId(), user);
	}

	private void displayAlarmStatusView(Collection<AlarmedUser> users) {
		this.cancelProgressBar();
		fillUserMap(users);
		fillAlarmStatusView();
	}

	private void fillAlarmStatusView() {
		SimpleAdapter adapter = new SimpleAdapter(this,
				itemRenderer(userMap.values()), R.layout.list_layout,
				new String[] { "name", "state" }, new int[] { R.id.tvTitle,
						R.id.tvSubtitle });
		this.lvAlarmedUsers.setAdapter(adapter);
	}

	private void refreshAlarmStatusView(AlarmedUser changedUser) {
		LogEx.verbose("Received Alarm status Update for User "
				+ changedUser.getFirstName() + " " + changedUser.getLastName());

		userMap.put(changedUser.getUserId(), changedUser);
		fillAlarmStatusView();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.alarm_status);

		this.lvAlarmedUsers = (ListView) findViewById(R.id.lvAlarmedUserList);

		Ensure.valid(AlarmData.isAlarmDataBundle(getIntent().getExtras()));
		alarm = AlarmData.create(getIntent().getExtras());

		// Todo: listen for status changes
		Broadcasts.registerForAlarmstatusChangedBroadcast(this,
				alarmStatusReceiver);

		startFetchAlarmStatus();
	}

	private void startFetchAlarmStatus() {
		displayProgressBar();

		new Thread(this.loadAlarmStatus).start();
	}

	ProgressDialog dialog;

	private void displayProgressBar() {
		this.dialog = ProgressDialog.show(AlarmStatusActivity.this, "",
				"Loading. Please wait...", true);

		dialog.show();
	}

	private void cancelProgressBar() {
		dialog.cancel();
	}
}
