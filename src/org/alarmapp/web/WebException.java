package org.alarmapp.web;

public class WebException extends Exception {

	private boolean isPermanentFailure = true;

	public WebException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WebException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	public WebException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	public WebException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

	public WebException(boolean isPermanentFailure, String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
		this.isPermanentFailure = isPermanentFailure;
	}

	public WebException(boolean isPermanentFailure, String detailMessage) {
		super(detailMessage);
		this.isPermanentFailure = isPermanentFailure;
	}

	public WebException(boolean isPermanentFailure, Throwable throwable) {
		super(throwable);
		this.isPermanentFailure = isPermanentFailure;
	}

	public boolean isPermanentFailure() {
		return isPermanentFailure;
	}
}
