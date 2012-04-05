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

package org.alarmapp.util.checker;

public class CheckerUtil {
	public final static TextViewChecker UsernameValidChecker = new RegexTextviewChecker(
			"([\\w.+-_@]){1,30}",
			"Der Benutzername darf aus Buchstaben, +, -, @, . und _ bestehen und muss zwischen 2 und 30 Zeichen lang sein");

	public final static TextViewChecker PasswordChecker = new RegexTextviewChecker(
			".{6,30}", "Das Passwort muss zwischen 6 und 30 Zeichen lang sein.");

	public final static TextViewChecker FirstNameChecker = new RegexTextviewChecker(
			"\\w{2,30}",
			"Der Vorname muss zwischen 2 und 30 Zeichen lang sein.");

	public final static TextViewChecker LastNameChecker = new RegexTextviewChecker(
			"\\w{2,30}",
			"Der Nachname muss zwischen 2 und 30 Zeichen lang sein.");

	public final static TextViewChecker EmailUniqueChecker = new EmailUniqueTextViewChecker();

	// public final static EditorActionChecker UsernameEditActionChecker = new
	// EditorActionChecker(
	// UsernameChecker);

	public final static EditorActionChecker PasswortEditActionChecker = new EditorActionChecker(
			PasswordChecker);

	public final static EditorActionChecker FirstNameEditActionChecker = new EditorActionChecker(
			FirstNameChecker);

	public final static EditorActionChecker LastNameEditActionChecker = new EditorActionChecker(
			LastNameChecker);

	public final static UsernameUniqueTextViewChecker UsernameUniqueChecker = new UsernameUniqueTextViewChecker();
}
