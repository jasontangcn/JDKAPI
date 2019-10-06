package com.fruits.jdkapi.thread.exceptionlistener;

public class ExceptionCallbackMain extends Object implements ExceptionListener {
	private int exceptionCount = 0;

	public ExceptionCallbackMain() {
	}

	public void exceptionOccurred(Exception ex, Object source) {
		exceptionCount++;
		System.err.println("Exception# " + exceptionCount + ", source=" + source);
		ex.printStackTrace();
	}

	public static void main(String[] args) {
		ExceptionListener listener = new ExceptionCallbackMain();
		ExceptionCallback callback = new ExceptionCallback(listener);
	}
}