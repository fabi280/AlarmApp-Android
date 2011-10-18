package org.alarmapp.activities;

import org.alarmapp.Broadcasts;
import org.alarmapp.Controller;
import org.alarmapp.R;
import org.alarmapp.R.id;
import org.alarmapp.R.layout;
import org.alarmapp.model.User;
import org.alarmapp.util.LogEx;
import org.alarmapp.web.WebException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.c2dm.C2DMessaging;

public class LoginActivity extends Activity {

	private EditText etEmail;
	private EditText etPassword;
	private Button btLogin;
	private Button btCreateAccount;
	private ProgressBar pbLogin;
	private TextView tvLoginProgress;
	private TextView tvProgressStep;
	private User user;

	private Runnable displayProgress(final String nextStep) {
		return new Runnable() {
			public void run() {
				tvProgressStep.setText(nextStep);
				pbLogin.incrementProgressBy(1);
			}
		};
	}

	private final BroadcastReceiver pushServiceRegistered = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			runOnUiThread(displayProgress("Smartphone registrieren"));

			LogEx.info("Registration is "
					+ intent.getStringExtra("registration_id"));

			unregisterReceiver(pushServiceRegistered);
		}
	};

	private final BroadcastReceiver smartphoneCreated = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			onSuccessfulLogin();
		}
	};

	OnClickListener onLoginClickListener = new OnClickListener() {
		public void onClick(View v) {

			LogEx.verbose("Login key down");
			btLogin.setOnClickListener(null);

			final String email = LoginActivity.this.etEmail.getText()
					.toString();
			final String password = LoginActivity.this.etPassword.getText()
					.toString();

			setVisibility(View.VISIBLE);
			new Thread(new Runnable() {

				public void run() {
					try {
						user = Controller.getWebClient().login(email, password);
						Controller.setUser(LoginActivity.this, user);

						runOnUiThread(displayProgress("Push-Dienst starten"));
						Broadcasts.registerForC2DMRegisteredBroadcast(
								LoginActivity.this, pushServiceRegistered);
						Broadcasts.registerForSmartphoneCreatedBroadcast(
								LoginActivity.this, smartphoneCreated);

						LogEx.info("C2DMessaging.Register");
						C2DMessaging.register(LoginActivity.this,
								"f.englert@gmail.com");
					} catch (final WebException e) {
						LogEx.exception(
								"Der Login des Benutzers schlug fehl. ", e);

						LoginActivity.this.displayError(e.getMessage());

					} finally {
						btLogin.setOnClickListener(onLoginClickListener);
						setVisibility(View.INVISIBLE);
					}

				}
			}).start();

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
		this.pbLogin = (ProgressBar) findViewById(R.id.pbLogin);
		this.tvLoginProgress = (TextView) findViewById(R.id.tvLoginProgress);
		this.tvProgressStep = (TextView) findViewById(R.id.tvProgressStep);

		this.pbLogin.setMax(3);

		setVisibility(View.INVISIBLE);

		this.btLogin.setOnClickListener(onLoginClickListener);
	}

	private void setVisibility(final int visibility) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				LoginActivity.this.pbLogin.setVisibility(visibility);
				LoginActivity.this.tvLoginProgress.setVisibility(visibility);
				LoginActivity.this.tvProgressStep.setVisibility(visibility);
			}
		});
	}

	private void displayToastInUIThread(final String text, final int duration) {
		this.runOnUiThread(new Runnable() {

			public void run() {
				Toast toast = Toast
						.makeText(LoginActivity.this, text, duration);
				toast.show();
			}
		});
	}

	private void onSuccessfulLogin() {
		Controller.setUser(this, user);
		displayToastInUIThread("Hallo " + user.GetFirstName(), 10);
	}

	private void displayError(String errorMessage) {
		displayToastInUIThread(errorMessage, 15);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogEx.info("Key Down!");
		return super.onKeyDown(keyCode, event);
	}
}
