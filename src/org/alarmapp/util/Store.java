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
			LogEx.exception(e1);
		} catch (IOException e) {
			LogEx.exception(e);
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
		}
	}
}
