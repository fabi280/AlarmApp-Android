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

import org.alarmapp.util.LogEx;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * @author frank
 * 
 */
public class EditorActionChecker implements OnEditorActionListener {

	private final TextViewChecker[] checker;

	public EditorActionChecker(TextViewChecker... checker) {
		super();
		this.checker = checker;
	}

	public boolean onEditorAction(TextView textView, int actionId,
			KeyEvent event) {

		if (checkRequired(actionId)) {
			performCheck(textView);
		}
		return false;
	}

	protected boolean checkRequired(int actionId) {
		return actionId == EditorInfo.IME_ACTION_NEXT
				|| actionId == EditorInfo.IME_ACTION_DONE;
	}

	protected void performCheck(TextView textView) {
		LogEx.verbose("check");

		for (TextViewChecker check : checker) {
			if (check.isValid(textView)) {
				setError(textView, null);
			} else {
				setError(textView, check.getFormatDescription());
			}
		}
	}

	protected void setError(TextView textView, String error) {
		textView.setError(error);
	}
}
