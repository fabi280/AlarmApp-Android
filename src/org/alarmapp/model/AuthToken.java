package org.alarmapp.model;

import java.io.Serializable;
import java.util.Date;

public interface AuthToken extends Serializable {
	public String GetToken();

	public Date GetExpireDate();
}
