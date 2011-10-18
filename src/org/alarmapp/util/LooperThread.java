package org.alarmapp.util;

import java.util.concurrent.CountDownLatch;

import android.os.Handler;
import android.os.Looper;

public class LooperThread extends Thread {
	public Handler handler;

	CountDownLatch latch = new CountDownLatch(1);

	@Override
	public synchronized void start() {
		super.start();
		try {
			latch.await();
		} catch (InterruptedException e) {
			LogEx.exception("Interrupted while starting the Looper Thread.", e);
		}
	}

	public void run() {
		try {
			// preparing a looper on current thread
			// the current thread is being detected implicitly
			Looper.prepare();

			// now, the handler will automatically bind to the
			// Looper that is attached to the current thread
			// You don't need to specify the Looper explicitly
			handler = new Handler();
			latch.countDown();

			// After the following line the thread will start
			// running the message loop and will not normally
			// exit the loop unless a problem happens or you
			// quit() the looper (see below)
			Looper.loop();
		} catch (Throwable t) {
			LogEx.exception("halted due to an error", t);
		}
	}

	public void addWorkItem(Runnable runable) {
		this.handler.post(runable);
	}
}
