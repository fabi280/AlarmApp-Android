package org.alarmapp.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alarmapp.R;
import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmState;
import org.alarmapp.model.classes.AlarmData;
import org.alarmapp.util.IntentUtil;
import org.alarmapp.util.LogEx;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
	private Vibrator vibrator;
	private MediaPlayer mediaPlayer;

	private Alarm alarm;

	private final HashMap<String, String> extraNames = new HashMap<String, String>() {
		{
			put("fire_fighter_count", "Einsatzkräfte:");
			put("alarmed", "Alarmzeit:");
			put("groups", "Alarmgruppen:");

		}
	};

	private OnClickListener acceptClick = new OnClickListener() {
		public void onClick(View v) {
			stopRingingAndVibrating();

			alarm.setState(AlarmState.Accepted);
			IntentUtil.createAlarmStatusUpdateIntent(AlarmActivity.this, alarm);

			updateButtonBarVisibility();
		}
	};

	private OnClickListener rejectClick = new OnClickListener() {

		public void onClick(View v) {
			stopRingingAndVibrating();

			alarm.setState(AlarmState.Rejeced);
			IntentUtil.createAlarmStatusUpdateIntent(AlarmActivity.this, alarm);

			updateButtonBarVisibility();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.alarm_confirmation);
		this.lvOperationDetails = (ListView) findViewById(R.id.lvAlarmDetails);
		this.tvTitle = (TextView) findViewById(R.id.tvTitle);
		this.tvText = (TextView) findViewById(R.id.tvText);
		this.btAccept = (Button) findViewById(R.id.btAccept);
		this.btReject = (Button) findViewById(R.id.btReject);
		this.btSwitchToStatus = (Button) findViewById(R.id.btSwitchToStatus);
		this.mediaPlayer = new MediaPlayer();

		this.btAccept.setOnClickListener(acceptClick);
		this.btReject.setOnClickListener(rejectClick);
		this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		alarm = AlarmData.Create(getIntent().getExtras());

		displayAlarm();
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
			ringAndVibrate();
		}

		List<Map<String, String>> items = new ArrayList<Map<String, String>>();

		for (Map.Entry<String, String> item : alarm.getAdditionalValues()
				.entrySet()) {
			putEntryToList(items, item.getKey(), item.getValue());
		}
		SimpleDateFormat dateFormatter = new SimpleDateFormat("H:mm:ss");
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
			setButtonVisibility(View.VISIBLE, btSwitchToStatus);
		} else {
			setButtonVisibility(View.VISIBLE, btAccept, btReject);
			setButtonVisibility(View.GONE, btSwitchToStatus);
		}
	}

	private void makeActivityVisible() {
		Window w = this.getWindow(); // in Activity's onCreate() for instance
		int flags = /*
					 * WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
					 */WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
		w.setFlags(flags, flags);
	}

	private void stopRingingAndVibrating() {
		vibrator.cancel();
		mediaPlayer.pause();
		mediaPlayer.stop();

		LogEx.verbose("Ringing and vibrating for Operation "
				+ alarm.getOperationId() + " stopped");
	}

	private void ringAndVibrate() {
		int dot = 200; // Length of a Morse Code "dot" in milliseconds
		int short_gap = 200; // Length of Gap Between dots/dashes
		int medium_gap = 500; // Length of Gap Between Letters
		long[] pattern = { 0, // Start immediately
				dot, short_gap, dot, short_gap, dot, medium_gap };

		// Only perform this pattern one time (-1 means "do not repeat")
		vibrator.vibrate(pattern, 0);

		try {
			Uri alert = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_ALARM);

			mediaPlayer.setDataSource(this, alert);
			final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mediaPlayer.setLooping(true);
				mediaPlayer.prepare();
				mediaPlayer.start();
			}

			LogEx.verbose("Ringing and vibrating for Operation "
					+ alarm.getOperationId() + " started");
		} catch (Exception e) {
			LogEx.exception(e);
		}
	}
}
