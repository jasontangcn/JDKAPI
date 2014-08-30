package com.fairchild.jdkapi.nio.socket.timeserver;

import com.fairchild.jdkapi.nio.socket.Notifier;
import com.fairchild.jdkapi.nio.socket.Server;

public class Main {

	public static void main(String[] args) {
		try {
			LogHandler loger = new LogHandler();
			TimeHandler timer = new TimeHandler();
			Notifier notifier = Notifier.getNotifier();
			notifier.addListener(loger);
			notifier.addListener(timer);

			System.out.println("Server starting.");
			Server server = new Server(5100);
			Thread timeServer = new Thread(server);
			timeServer.start();
		} catch (Exception e) {
			System.out.println("Server error: " + e.getMessage());
			System.exit(-1);
		}
	}
}
