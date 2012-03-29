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

package org.alarmapp.activities.account_create;

import org.alarmapp.AlarmApp;
import org.alarmapp.util.ActivityUtil;
import org.alarmapp.util.IntentUtil;

import android.webkit.WebView;

public class AccountCreateWebViewClient extends android.webkit.WebViewClient {

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (isRegistrationDone(url)) {
			IntentUtil.displayLoginActivity(AlarmApp.getInstance());
		}
		view.loadUrl(url);
		return true;
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		ActivityUtil.stopProgressBar();
	}

	private boolean isRegistrationDone(String url) {
		return url.endsWith("/register/done");
	}
}
