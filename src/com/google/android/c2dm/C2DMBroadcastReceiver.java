package com.google.android.c2dm;

/*
 */

import org.alarmapp.util.LogEx;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Helper class to handle BroadcastReciver behavior. - can only run for a
 * limited amount of time - it must start a real service for longer activity -
 * must get the power lock, must make sure it's released when all done.
 * 
 */
public class C2DMBroadcastReceiver extends BroadcastReceiver {

	@Override
	public final void onReceive(Context context, Intent intent) {
		LogEx.verbose("Broadcast received");
		// To keep things in one place.
		C2DMBaseReceiver.runIntentInService(context, intent);
		setResult(Activity.RESULT_OK, null /* data */, null /* extra */);
	}
}