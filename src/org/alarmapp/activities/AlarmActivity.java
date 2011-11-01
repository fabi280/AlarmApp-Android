package org.alarmapp.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alarmapp.AlarmApp;
import org.alarmapp.R;
import org.alarmapp.model.Alarm;
import org.alarmapp.model.AlarmState;
import org.alarmapp.model.classes.AlarmData;
import org.alarmapp.util.Ensure;
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
		private static final long serialVersionUID = 1L;

		{
			put("fire_fighter_count", "Einsatzkräfte:");
			put("alarmed", "Alarmzeit:");
			put("groups", "Alarmgruppen:");
		}
	};

	private OnClickListener onUpdateAlarmStatusClick(final AlarmState newState) {
		return new OnClickListener() {
			public void onClick(View v) {
				stopRingingAndVibrating();

				alarm.setState(newState);
				Alarm storedAlarm = AlarmApp.getAlarmStore().get(
						alarm.getOperationId());
				storedAlarm.setState(newState);
				storedAlarm.save();
				IntentUtil.createAlarmStatusUpdateIntent(AlarmActivity.this,
						alarm);

				updateButtonBarVisibility();
			}
		};
	}

	private OnClickListener switchToClick = new OnClickListener() {
		public void onClick(View v) {
			LogEx.info("Clicked on the Switch to Alarm Status button");
			IntentUtil.createDisplayAlarmStatusUpdateIntent(AlarmActivity.this,
					alarm);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LogEx.verbose("AlarmActivity onCreate");

		this.setContentView(R.layout.alarm_confirmation);
		this.lvOperationDetails = (ListView) findViewById(R.id.lvAlarmDetails);
		this.tvTitle = (TextView) findViewById(R.id.tvTitle);
		this.tvText = (TextView) findViewById(R.id.tvText);
		this.btAccept = (Button) findViewById(R.id.btAccept);
		this.btReject = (Button) findViewById(R.id.btReject);
		this.btSwitchToStatus = (Button) findViewById(R.id.btSwitchToStatus);

		this.mediaPlayer = new MediaPlayer();
		initMediaPlayer();

		this.btAccept
				.setOnClickListener(onUpdateAlarmStatusClick(AlarmState.Accepted));
		this.btReject
				.setOnClickListener(onUpdateAlarmStatusClick(AlarmState.Rejeced));
		this.btSwitchToStatus.setOnClickListener(switchToClick);

		this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		if (AlarmData.isAlarmDataBundle(savedInstanceState))
			alarm = AlarmData.create(savedInstanceState);
		else {
			Ensure.valid(AlarmData.isAlarmDataBundle(getIntent().getExtras()));
			alarm = AlarmData.create(getIntent().getExtras());
		}

		displayAlarm();
	}

	private void initMediaPlayer() {
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		try {
			mediaPlayer.setDataSource(this, alert);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
			mediaPlayer.setLooping(true);
			mediaPlayer.prepare();
		} catch (Exception e) {
			LogEx.exception(e);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		LogEx.verbose("AlarmActivity onResume. State is " + alarm.getState());

		if (alarm.getState().isUserActionRequired()) {
			makeActivityVisible();
			ringAndVibrate();
		}
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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		LogEx.verbose("AlarmActivity onSaveInstance "
				+ this.alarm.getOperationId());
		outState.putAll(this.alarm.getBundle());
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();

		LogEx.verbose("AlarmActivity onPause");

		stopRingingAndVibrating();
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
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause();

			LogEx.verbose("Ringing and vibrating for Operation "
					+ alarm.getOperationId() + " stopped");
		}
	}

	private void ringAndVibrate() {
		int dot = 200; // Length of a Morse Code "dot" in milliseconds
		int short_gap = 200; // Length of Gap Between dots/dashes
		int medium_gap = 500; // Length of Gap Between Letters
		long[] pattern = { 0, // Start immediately
				dot, short_gap, dot, short_gap, dot, medium_gap };

		// Only perform this pattern one time (-1 means "do not repeat")
		vibrator.vibrate(pattern, 0);

		final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
			if (!mediaPlayer.isPlaying())
				mediaPlayer.start();
		}

		LogEx.verbose("Ringing and vibrating for Operation "
				+ alarm.getOperationId() + " started");
	}
}
