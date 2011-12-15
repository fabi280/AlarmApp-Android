package org.alarmapp.model;

import java.io.Serializable;

public interface User extends Serializable, Person {

	public String getFirstName();

	public String getLastName();

	public String getFullName();

	/***
	 * @throws UnsupportedOperationException
	 *             if hasAccount() is false
	 */
	public FireDepartment getFireDepartment();

	/***
	 * @throws UnsupportedOperationException
	 *             if hasAccount() is false
	 */
	public AuthToken getAuthToken();

	/***
	 * @throws UnsupportedOperationException
	 *             if hasAccount() is false
	 */
	public void setAuthToken(AuthToken token);

	/***
	 * @throws UnsupportedOperationException
	 *             if hasAccount() is false
	 */
	public String getId();

	public boolean canViewAlarmStatus();

	public boolean hasDepartment();

	public boolean hasAuthToken();

	public boolean isLoggedIn();

	public boolean hasAccount();
}
