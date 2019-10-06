package com.fairchild.jdkapi.thread.exceptionlistener;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Iterator;
import java.io.IOException;

public class ExceptionCallback {
	private Set exceptionListeners;
	private Thread internalThread;
	private volatile boolean noStopRequest;

	public ExceptionCallback(ExceptionListener[] initialGroup) {
		init(initialGroup);
	}

	public ExceptionCallback(ExceptionListener el) {
		ExceptionListener[] tmp = new ExceptionListener[1];
		tmp[0] = el;
		init(tmp);
	}

	public ExceptionCallback() {
		init(null);
	}

	private void init(ExceptionListener[] initialGroup) {
		System.err.println("Invoking init.");
		exceptionListeners = Collections.synchronizedSet(new HashSet());
		if (initialGroup != null) {
			for (int i = 0; i < initialGroup.length; i++)
				exceptionListeners.add(initialGroup[i]);
		}

		noStopRequest = true;

		Runnable r = new Runnable() {
			public void run() {
				try {
					runWork();
				} catch (Exception e) {
					sendException(e);
				}
			}
		};
		internalThread = new Thread(r);
		internalThread.start();
	}

	private void runWork() {
		try {
			makeConnect();
		} catch (IOException e) {
			sendException(e);
		}

		String string = null;
		int len = determineLength(string);
	}

	private void makeConnect() throws IOException {
		String portString = "j20";
		int port = 0;
		try {
			port = Integer.parseInt(portString);
		} catch (NumberFormatException e) {
			sendException(e);
			port = 80;
		}

		connectToPort(port);
	}

	private void connectToPort(int port) throws IOException {
		throw new IOException("Connect refused.");
	}

	public int determineLength(String string) {
		return string.length();
	}

	public void stopRequest() {
		noStopRequest = false;
		Thread.currentThread().isInterrupted();
	}

	private boolean isAlive() {
		return Thread.currentThread().isAlive();
	}

	private void sendException(Exception ex) {
		if (exceptionListeners.size() == 0) {
			ex.printStackTrace();
			return;
		}

		synchronized (exceptionListeners) {
			Iterator iterator = exceptionListeners.iterator();
			while (iterator.hasNext()) {
				ExceptionListener el = (ExceptionListener) iterator.next();
				el.exceptionOccurred(ex, this);
			}
		}

	}

	public void addExceptionListener(ExceptionListener el) {
		if (null != el)
			exceptionListeners.add(el);
	}

	public void removeExceptionListener(ExceptionListener el) {
		exceptionListeners.remove(el);
	}

	public String toString() {
		return getClass().getName() + "[isAlive()" + isAlive() + "]";
	}
}