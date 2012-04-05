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

package org.alarmapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class Store<T> {

	private File file;

	public Store(Context ctxt, String fileName) {
		this.file = new File(ctxt.getFilesDir(), fileName);
	}

	public T read() {
		FileInputStream in;
		try {
			in = new FileInputStream(this.file);
			ObjectInputStream inStream = new ObjectInputStream(in);

			@SuppressWarnings("unchecked")
			T result = (T) inStream.readObject();

			inStream.close();
			return result;

		} catch (ClassNotFoundException e) {
			LogEx.exception(e);
			return null;
		} catch (FileNotFoundException e1) {
			LogEx.warning("File " + this.file.getName() + " does not exist.");
		} catch (IOException e) {
			LogEx.exception(e);
		} catch (Exception ex) {
			LogEx.exception(ex);
		}
		return null;
	}

	public void write(T instance) {
		FileOutputStream out;
		try {
			out = new FileOutputStream(this.file);
			ObjectOutputStream outStream = new ObjectOutputStream(out);
			outStream.writeObject(instance);
			out.close();
		} catch (FileNotFoundException e) {
			LogEx.exception(e);
		} catch (IOException e) {
			LogEx.exception(e);
		} catch (Exception ex) {
			LogEx.exception(ex);
		}
	}
}
