package org.alarmapp;

import org.alarmapp.model.User;
import org.alarmapp.util.LogEx;
import org.alarmapp.web.WebException;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText etEmail;
	private EditText etPassword;
	private Button btLogin;
	private Button btCreateAccount;

	OnClickListener onLoginClickListener = new OnClickListener() {
		public void onClick(View v) {
			try {
				LogEx.verbose("Login key down");
				btLogin.setOnClickListener(null);

				String email = LoginActivity.this.etEmail.getText().toString();
				String password = LoginActivity.this.etPassword.getText()
						.toString();

				User user = Controller.getWebClient().login(email, password);
				onSuccessfulLogin(user);
			} catch (WebException e) {
				LogEx.exception("Der Login des Benutzers schlug fehl. ", e);
				LoginActivity.this.displayError(e.getMessage());
			} finally {
				btLogin.setOnClickListener(onLoginClickListener);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.login);

		this.btLogin = (Button) findViewById(R.id.btLogin);
		this.btCreateAccount = (Button) findViewById(R.id.btCreateAccount);
		this.etEmail = (EditText) findViewById(R.id.etEmail);
		this.etPassword = (EditText) findViewById(R.id.etPassword);

		LogEx.verbose("Set onLoginClick listener");
		this.btLogin.setOnClickListener(onLoginClickListener);
	}

	private void onSuccessfulLogin(User user) {
		Toast toast = Toast.makeText(this, "Hallo " + user.GetFirstName(), 5);
		toast.show();
	}

	private void displayError(String errorMessage) {
		Toast toast = Toast.makeText(this, errorMessage, 15);
		toast.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogEx.info("Key Down!");
		return super.onKeyDown(keyCode, event);
	}
}
