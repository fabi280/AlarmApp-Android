package org.alarmapp.util;

import android.app.Activity;
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
