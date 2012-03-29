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
