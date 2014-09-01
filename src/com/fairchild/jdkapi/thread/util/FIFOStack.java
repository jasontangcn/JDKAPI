package com.fairchild.jdkapi.thread.util;

public class FIFOStack {
	private Object[] queue;
	private int capability;

	private int size = 0;
	private int head = 0;
	private int tail = 0;

	public FIFOStack(int capability) {
		capability = Math.max(1, capability);
		queue = new Object[capability];
	}

	public synchronized int getSize() {
		return size;
	}

	public synchronized boolean stackIsFull() {
		return (size == capability);
	}

	public synchronized boolean isEmpty() {
		return (size == 0);
	}

	public synchronized void add(Object obj) throws InterruptedException {
		while (stackIsFull())
			wait();
		queue[head] = obj;
		head = (head + 1) % capability;
		size++;

		notifyAll();
	}

	public synchronized Object remove() throws InterruptedException {
		while (size == 0)
			wait();
		Object obj = queue[tail];
		queue[tail] = null;
		tail = (tail + 1) % capability;
		size--;

		notifyAll();
		return obj;
	}

	public synchronized Object[] removeAll() throws InterruptedException {
		Object[] list = new Object[size];
		for (int control = 0; control < size; control++)
			list[control] = remove();
		return list;
	}
}