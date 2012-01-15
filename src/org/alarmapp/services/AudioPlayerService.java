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

package org.alarmapp.services;

import org.alarmapp.AlarmApp;
import org.alarmapp.model.Alarm;
import org.alarmapp.model.classes.AlarmData;
import org.alarmapp.util.AlarmAppWakeLock;
import org.alarmapp.util.LogEx;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * @author frankenglert
 * 
 * @see com.android.alarmclock.AlarmKlaxon.java
 * 
 * @based_on Please mind: This class is based on the AudioKlaxton. You could
 *           find this class in the Android Alarm Clock.
 * 
 */
public class AudioPlayerService extends Service {

	private static final int ALARM_TIMEOUT_SECONDS = 10 * 60;

	private static final long[] sVibratePattern = new long[] { 500, 500 };

	private boolean mPlaying = false;
	private Vibrator mVibrator;
	private MediaPlayer mMediaPlayer;
	private TelephonyManager mTelephonyManager;
	private int mInitialCallState;
	private Alarm alarm;

	public static final String START_PLAYING = "AudioPlayerService.startPlaying";
	public static final String STOP_PLAYING = "AudioPlayerService.stopPlaying";

	// Internal messages
	private static final int KILLER = 1000;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case KILLER:
				LogEx.verbose("*********** Alarm killer triggered ***********");

