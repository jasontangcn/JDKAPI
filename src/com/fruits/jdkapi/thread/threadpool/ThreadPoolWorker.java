package com.fruits.jdkapi.thread.threadpool;

import com.fruits.jdkapi.thread.util.SynchronizedStack;

public class ThreadPoolWorker {
	private static int nextWorkID = 0;
	private int workerID;

	private SynchronizedStack synchronizedStackIdleWorkers;
	private Thread internalThread;
	private volatile boolean noStopRequest;
	private SynchronizedStack signalStack;

	public ThreadPoolWorker(SynchronizedStack synchronizedStack) {
		synchronizedStackIdleWorkers = synchronizedStack;

		workerID = generateNextID();
		signalStack = new SynchronizedStack(1);

		noStopRequest = true;
		internalThread = new Thread(new Runnable() {
			public void run() {
				try {
					runWork();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		internalThread.start();
	}

	private static synchronized int generateNextID() {
		int id = nextWorkID;
		nextWorkID++;
		return id;
	}

	public void process(Runnable runnable) throws InterruptedException {
		signalStack.add(runnable);
	}

	private void runWork() {
		while (noStopRequest) {
			try {
				synchronizedStackIdleWorkers.add(this);
				// 线程阻塞在这里，等待任务
				Runnable r = (Runnable) signalStack.remove();
				System.out.println("workerID = " + workerID + ", starting execution of new Runnable  :" + r);
				runIt(r);
			} catch (InterruptedException it) {
				Thread.currentThread().interrupt(); // 再申明
			}
		}
	}

	private void runIt(Runnable runnable) {
		// 只有线程阻塞时，才会注意到中断标志
		try {
			runnable.run();
		} catch (Exception e) {
			System.err.println("Uncaught exception fell througn from run().");
			e.printStackTrace();
		} finally {
			// 无论runIt是怎么运行的，这里确保中断标志的清除。
			// 不然，当线程完成工作，回到空闲线程池的时候，在46行，准备进入阻塞状态的时候，会自动激发异常，而不能正常工作。
			Thread.interrupted();
		}
	}

	public void stopRequest() {
		System.out.println("workerID = " + workerID + ", stopRequest() received.");
		noStopRequest = false;
		internalThread.interrupt();
	}

	public boolean isAlive() {
		// isAlive()的意思是：是否在运行，哪怕是休眠或是刮起等状态。
		return internalThread.isAlive();
	}
}