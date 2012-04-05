/*
 * Copyright (C) 2011-2012 AlarmApp.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.alarmapp.activities;

import java.util.List;

import org.alarmapp.AlarmApp;
import org.alarmapp.R;
import org.alarmapp.model.User;
import org.alarmapp.util.CollectionUtil;
import org.alarmapp.util.adapter.BinderAdapter;
import org.alarmapp.util.adapter.IAdapterBinder;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InformationActivity extends ListActivity {

	class Info {

		public Info(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}

		public final String key;
		public final String value;
	}

	private IAdapterBinder<Info> binder = new IAdapterBinder<InformationActivity.Info>() {

		public View getView(Info item, View row, ViewGroup parent) {
			TextView title = (TextView) row.findViewById(R.id.tvTitle);
			TextView text = (TextView) row.findViewById(R.id.tvText);

			title.setText(item.key);
			text.setText(item.value);
			return row;
		}
	};

	private List<Info> collectInformations() {
		User u = AlarmApp.getUser();
		List<Info> result = CollectionUtil.asList(new Info("Feuerwehr", u
				.getFireDepartment().getName()),
				new Info("Angemeldet als", u.getFullName()), new Info(
						"Darf Alarmstatus ansehen?",
						u.canViewAlarmStatus() ? "ja" : "nein"), new Info(
						"Zugangskennung", u.getAuthToken().GetToken()),
				new Info("Ablaufdatum der Zugangskennung", u.getAuthToken()
						.GetExpireDate().toLocaleString()));

		if (u.getFireDepartment() != null
				&& u.getFireDepartment().getPosition() != null)
			result.add(new Info("Standort Feuerwehrhaus", u.getFireDepartment()
					.getPosition().toString()));
		return result;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		BinderAdapter<Info> adapter = new BinderAdapter<InformationActivity.Info>(
				this, R.layout.list_layout_info, binder, collectInformations());
		this.getListView().setAdapter(adapter);
	}
}
