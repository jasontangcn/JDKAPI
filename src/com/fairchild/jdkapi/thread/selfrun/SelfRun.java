package com.fairchild.jdkapi.thread.selfrun;

/*
 * 实现的特殊功能:
 * 1、可以中断线程
 * 2、防止其它的线程在这个对象上运行
 *
 */

public class SelfRun extends Object implements Runnable {
	private Thread internalThread;
	private volatile boolean noStopRequest;

	public SelfRun() {
		System.out.println("The SelfRun object is initializing.");

		noStopRequest = true;
		internalThread = new Thread(this);
		internalThread.start();
	}

	public void run() {
		// 防止其它线程直接调用run.
		if (Thread.currentThread() != internalThread) {
			throw new RuntimeException("Only the internalThread is allowed to run this method.");
		}
		while (noStopRequest) {
			System.out.println("Run() is going in the thread " + Thread.currentThread().getName());
			try {
				// internalThread.sleep(700);
				Thread.sleep(700);
			} catch (InterruptedException e) {
				System.out.println("The internalThread is interrupted.");
				// 这里，我将run()停止条件转移到这里，因为考虑到中断的原因，不单单是手动设置。
				noStopRequest = false;
				// 在这里使用return也是一种解决方案。
				// return;
				// Thread.currentThread().interrupt();
			}
		}
	}

	public void stopRequest() {
		// noStopRequest = false;
		internalThread.interrupt();
	}

	public boolean isAlive() {
		return internalThread.isAlive();
	}
}