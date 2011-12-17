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
import org.alarmapp.model.classes.PersonData;
import org.alarmapp.util.ActivityUtil;
import org.alarmapp.util.IntentUtil;
import org.alarmapp.util.LogEx;
import org.alarmapp.util.checker.AsyncEditorActionChecker;
import org.alarmapp.util.checker.CheckerUtil;
import org.alarmapp.util.checker.EditorActionChecker;
import org.alarmapp.util.checker.TextViewChecker;
import org.alarmapp.web.WebClient;
import org.alarmapp.web.WebException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author frank
 * 
 */
public class AccountCreateActivity extends Activity {

	private static final String PASSWPRD_MATCH_ERROR = "Die Passworte stimmen nicht überein.";
	private static final String CREATE_USER_FAILED_ERROR = "Das Anlegen eines neuen Benutzers ist gescheitert.";

	EditText etFirstName;
	EditText etLastName;
	EditText etUserName;
	EditText etEmail;
	EditText etPassword;
	EditText etPassword2;
	Button btAccountCreate;
	WebClient httpWebClient;
	ProgressDialog progressDialog;

	OnClickListener accountCreateClick = new OnClickListener() {
		public void onClick(View v) {
			LogEx.info("Creating User Account");

			if (!allEditTextsValid()) {
				displayError("Bitte korrigieren Sie Ihre Eingaben!");
			}
			progressDialog = ProgressDialog.show(AccountCreateActivity.this,
					"", "Benutzeraccount wird erzeugt. Bitte warten...", true);
			new Thread(createUserRunnable).start();
		}

	};

	private boolean allEditTextsValid() {
		return etFirstName.getError() == null && etEmail.getError() == null
				&& etLastName.getError() == null
				&& etPassword.getError() == null
				&& etPassword2.getError() == null
				&& etUserName.getError() == null;
	}

	private Runnable createUserRunnable = new Runnable() {

		public void run() {
			String username = etUserName.getText().toString();
			String firstName = etFirstName.getText().toString();
			String lastName = etLastName.getText().toString();
			String email = etEmail.getText().toString();
			String password = etPassword.getText().toString();
			String passwordConfirmation = etPassword2.getText().toString();

			try {
				// final PersonData p = AlarmApp.getWebClient().createUser(
				// username, firstName, lastName, email, password,
				// passwordConfirmation);
				throw new WebException("fail");
				// runOnUiThread(new Runnable() {
				//
				// public void run() {
				// userCreateSuccessful(p);
				// }
				// });
			} catch (WebException e) {
				LogEx.exception("Creating a new User failed!", e);
				progressDialog.cancel();
				displayError(CREATE_USER_FAILED_ERROR + e.getMessage());
			}
		}

	};

	private void userCreateSuccessful(PersonData value) {
		LogEx.info("User " + value.getFullName() + " created. Id is "
				+ value.getId());
		progressDialog.cancel();

		AlarmApp.setUser(value);

		IntentUtil.displayJoinDepartmentActivity(this, value);
	}

	/**
	 * @param string
	 */
	protected void displayError(String string) {
		ActivityUtil.displayToast(this, string, 30);

	}

	TextViewChecker passwordConfirmedChecker = new TextViewChecker() {
		public boolean isValid(TextView v) {
			return etPassword.getText().toString()
					.equals(etPassword2.getText().toString());
		}

		public String getFormatDescription() {
			return PASSWPRD_MATCH_ERROR;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.account_create);

		this.etEmail = (EditText) findViewById(R.id.etEmail);
		this.etFirstName = (EditText) findViewById(R.id.etFirstName);
		this.etLastName = (EditText) findViewById(R.id.etLastName);
		this.etUserName = (EditText) findViewById(R.id.etName);
		this.etPassword = (EditText) findViewById(R.id.etPassword);
		this.etPassword2 = (EditText) findViewById(R.id.etPassword2);

		this.btAccountCreate = (Button) findViewById(R.id.btAccountCreate);

		this.etPassword2.setImeOptions(EditorInfo.IME_ACTION_DONE);

		btAccountCreate.setOnClickListener(accountCreateClick);

		this.etUserName.setOnEditorActionListener(new AsyncEditorActionChecker(
				this, CheckerUtil.UsernameValidChecker,
				CheckerUtil.UsernameUniqueChecker));
		this.etPassword
				.setOnEditorActionListener(CheckerUtil.PasswortEditActionChecker);
		this.etPassword2.setOnEditorActionListener(new EditorActionChecker(
				passwordConfirmedChecker));
		this.etLastName
				.setOnEditorActionListener(CheckerUtil.LastNameEditActionChecker);
		this.etFirstName
				.setOnEditorActionListener(CheckerUtil.FirstNameEditActionChecker);
		this.etEmail.setOnEditorActionListener(new AsyncEditorActionChecker(
				this, CheckerUtil.EmailUniqueChecker));
	}
}
