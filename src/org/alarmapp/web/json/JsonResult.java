/*
 * Copyright (C) 2011-2012 AlarmApp.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
