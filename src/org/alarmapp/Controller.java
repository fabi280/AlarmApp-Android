package org.alarmapp;

import org.alarmapp.model.User;
import org.alarmapp.util.LogEx;
import org.alarmapp.util.Store;
import org.alarmapp.web.HttpWebClient;
import org.alarmapp.web.WebClient;

import android.content.Context;

public class Controller {

	private static User user = null;

	public static User getUser(Context ctxt) {
		if (user == null) {
			Store<User> userStore = new Store<User>(ctxt, "USER");
			user = userStore.read();
		}
		return user;
	}

	public static void setUser(Context ctxt, User user) {
		Controller.user = user;
		Store<User> userStore = new Store<User>(ctxt, "USER");
		userStore.write(user);

		LogEx.info("User is " + user);

	}

	private static WebClient webClient = null;

	public static WebClient getWebClient() {
		if (webClient == null)
			webClient = new HttpWebClient();

		return webClient;
	}
}
