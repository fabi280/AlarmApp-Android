package org.alarmapp.web;

public class FireDepartmentNotActiveException extends WebException {

	public FireDepartmentNotActiveException() {
	}

	public FireDepartmentNotActiveException(String detailMessage,
			Throwable throwable) {
		super(true, detailMessage, throwable);
	}

	public FireDepartmentNotActiveException(String detailMessage) {
		super(true, detailMessage);
	}

	public FireDepartmentNotActiveException(Throwable throwable) {
		super(true, throwable);
	}
}
