package org.alarmapp.util;

import android.app.Activity;
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
}
