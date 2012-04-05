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

package org.alarmapp.model.classes;

import java.util.Date;

import org.alarmapp.model.AuthToken;

public class AuthTokenData implements AuthToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AuthTokenData(String token, Date expireDate) {
		super();
		this.token = token;
		this.expireDate = expireDate;
	}

	private String token;
	private Date expireDate;

	public String GetToken() {
		return this.token;
	}

	public Date GetExpireDate() {
		return this.expireDate;
	}

	@Override
	public String toString() {
		return this.token + " expires " + this.expireDate;
	}

}
