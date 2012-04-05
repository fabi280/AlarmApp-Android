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
