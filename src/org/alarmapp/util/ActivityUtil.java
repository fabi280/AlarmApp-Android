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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

	private static AlertDialog aDialog;
	private static AlertDialog.Builder aDialogBuilder;

	public static void showAlertDialog(final Activity activity,
			final String titel, final String text) {
		activity.runOnUiThread(new Runnable() {

			public void run() {
				aDialogBuilder = new AlertDialog.Builder(activity);
				aDialogBuilder.setMessage(text);
				aDialogBuilder.setTitle(titel);
				aDialogBuilder.setCancelable(false);
				aDialogBuilder.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();

							}
						});
				aDialog = aDialogBuilder.create();
				aDialog.show();
			}
		});

	}

	private static ProgressDialog pDialog;

	public static ProgressDialog startProgressBar(final Activity activity) {
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

		return pDialog;
	}

	public static void stopProgressBar(ProgressDialog dialog) {
		dialog.dismiss();
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
