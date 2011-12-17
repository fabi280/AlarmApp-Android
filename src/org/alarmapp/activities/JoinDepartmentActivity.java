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

import org.alarmapp.AlarmApp;
import org.alarmapp.R;
import org.alarmapp.model.classes.FireDepartmentData;
import org.alarmapp.model.classes.PersonData;
import org.alarmapp.util.ActivityUtil;
import org.alarmapp.util.IntentUtil;
import org.alarmapp.util.LogEx;
import org.alarmapp.web.WebException;
import org.alarmapp.web.json.WebResult;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Frank Englert
 * 
 */
public class JoinDepartmentActivity extends Activity {

	private static final String ERROR_GET_DEPARTMENTS_FAILED = "Fehler beim Abrufen der verfügbaren Feuerwehren";
	private PersonData person;
	private AutoCompleteTextView tvDepartment;
	private TextView tvUser;
	private Button btJoin;
	private ArrayAdapter<String> adapter;
	private ProgressDialog progressDialog;

	private TextWatcher watcher = new TextWatcher() {

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String term = s.toString();
			LogEx.verbose("Text is " + term);
			if (isDepartmentLoadRequired(term))
				loadDepartments(term);
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		public void afterTextChanged(Editable s) {

		}
	};

	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View arg0) {
			LogEx.verbose("Join Button pressed.");

			progressDialog = ProgressDialog.show(JoinDepartmentActivity.this,
					"", "Benutzeraccount wird erzeugt. Bitte warten...", true);

			new Thread(new Runnable() {

				public void run() {
					try {
						WebResult result = AlarmApp.getWebClient()
								.joinFireDepartment(person,
										tvDepartment.getText().toString());

						if (result.wasSuccessful())
							joinSuccessful();
						else {
							LogEx.warning("Join failed! " + result.getMsg());
							displayError("Beitritt fehlgeschlagen!");
						}
					} catch (WebException e) {
						displayError(e.getMessage());
						LogEx.exception(e);
					} finally {
						cancelProgressDialog();
					}
				}

			}).start();

		}
	};

	private void joinSuccessful() {

		person.setFireDepartment(new FireDepartmentData(-1, tvDepartment
				.getText().toString(), 0.0f, 0.0f));

		AlarmApp.setUser(person);

		IntentUtil.displayJoinPendingActivity(JoinDepartmentActivity.this);
	}

	private void cancelProgressDialog() {
		runOnUiThread(new Runnable() {

			public void run() {
				if (progressDialog != null)
					progressDialog.cancel();
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.join_activity_layout);

		if (savedInstanceState != null) {
			person = PersonData.create(savedInstanceState);
		} else {
			person = PersonData.create(this.getIntent().getExtras());
		}

		this.tvDepartment = (AutoCompleteTextView) findViewById(R.id.tvDepartment);
		this.tvUser = (TextView) findViewById(R.id.tvUser);
		this.btJoin = (Button) findViewById(R.id.btJoin);

		this.tvDepartment.addTextChangedListener(watcher);
		this.tvUser.setText(person.getFirstName() + ", ");
		this.btJoin.setOnClickListener(this.onClickListener);

		adapter = new ArrayAdapter<String>(this,
				R.layout.join_activity_autocomplete_layout);
		this.tvDepartment.setAdapter(adapter);

		loadDepartments("");
	}

	private void loadDepartments(final String term) {
		new Thread(new Runnable() {

			public void run() {

				try {
					for (String elem : AlarmApp.getWebClient()
							.getFiredepartmentList(term))
						adapter.add(elem);

					runOnUiThread(new Runnable() {
						public void run() {
							adapter.notifyDataSetChanged();
						}

					});
				} catch (WebException e) {
					LogEx.exception("Failed to get the department list.", e);
					displayError(ERROR_GET_DEPARTMENTS_FAILED);
				}
			}

		}).start();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putAll(person.getBundle());
	}

	private boolean isDepartmentLoadRequired(String term) {
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).toLowerCase().startsWith(term.toLowerCase())) {
				LogEx.verbose("No load required.");
				return false;
			}
		}
		LogEx.verbose("No Fire Department found with the Prefix '" + term
				+ "'. Reload required");

		return true;
	}

	protected void displayError(final String text) {
		runOnUiThread(new Runnable() {
			public void run() {
				ActivityUtil
						.displayToast(JoinDepartmentActivity.this, text, 30);
			}
		});

	}
}
