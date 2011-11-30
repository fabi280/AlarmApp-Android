package org.alarmapp.web.json;

import org.alarmapp.web.WebResult;

public class JsonResult<T> implements WebResult<T> {

	private boolean wasSuccessful;
	private T value;

	public JsonResult(boolean wasSuccessful, T value) {
		this.wasSuccessful = wasSuccessful;
		this.value = value;
	}

	public boolean wasSuccessful() {
		return wasSuccessful;
	}

	public T getValue() {
		if (wasSuccessful)
			return value;

		throw new RuntimeException(
				"The Operation was not successful. Consider checking wasSuccessful before getting the result.");
	}

}
