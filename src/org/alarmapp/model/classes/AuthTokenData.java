package org.alarmapp.model.classes;

import java.util.Date;

import org.alarmapp.model.AuthToken;

public class AuthTokenData implements AuthToken {

	public AuthTokenData(String token, Date expireDate) {
		super();
		this.token = token;
		this.expireDate = expireDate;
	}

	private String token;
	private Date expireDate;

	public String GetToken() {
		return this.token;
	}

	public Date GetExpireDate() {
		return this.expireDate;
	}

	@Override
	public String toString() {
		return this.token + " expires " + this.expireDate;
	}

}
