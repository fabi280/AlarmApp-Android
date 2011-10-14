package org.alarmapp;

import org.alarmapp.model.User;
import org.alarmapp.web.HttpWebClient;
import org.alarmapp.web.WebClient;

public class Controller {

	public static User getUser() {
		return null;
	}

	private static WebClient webClient = null;

	public static WebClient getWebClient() {
		if (webClient == null)
			webClient = new HttpWebClient();

		return webClient;
	}
}
