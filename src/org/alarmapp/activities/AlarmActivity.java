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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AlarmActivity extends Activity {

	private ListView lvOperationDetails;
	private TextView tvTitle;
	private TextView tvText;
	private TextView tvAlarmed;
	private Button btAccept;
	private Button btReject;
	private Vibrator vibrator;
	private MediaPlayer mediaPlayer;

	private Alarm alarm;

	private OnClickListener acceptClick = new OnClickListener() {
		public void onClick(View v) {
			alarm.setState(AlarmState.Accepted);
			IntentUtil.createAlarmStatusUpdateIntent(AlarmActivity.this, alarm);
			setButtonVisibility(View.INVISIBLE);

			vibrator.cancel();
			mediaPlayer.stop();
		}
	};

	private OnClickListener rejectClick = new OnClickListener() {

		public void onClick(View v) {
			alarm.setState(AlarmState.Rejeced);
			IntentUtil.createAlarmStatusUpdateIntent(AlarmActivity.this, alarm);
			setButtonVisibility(View.INVISIBLE);

			vibrator.cancel();
			mediaPlayer.stop();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.alarm_confirmation);
		this.lvOperationDetails = (ListView) findViewById(R.id.lvAlarmDetails);
		this.tvTitle = (TextView) findViewById(R.id.tvTitle);
		this.tvText = (TextView) findViewById(R.id.tvText);
		this.tvAlarmed = (TextView) findViewById(R.id.tvDate);
		this.btAccept = (Button) findViewById(R.id.btAccept);
		this.btReject = (Button) findViewById(R.id.btReject);
		this.mediaPlayer = new MediaPlayer();

		this.btAccept.setOnClickListener(acceptClick);
		this.btReject.setOnClickListener(rejectClick);
		this.vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		alarm = AlarmData.Create(getIntent().getExtras());
		displayAlarm();
	}

	private void setButtonVisibility(final int visibility) {
		runOnUiThread(new Runnable() {

			public void run() {
				btAccept.setVisibility(visibility);
				btReject.setVisibility(visibility);
			}
		});
	}

	private void displayAlarm() {

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
		} catch (Exception e) {
			LogEx.exception(e);
		}

		List<Map<String, String>> items = new ArrayList<Map<String, String>>();

		for (Map.Entry<String, String> item : alarm.getAdditionalValues()
				.entrySet()) {
			HashMap<String, String> m = new HashMap<String, String>();
			m.put("key", item.getKey());
			m.put("value", item.getValue());
			items.add(m);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, items,
				R.layout.list_layout, new String[] { "key", "value" },
				new int[] { R.id.tvTitle, R.id.tvSubtitle });
		this.lvOperationDetails.setAdapter(adapter);
		this.lvOperationDetails.smoothScrollToPosition(0);

		this.tvTitle.setText(alarm.getTitle());
		this.tvText.setText(alarm.getText());

		SimpleDateFormat dateFormatter = new SimpleDateFormat("H:m:s");

		this.tvAlarmed.setText("Alarmzeit: "
				+ dateFormatter.format(alarm.getAlarmed()));

		if (!alarm.isFinal())
			setButtonVisibility(View.VISIBLE);
		else
			setButtonVisibility(View.INVISIBLE);
	}
}
