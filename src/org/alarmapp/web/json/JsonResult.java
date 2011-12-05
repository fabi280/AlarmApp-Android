package org.alarmapp.web.json;

import org.alarmapp.web.WebResult;

public class JsonResult<T> implements WebResult<T> {

	private boolean wasSuccessful;
	private T value;
	private String message;
	private String tag;

	public JsonResult(T value) {
		this.wasSuccessful = true;
		this.value = value;
	}

	public JsonResult(String tag, String message) {
		this.wasSuccessful = false;
		this.value = null;
		this.message = message;
		this.tag = tag;
	}

	public boolean wasSuccessful() {
		return wasSuccessful;
	}

	public String getErrorMesssage() {
		return message;
	}

	public String getErrorTag() {
		return tag;
	}

	public T getValue() {
		if (wasSuccessful)
			return value;

		throw new RuntimeException(
				"The Operation was not successful. Consider checking wasSuccessful before getting the result.");
	}

}
