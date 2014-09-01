package com.fairchild.jdkapi.thread.threadlocal;

public class ThreadId extends ThreadLocal {
	private int nextId;

	public ThreadId(int nextId) {
		this.nextId = nextId;
	}

	private synchronized Integer getNewId() {
		Integer id = new Integer(nextId);
		nextId++;
		return id;
	}

	public int getThreadID() {
		Integer id = (Integer) get();
		return id.intValue();
	}

	protected synchronized Object initialValue() {
		print("Invoking initialValue.");
		return getNewId();
	}

	private void print(String msg) {
		System.out.println(Thread.currentThread().getName() + " : " + msg);
	}
}