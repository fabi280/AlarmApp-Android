package org.alarmapp.model;

import java.util.Date;

public interface AuthToken {
	public String GetToken();

	public Date GetExpireDate();
}
