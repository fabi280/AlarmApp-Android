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

import android.app.Activity;
import android.view.KeyEvent;
import android.widget.TextView;

/**
 * @author frank
 * 
 */
public class AsyncEditorActionChecker extends EditorActionChecker {

	private final Activity context;

	public AsyncEditorActionChecker(Activity context,
			TextViewChecker... checkers) {
		super(checkers);
		this.context = context;
	}

	public boolean onEditorAction(final TextView textView, final int actionId,
			final KeyEvent event) {
		if (checkRequired(actionId)) {

			new Thread(new Runnable() {
				public void run() {
					performCheck(textView);
				}
			}).start();

		}
		return false;
	}

	protected void setError(final TextView view, final String msg) {
		context.runOnUiThread(new Runnable() {
			public void run() {
				view.setError(msg);
			}
		});
	}
}
