package com.fruits.jdkapi.nio.socket.timeserver;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import com.fruits.jdkapi.nio.socket.EventAdapter;
import com.fruits.jdkapi.nio.socket.Request;
import com.fruits.jdkapi.nio.socket.Response;

public class TimeHandler extends EventAdapter {
	public TimeHandler() {
	}

	public void onWrite(Request request, Response response) throws Exception {
		String command = new String(request.getData());
		String time = null;
		Date date = new Date();

		if (command.equals("GB")) {
			DateFormat cnDate = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.CHINA);
			time = cnDate.format(date);
		} else {
			DateFormat enDate = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.US);
			time = enDate.format(date);
		}

		response.send(time.getBytes());
	}
}
