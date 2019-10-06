package com.fruits.jdkapi.thread.selfrun;

public class RunnableTestClass extends Object implements Runnable {
	public void run() {
		try {
			for (int i = 1; i <= 250; i++) {
				Thread.sleep(70);
				System.out.println("00000-" + i);
			}
		} catch (InterruptedException e) {
		}
	}
}