				stopSelf();
				break;
			}
		}
	};

	private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String ignored) {
			// The user might already be in a call when the alarm fires. When
			// we register onCallStateChanged, we get the initial in-call state
			// which kills the alarm. Check against the initial call state so
			// we don't kill the alarm during a call.
			if (state != TelephonyManager.CALL_STATE_IDLE
					&& state != mInitialCallState) {
				stopSelf();
			}
		}
	};

	@Override
	public void onCreate() {
		mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		// Listen for incoming calls to kill the alarm.
		mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		mTelephonyManager.listen(mPhoneStateListener,
				PhoneStateListener.LISTEN_CALL_STATE);
		LogEx.verbose("Aquire CPU Wakelock");
		AlarmAppWakeLock.acquireCpuWakeLock(this);
	}

	@Override
	public void onDestroy() {
		stop();
		// Stop listening for incoming calls.
		mTelephonyManager.listen(mPhoneStateListener, 0);
		AlarmAppWakeLock.releaseCpuLock();
	}

	public IBinder onBind(Intent intent) {
		return null;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		// No intent, tell the system not to restart us.
		if (intent == null) {
			stopSelf();
			return START_NOT_STICKY;
		}

		if (intent.getAction().equals(STOP_PLAYING)) {
			LogEx.verbose("AudioPlayerService failed to parse the alarm from the intent");
			stop();
			stopSelf();
			return START_NOT_STICKY;
		}

		else if (intent.getAction().equals(START_PLAYING)) {

			alarm = AlarmData.create(intent.getExtras());
			play(alarm);
			// Record the initial call state here so that the new alarm has the
			// newest state.
			mInitialCallState = mTelephonyManager.getCallState();
			return START_STICKY;
		} else {
			throw new IllegalArgumentException("The given intent action '"
					+ intent.getAction() + "' is unknown.");
		}
	}

	// Volume suggested by media team for in-call alarms.
	private static final float IN_CALL_VOLUME = 0.125f;

	private Uri getRingtone() {
		try {
			if (AlarmApp.getPreferences().contains("alarm_ringtone")) {
				return Uri.parse(AlarmApp.getPreferences().getString(
						"alarm_ringtone", null));
			}
		} catch (Exception e) {
			LogEx.exception(
					"Failed to load the ringtone from pref. Using default one",
					e);
		}
		return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
	}

	private void play(Alarm alarm) {
		// stop() checks to see if we are already playing.
		stop();

		LogEx.verbose("AudioPlayerService.play() " + alarm.getOperationId()
				+ " alert ");

		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		if (respectAndroidRingerMode()) {

			switch (am.getRingerMode()) {
			case AudioManager.RINGER_MODE_SILENT:
				LogEx.info("Device is in silent Mode. Do nothing.");
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				LogEx.info("Device is in vibration only Mode. Vibrate.");
				startVibration();
				break;
			case AudioManager.RINGER_MODE_NORMAL:
				LogEx.info("Device is in normal Mode. Ring and Vibrate");
				startVibration();
				startRinging();
				break;
			}
		} else {
			startVibration();
			startRinging();
		}

		enableKiller(alarm);
		mPlaying = true;
	}

	private boolean respectAndroidRingerMode() {
		AlarmApp.getPreferences().getBoolean("respect_ringer_mode", true);
		return false;
	}

	private void startVibration() {
		/* Start the vibrator after everything is ok with the media player */
		if (AlarmApp.getPreferences().getBoolean("alarm_vibrate", true)) {
			mVibrator.vibrate(sVibratePattern, 0);
		} else {
			mVibrator.cancel();
		}
	}

	private void startRinging() {
		Uri alert = getRingtone();
		// Fall back on the default alarm if the database does not have an
		// alarm stored.
		if (alert == null) {
			alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

			LogEx.verbose("Using default alarm: " + alert.toString());
		}

		// TODO: Reuse mMediaPlayer instead of creating a new one and/or use
		// RingtoneManager.
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnErrorListener(new OnErrorListener() {
			public boolean onError(MediaPlayer mp, int what, int extra) {
				LogEx.exception("Error occurred while playing audio.");
				mp.stop();
				mp.release();
				mMediaPlayer = null;
				return true;
			}
		});

		try {
			// Check if we are in a call. If we are, use the in-call alarm
			// resource at a low volume to not disrupt the call.
			if (mTelephonyManager.getCallState() != TelephonyManager.CALL_STATE_IDLE) {
				LogEx.verbose("Using the in-call alarm");
				mMediaPlayer.setVolume(IN_CALL_VOLUME, IN_CALL_VOLUME);

			} else {
				mMediaPlayer.setDataSource(this, alert);
			}
			startAlarm(mMediaPlayer);
		} catch (Exception ex) {
			LogEx.verbose("Using the fallback ringtone");
			// The alert may be on the sd card which could be busy right
			// now. Use the fallback ringtone.
			try {
				// Must reset the media player to clear the error state.
				mMediaPlayer.reset();

				startAlarm(mMediaPlayer);
			} catch (Exception ex2) {
				// At this point we just don't play anything.
				LogEx.exception("Failed to play fallback ringtone", ex2);
			}
		}
	}

	// Do the common stuff when starting the alarm.
	private void startAlarm(MediaPlayer player) throws java.io.IOException,
			IllegalArgumentException, IllegalStateException {
		final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// do not play alarms if stream volume is 0
		// (typically because ringer mode is silent).
		if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
			player.setAudioStreamType(AudioManager.STREAM_ALARM);
			player.setLooping(true);
			player.prepare();
			player.start();
		}
	}

	/**
	 * Stops alarm audio and disables alarm if it not snoozed and not repeating
	 */
	public void stop() {
		LogEx.verbose("AudioPlayerService.stop()");
		if (mPlaying) {
			mPlaying = false;

			// Stop audio playing
			if (mMediaPlayer != null) {
				mMediaPlayer.stop();
				mMediaPlayer.release();
				mMediaPlayer = null;
			}

			// Stop vibrator
			mVibrator.cancel();
		}
		disableKiller();
	}

	/**
	 * Kills alarm audio after ALARM_TIMEOUT_SECONDS, so the alarm won't run all
	 * day.
	 * 
	 * This just cancels the audio, but leaves the notification popped, so the
	 * user will know that the alarm tripped.
	 */
	private void enableKiller(Alarm alarm) {
		int alarmTimeoutInSec = AlarmApp.getPreferences().getInt(
				"ringtone_timeout", 5) * 60;
		mHandler.sendMessageDelayed(mHandler.obtainMessage(KILLER, alarm),
				1000 * alarmTimeoutInSec);
	}

	private void disableKiller() {
		mHandler.removeMessages(KILLER);
	}

}
