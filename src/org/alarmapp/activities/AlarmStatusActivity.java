package org.alarmapp.activities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.alarmapp.AlarmApp;
import org.alarmapp.Broadcasts;
import org.alarmapp.R;
import org.alarmapp.activities.list_adapters.AlarmedUserAdapter;
import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmedUser;
import org.alarmapp.model.classes.AlarmData;
import org.alarmapp.model.classes.AlarmedUserData;
import org.alarmapp.util.ActivityUtil;
import org.alarmapp.util.Ensure;
import org.alarmapp.util.LogEx;
import org.alarmapp.web.WebException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;

public class AlarmStatusActivity extends Activity {

	Alarm alarm;
	ListView lvAlarmedUsers;
	ImageButton btRefresh;

	private final OnClickListener refreshListener = new OnClickListener() {

		public void onClick(View v) {
			startFetchAlarmStatus();
		}
	};

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
				final Collection<AlarmedUser> alarmedUsers = AlarmApp
						.getWebClient().getAlarmStatus(alarm.getOperationId());

				runOnUiThread(new Runnable() {
					public void run() {
						displayAlarmStatusView(alarmedUsers);
					}
				});

			} catch (final WebException e) {
				LogEx.exception("Failed to load the user list", e);

				runOnUiThread(new Runnable() {
					public void run() {
						cancelProgressBar();
						displayError(e.getMessage());
					}
				});
			}

		}
	};

	private HashSet<AlarmedUser> alarmedUsers = new HashSet<AlarmedUser>();

	private void addUser(AlarmedUser user) {
		if (alarmedUsers.contains(user)) {
			alarmedUsers.remove(user);
		}
		alarmedUsers.add(user);

		LogEx.verbose("Added " + user + ". Hash is " + alarmedUsers.hashCode()
				+ ". User id is " + user.getUserId());
	}

	private void displayAlarmStatusView(Collection<AlarmedUser> users) {
		LogEx.verbose("Display the alarm status for " + users.size()
				+ " fire fighters");

		this.cancelProgressBar();

		for (AlarmedUser u : users)
			addUser(u);

		LogEx.verbose("Alarmed user collection contains " + alarmedUsers.size()
				+ " elements");

		fillAlarmStatusView();
	}

	private void fillAlarmStatusView() {
		AlarmedUserAdapter adapter = new AlarmedUserAdapter(this,
				new ArrayList<AlarmedUser>(this.alarmedUsers));

		this.lvAlarmedUsers.setAdapter(adapter);
	}

	private void refreshAlarmStatusView(AlarmedUser changedUser) {
		LogEx.verbose("Received Alarm status Update for User " + changedUser);

		if (!this.alarmedUsers.contains(changedUser))
			LogEx.warning("Alarmed User does not contain user " + changedUser);

		addUser(changedUser);
		fillAlarmStatusView();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LogEx.info("Started AlarmStatusActivity");

		setContentView(R.layout.alarm_status);

		this.lvAlarmedUsers = (ListView) findViewById(R.id.lvAlarmedUserList);
		this.btRefresh = (ImageButton) findViewById(R.id.btRefresh);

		Ensure.valid(AlarmData.isAlarmDataBundle(getIntent().getExtras()));
		alarm = AlarmData.create(getIntent().getExtras());

		// Todo: listen for status changes
		Broadcasts.registerForAlarmstatusChangedBroadcast(this,
				alarmStatusReceiver);

		btRefresh.setOnClickListener(refreshListener);

		startFetchAlarmStatus();
		makeActivityVisible();
	}

	private void makeActivityVisible() {
		Window w = this.getWindow(); // in Activity's onCreate() for instance
		int flags = /*
					 * WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
					 */WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
		w.setFlags(flags, flags);
	}

	private void displayError(String message) {
		ActivityUtil.displayToast(this, message, 15);
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
