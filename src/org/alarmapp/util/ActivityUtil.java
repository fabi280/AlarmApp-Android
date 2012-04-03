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

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class ActivityUtil {
	public static void displayToast(final Activity activity, final String text,
			final int duration) {
		activity.runOnUiThread(new Runnable() {

			public void run() {
				Toast toast = Toast.makeText(activity, text, duration);
				toast.show();

			}
		});
	}

	private static ProgressDialog pDialog;

	public static void startProgressBar(final Activity activity) {
		activity.runOnUiThread(new Runnable() {

			public void run() {
				// pBar = ProgressBar.makeText(activity, text, duration);
				pDialog = new ProgressDialog(activity);
				pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pDialog.setMessage("Laden");
				pDialog.setCancelable(false);
				pDialog.show();
			}
		});
	}

	public static void stopProgressBar() {
		pDialog.dismiss();
	}

	public static void makeVisible(Activity activity) {
		Window w = activity.getWindow(); // in Activity's onCreate() for
											// instance
		int flags = /*
					 * WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
					 */WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
		w.setFlags(flags, flags);
	}
}
