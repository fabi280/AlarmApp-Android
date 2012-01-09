package org.alarmapp.activities;

import java.util.ArrayList;
import java.util.List;

import org.alarmapp.AlarmApp;
import org.alarmapp.R;
import org.alarmapp.util.IntentUtil;
import org.alarmapp.util.LogEx;
import org.alarmapp.util.RingtoneUtil;
import org.alarmapp.util.adapter.BinderAdapter;
import org.alarmapp.util.adapter.IAdapterBinder;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.c2dm.C2DMessaging;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */

	private class MenuEntry {
		public static final int COLOR_GREEN = 0;
		public static final int COLOR_RED = 1;
		public static final int COLOR_BLUE = 2;
		public static final int COLOR_YELLOW = 3;

		public MenuEntry(String title, String text, int color,
				Runnable clickHandler) {
			this.Title = title;
			this.Text = text;
			this.Color = color;
			this.ClickHandler = clickHandler;
		}

		public final int Color;
		public final String Title;
		public final String Text;
		public final Runnable ClickHandler;
	}

	private IAdapterBinder<MenuEntry> binder = new IAdapterBinder<MainActivity.MenuEntry>() {

		public View getView(MenuEntry item, View row, ViewGroup parent) {
			TextView title = (TextView) row.findViewById(R.id.tvTitle);
			TextView text = (TextView) row.findViewById(R.id.tvSubtitle);
			ImageView color = (ImageView) row.findViewById(R.id.ivStatus);

			title.setText(item.Title);
			text.setText(item.Text);
			color.setImageLevel(item.Color);
			return row;
		}
	};

	private ListView lvMainItems;

	private OnItemClickListener itemClick = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
			MenuEntry m = (MenuEntry) arg0.getItemAtPosition(pos);
			if (m != null && m.ClickHandler != null) {
				m.ClickHandler.run();
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.log_out:
			new Thread(logoutClick).start();
			IntentUtil.displayLoginActivity(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private Runnable feedbackClick = new Runnable() {

		public void run() {
			IntentUtil.sendFeedbackEmailIntent(MainActivity.this);
		}
	};

	private Runnable infoClick = new Runnable() {

		public void run() {
			IntentUtil.displayInformationsActivity(MainActivity.this);

		}
	};

	private Runnable prefsClick = new Runnable() {

		public void run() {
			IntentUtil.displayPreferencesActivity(MainActivity.this);

		}
	};

	private Runnable alarmListClick = new Runnable() {

		public void run() {
			IntentUtil.displayAlarmListActivity(MainActivity.this);
		}
	};

	private Runnable logoutClick = new Runnable() {

		public void run() {
			C2DMessaging.unregister(MainActivity.this);
		}
	};

	private Runnable deleteRingtoneDir = new Runnable() {

		public void run() {
			RingtoneUtil.deleteRingtoneDir();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		LogEx.verbose("MainActivity");

		if (!isUserAvailable()) {
			IntentUtil.displayLoginActivity(this);
			return;
		}

		LogEx.verbose("Ringtone is "
				+ AlarmApp.getPreferences().getString("alarm_ringtone", null));

		if (!RingtoneUtil.doesAlarmDirExsist()) {

			LogEx.verbose("Install new ring tones");
			RingtoneUtil.installSwissphoneQuattro();
			RingtoneUtil.installMotorolaBMD();
		} else {
			LogEx.verbose("Ringtones already installed!");
		}

		this.lvMainItems = (ListView) findViewById(R.id.lvMainItems);
		this.lvMainItems.setOnItemClickListener(itemClick);
		displayMenu();

	}

	private void displayMenu() {
		List<MenuEntry> entries = new ArrayList<MainActivity.MenuEntry>();
		entries.add(new MenuEntry("Letzte Einsätze",
				"Zeigt Ihnen eine Liste mit den letzten Einsätzen an.",
				MenuEntry.COLOR_BLUE, alarmListClick));
		entries.add(new MenuEntry("Informationen",
				"Zeigt Infos zu Ihrere Feuerwehr und Ihrem Benutzeraccount ",
				MenuEntry.COLOR_GREEN, infoClick));
		entries.add(new MenuEntry("Einstellungen",
				"Einstellungen der AlarmApp anzeigen und ändern.",
				MenuEntry.COLOR_YELLOW, prefsClick));
		entries.add(new MenuEntry("Feedback",
				"Verbesserungsvorschläge oder Fehler melden.",
				MenuEntry.COLOR_RED, feedbackClick));

		// entries.add(new MenuEntry("Lösche", "Alarmtonverzeichnis",
		// MenuEntry.COLOR_BLUE, deleteRingtoneDir));
		// entries.add(new MenuEntry("Stopp", "Alarmton", MenuEntry.COLOR_BLUE,
		// stopSoundClick));

		this.lvMainItems.setAdapter(new BinderAdapter<MenuEntry>(this,
				R.layout.list_layout_main, binder, entries));
	}

	private boolean isUserAvailable() {
		return AlarmApp.getUser().isLoggedIn();
	}
}