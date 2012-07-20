/*
 * Copyright (C) 2011-2012 AlarmApp.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.alarmapp;

import org.acra.ErrorReporter;
import org.acra.annotation.ReportsCrashes;
import org.alarmapp.model.AlarmStore;
import org.alarmapp.model.User;
import org.alarmapp.model.classes.AnonymusUserData;
import org.alarmapp.model.classes.PersistentAlarmStore;
import org.alarmapp.util.Ensure;
import org.alarmapp.util.LogEx;
import org.alarmapp.util.Store;
import org.alarmapp.web.AuthHttpClient;
import org.alarmapp.web.AuthWebClient;
import org.alarmapp.web.HttpWebClient;
import org.alarmapp.web.WebClient;

import roboguice.RoboGuice;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.preference.PreferenceManager;

import com.google.inject.util.Modules;

//dFRiRHVCbGVxVzNva0NkdHB0NVN5Q0E6MQ
@ReportsCrashes(formKey = "dFRiRHVCbGVxVzNva0NkdHB0NVN5Q0E6MQ")
public class AlarmApp extends Application {

	private static AlarmApp instance;

	private static AuthWebClient webClient = null;
	private static WebClient client = null;

	/***
	 * @throws IllegalStateException
	 *             if the user did not log in yet
	 * @return A web client for accessing the alarm web service
	 */
	public static AuthWebClient getAuthWebClient() {
		Ensure.notNull(instance);

		if (getUser() == null)
			throw new IllegalStateException(
					"Kein Benutzer verfügbar. Sie müssen sich erst einloggen!");

		if (webClient == null)
			webClient = new AuthHttpClient(getWebClient(), getUser()
					.getAuthToken());
		return webClient;
	}

	/**
	 * 
	 * @return Instance of the WebClient to access the web service
	 */
	public static WebClient getWebClient() {
		if (client == null)
			client = new HttpWebClient();
		return client;
	}

	private static User user = null;

	/**
	 * 
	 * @return The user. Maybe a AnonymusUser if the user has not logged in yet.
	 */
	public static User getUser() {
		Ensure.notNull(instance);

		if (user == null) {
			Store<User> userStore = new Store<User>(instance, "USER");
			user = userStore.read();
			LogEx.verbose("Loaded the user " + user + " from local storage");

			if (user != null && user.isLoggedIn()) {

				ErrorReporter.getInstance().putCustomData("UserId",
						user.getId());
				ErrorReporter.getInstance().putCustomData("User Name",
						user.getFullName());

				if (user.hasDepartment())
					ErrorReporter.getInstance().putCustomData(
							"Fire Department",
							user.getFireDepartment().getName());
			}
		}

		if (user == null)
			return new AnonymusUserData();

		return user;
	}

	public static boolean isDebuggable() {
		boolean isDebuggable = (0 != (instance.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));

		return isDebuggable;
	}

	public static void setUser(User user) {
		Ensure.notNull(user);

		AlarmApp.user = user;
		Store<User> userStore = new Store<User>(instance, "USER");
		userStore.write(user);

		LogEx.info("Wrote user " + user + " to local storage");
	}

	private static AlarmStore alarmStore = null;

	public static AlarmStore getAlarmStore() {
		Ensure.notNull(instance);

		if (alarmStore == null)
			alarmStore = new PersistentAlarmStore(instance);

		return alarmStore;
	}

	public static SharedPreferences getPreferences() {
		Ensure.notNull(instance);
		return PreferenceManager.getDefaultSharedPreferences(instance);
	}

	public static AlarmApp getInstance() {
		Ensure.notNull(instance);
		return instance;
	}

	@Override
	public void onCreate() {
		// TODO: Comment in acra again
		// ACRA.init(this);
		super.onCreate();

		instance = this;

		RoboGuice.setBaseApplicationInjector(
				(Application) AlarmApp.getInstance(),
				RoboGuice.DEFAULT_STAGE,
				Modules.override(
						RoboGuice.newDefaultRoboModule(AlarmApp.getInstance()))
						.with(new AlarmAppModule()));

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		if (isDebuggable()) {
			LogEx.info("Running in Debug mode");
		}
	}
}
