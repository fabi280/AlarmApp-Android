package org.alarmapp.web;

public class WebException extends Exception {

	private static final long serialVersionUID = 1L;
	private boolean isPermanentFailure = true;

	public WebException() {
		super();
	}

	public WebException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public WebException(String detailMessage) {
		super(detailMessage);
	}

	public WebException(Throwable throwable) {
		super(throwable);
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
