/*
 * Copyright (C) 2011 AlarmApp.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package org.alarmapp.util.checker;

import android.widget.TextView;

public class RegexTextviewChecker implements TextViewChecker {

	private final String regex;
	private final String formatDescr;

	public RegexTextviewChecker(String regex, String formatDescr) {
		super();
		this.regex = regex;
		this.formatDescr = formatDescr;
	}

	public boolean isValid(TextView v) {
		return v.getText().toString().matches(regex);
	}

	public String getFormatDescription() {
		return formatDescr;
	}

}
