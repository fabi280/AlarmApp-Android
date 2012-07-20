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

package org.alarmapp;

import org.alarmapp.behaviour.IPushMessageHandler;
import org.alarmapp.behaviour.binding.Alarm;
import org.alarmapp.behaviour.binding.AlarmStatus;
import org.alarmapp.behaviour.implementation.AlarmPushMessageHandler;
import org.alarmapp.behaviour.implementation.AlarmStatusPushMessageHandler;

import com.google.inject.AbstractModule;

/**
 * @author Frank Englert
 * 
 *         The GUICE Configuration for this project.
 */
public class AlarmAppModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IPushMessageHandler.class).annotatedWith(Alarm.class).to(
				AlarmPushMessageHandler.class);
		bind(IPushMessageHandler.class).annotatedWith(AlarmStatus.class).to(
				AlarmStatusPushMessageHandler.class);
	}

}
