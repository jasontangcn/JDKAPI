package com.fruits.jdkapi.nio.socket.timeserver;

import java.util.Date;

import com.fruits.jdkapi.nio.socket.EventAdapter;
import com.fruits.jdkapi.nio.socket.Request;

public class LogHandler extends EventAdapter {
	public LogHandler() {
	}

	public void onClosed(Request request) throws Exception {
		String log = new Date().toString() + " from " + request.getAddress().toString();
		System.out.println(log);
	}

	public void onError(String error) {
		System.out.println("Error: " + error);
	}
}
