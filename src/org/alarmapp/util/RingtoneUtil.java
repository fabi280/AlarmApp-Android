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

package org.alarmapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.alarmapp.AlarmApp;
import org.alarmapp.R;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * @author frankenglert
 * 
 */
public class RingtoneUtil {
	private static final String ALARMS_DIR = "/sdcard/org.alarmapp/alarms";

	public static boolean doesAlarmDirExsist() {
		return new File(ALARMS_DIR).exists();
	}

	public static void installMotorolaBMD() {
		installRingtone(R.raw.bmd, "bmd.ogg", "Motorola BMD", false);
	}

	public static void installSwissphoneQuattro() {
		installRingtone(R.raw.quattro, "quattro.ogg", "Swissphone Quattro",
				false);
	}

	public static void installRingtone(int ressourceId, String soundFile,
			String ringtoneName, boolean isDefaultRingtone) {

		Ensure.notNull(soundFile);
		Ensure.notNull(ringtoneName);

		LogEx.verbose("Install Ringtone " + ringtoneName);

		try {
			File newSoundFile = writeToSDCard(ressourceId, soundFile);
			Uri newRingtone = installSoundFile(newSoundFile, ringtoneName);

			if (isDefaultRingtone)
				setAsDefaultRingtone(newRingtone);

		} catch (IOException e) {
			LogEx.exception("Failed to install the sound file " + soundFile, e);
		}
	}

	/**
	 * @return the uri of the newly installed ringtone
	 */
	private static Uri installSoundFile(File soundFile, String name) {
		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, soundFile.getAbsolutePath());
		values.put(MediaStore.MediaColumns.TITLE, name);
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/ogg");
		values.put(MediaStore.MediaColumns.SIZE, soundFile.length());
		values.put(MediaStore.Audio.Media.ARTIST, "AlarmApp");
		values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
		values.put(MediaStore.Audio.Media.IS_ALARM, true);
		values.put(MediaStore.Audio.Media.IS_MUSIC, false);

		Uri uri = MediaStore.Audio.Media.getContentUriForPath(soundFile
				.getAbsolutePath());
		ContentResolver mCr = AlarmApp.getInstance().getContentResolver();
		Uri contentUri = mCr.insert(uri, values);

		LogEx.verbose("Installed Ringtone uri is " + contentUri);
		return Uri.parse(soundFile.getAbsolutePath());
	}

	public static void setAsDefaultRingtone(Uri ringtone) {
		if (ringtone == null)
			return;

		Ensure.notNull(ringtone);

		LogEx.verbose("New Ringtone uri is " + ringtone);

		Editor editor = AlarmApp.getPreferences().edit();
		editor.putString("alarm_ringtone", ringtone.toString());
		editor.commit();
	}

	public static void deleteRingtoneDir() {

		PrintDir(new File("/sdcard/org.alarmapp"));

		if (deleteDirectory(new File(ALARMS_DIR)))
			LogEx.info("Directory " + ALARMS_DIR + " dropped");
		else
			LogEx.warning("Deletion of " + ALARMS_DIR + " failed");

		PrintDir(new File("/sdcard/org.alarmapp"));

	}

	private static void PrintDir(File dir) {
		LogEx.info((dir.isDirectory() ? "d" : "f") + dir.getPath());
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (files != null) {
				for (File f : files) {
					PrintDir(f);
				}
			}
		}

	}

	static public boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return path.delete();
	}

	/**
	 * returns the file descriptor of the newly installed Ringtone
	 */
	private static File writeToSDCard(int ressourceId, String fileName)
			throws IOException {
		File newSoundFile = new File(ALARMS_DIR, fileName);

		if (!doesAlarmDirExsist()) {
			if (!new File(ALARMS_DIR).mkdirs())
				throw new IOException("Failed to create the Ringtone dir");
		} else if (newSoundFile.exists()) {
			LogEx.verbose("Sound file " + fileName + " does already exist.");
			return newSoundFile;
		}

		LogEx.verbose("android.resource://org.alarmapp/" + ressourceId);

		Uri mUri = Uri.parse("android.resource://org.alarmapp/" + ressourceId);
		ContentResolver mCr = AlarmApp.getInstance().getContentResolver();
		AssetFileDescriptor soundFile;

		soundFile = mCr.openAssetFileDescriptor(mUri, "r");

		byte[] readData = new byte[1024];
		FileInputStream fis = soundFile.createInputStream();
		FileOutputStream fos = new FileOutputStream(newSoundFile);
		int i = fis.read(readData);

		while (i != -1) {
			fos.write(readData, 0, i);
			i = fis.read(readData);
		}

		fos.close();

		return newSoundFile;
	}
}
