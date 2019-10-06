package com.fruits.jdkapi.thread.threadlocal;

public class ThreadIdTest extends Object implements Runnable {
	private ThreadId threadId;

	public ThreadIdTest(ThreadId threadId) {
		this.threadId = threadId;
	}

	public void run() {
		try {
			print("threadId.getThreadID = " + threadId.getThreadID());
			Thread.sleep(2000);
			print("threadId.getThreadID = " + threadId.getThreadID());
		} catch (InterruptedException e) {
		}
	}

	private void print(String msg) {
		System.out.println(Thread.currentThread().getName() + " : " + msg);
	}

	public static void main(String[] args) {
		ThreadIdTest shared = new ThreadIdTest(new ThreadId(1000));
		
		try {
			Thread thread1 = new Thread(shared, "Thread1");
			thread1.start();

			Thread.sleep(500);

			Thread thread2 = new Thread(shared, "thread2");
			thread2.start();

			Thread.sleep(500);

			Thread thread3 = new Thread(shared, "thread3");
			thread3.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}