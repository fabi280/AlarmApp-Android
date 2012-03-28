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

package org.alarmapp.activities;

import org.alarmapp.R;
import org.alarmapp.activities.account_create.AccountCreateWebViewClient;
import org.alarmapp.util.ActivityUtil;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * @author frank
 * 
 */
public class AccountCreateActivity extends Activity {

	private WebView webView;

	protected void setError(final TextView tv, final String msg) {
		runOnUiThread(new Runnable() {

			public void run() {
				tv.setError(msg);

			}
		});
	}

	protected void displayError(final String string) {
		runOnUiThread(new Runnable() {
			public void run() {
				ActivityUtil.displayToast(AccountCreateActivity.this, string,
						30);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.account_create);

		webView = (WebView) findViewById(R.id.wvCreateAccount);
		webView.getSettings().setJavaScriptEnabled(true);

		webView.setWebViewClient(new AccountCreateWebViewClient());
		webView.loadUrl("http://alarmnotificationservice.appspot.com/mobile/register");

	}
}
