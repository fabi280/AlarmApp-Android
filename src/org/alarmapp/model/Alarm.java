package org.alarmapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import android.os.Bundle;

/**
 * Implementors of this interface should also implement a static function Alarm
 * Create(Bundle b)
 * 
 * @author frank
 * 
 */
public interface Alarm extends Serializable, Bundable {
	public String getTitle();

	public String getText();

	public String getOperationId();

	public Date getAlarmed();

	public Map<String, String> getAdditionalValues();

	public boolean isFinal();

	public boolean isAlarmStatusViewer();

	public AlarmState getState();

	public void setState(AlarmState state);

	public Set<AlarmedUser> getAlarmedUsers();

	public void setAlarmedUsers(Set<AlarmedUser> users);

	public void updateAlarmedUser(AlarmedUser user);

	public void save();

	public Bundle getBundle();
}
