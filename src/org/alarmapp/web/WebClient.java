package org.alarmapp.web;

import org.alarmapp.model.AuthToken;
import org.alarmapp.model.User;

public interface WebClient {
	public User login(String name, String password) throws WebException;

	public boolean checkAuthentication(AuthToken token) throws WebException;
}
