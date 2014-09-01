package com.fairchild.jdkapi.thread.threadpool;

public class ThreadPoolMain extends Object {
	public static Runnable makeRunnable(final String name, final long delay) {
		return new Runnable() {
			public void run() {
				try {
					System.out.println(name + " was started.");
					Thread.sleep(delay);
					System.out.println(name + " is working.");
					Thread.sleep(2000);
					System.out.println(name + " is leaving");
				} catch (InterruptedException e) {
					System.out.println(name + " : is interrupted!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public String toString() {
				return name;
			}
		};
	}

	public static void main(String[] args) {
		try {
			ThreadPoolManager pool = new ThreadPoolManager(3);

			Runnable work1 = makeRunnable("Work1", 3000);
			pool.excute(work1);
			Runnable work2 = makeRunnable("Work2", 1000);
			pool.excute(work2);
			Runnable work3 = makeRunnable("Work3", 2000);
			pool.excute(work3);
			Runnable work4 = makeRunnable("Work4", 60000);
			pool.excute(work4);
			Runnable work5 = makeRunnable("Work5", 1000);
			pool.excute(work5);

			pool.stopIdleThreadWorkers();
			Thread.sleep(2000);
			pool.stopIdleThreadWorkers();
			Thread.sleep(5000);

			pool.stopAllThreadWorkers();

		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}
}