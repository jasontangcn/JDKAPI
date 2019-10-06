package com.fruits.jdkapi.nio.socket;

import java.util.ArrayList;

public class Notifier {
	private ArrayList<ServerListener> listeners = new ArrayList<ServerListener>();
	private static Notifier instance = null;

	private Notifier() {
	}

	public static synchronized Notifier getNotifier() {
		if (null == instance) {
			instance = new Notifier();
			return instance;
		} else
			return instance;
	}

	public void addListener(ServerListener listener) {
		synchronized (listeners) {
			if (!listeners.contains(listener))
				listeners.add(listener);
		}
	}

	public void fireOnError(String error) {
		for (int i = (listeners.size() - 1); i >= 0; i--)
			((ServerListener) listeners.get(i)).onError(error);
	}

	public void fireOnAccept() throws Exception {
		for (int i = (listeners.size() - 1); i >= 0; i--)
			((ServerListener) listeners.get(i)).onAccept();
	}
	
	public void fireOnAccepted(Request request) throws Exception {
		for (int i = (listeners.size() - 1); i >= 0; i--)
			((ServerListener) listeners.get(i)).onAccepted(request);
	}

	public void fireOnRead(Request request) throws Exception {
		for (int i = (listeners.size() - 1); i >= 0; i--)
			((ServerListener) listeners.get(i)).onRead(request);
	}

	public void fireOnWrite(Request request, Response response) throws Exception {
		for (int i = (listeners.size() - 1); i >= 0; i--)
			((ServerListener) listeners.get(i)).onWrite(request, response);
	}

	public void fireOnClosed(Request request) throws Exception {
		for (int i = (listeners.size() - 1); i >= 0; i--)
			((ServerListener) listeners.get(i)).onClosed(request);
	}
}
