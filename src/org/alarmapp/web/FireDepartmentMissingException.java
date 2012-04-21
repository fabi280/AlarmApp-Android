package org.alarmapp.web;

public class FireDepartmentMissingException extends WebException {

	public FireDepartmentMissingException() {
		super();
	}

	public FireDepartmentMissingException(String detailMessage,
			Throwable throwable) {
		super(true, detailMessage, throwable);
	}

	public FireDepartmentMissingException(String detailMessage) {
		super(true, detailMessage);
	}

	public FireDepartmentMissingException(Throwable throwable) {
		super(true, throwable);
	}
}
