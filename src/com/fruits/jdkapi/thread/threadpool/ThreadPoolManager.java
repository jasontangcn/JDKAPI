package com.fruits.jdkapi.thread.threadpool;

import com.fruits.jdkapi.thread.util.SynchronizedStack;

/*
 * ThreadPoolManager功能描述：生成、管理线程池中的线程。
 * 并且这个ThreadPool是一个“范型线程池”。
 * 因为它和具体工作对象的接口：就是run方法。
 *
 */
public class ThreadPoolManager {
	private int threadNumber;
	//
	// 变量difoStackIdleWorkers、threadPoolWorks的逻辑
	// ThreadPoolWorkers持有所有的线程
	// 而fifoStackIdleWorkers持有所有的空闲线程
	// 这样做的目的是为了保证线程完成任务后重新回到池中，而不至于没有句柄把持或者被垃圾回收器
	// 回收
	//
	//
	private SynchronizedStack synchronizedStackIdleWorkers;
	private ThreadPoolWorker[] threadPoolWorks;

	public ThreadPoolManager(int threadThreshold) throws InterruptedException {
		threadThreshold = Math.max(1, threadThreshold);
		synchronizedStackIdleWorkers = new SynchronizedStack(threadThreshold);
		threadPoolWorks = new ThreadPoolWorker[threadThreshold];

		for (int i = 0; i < threadThreshold; i++) {
			threadPoolWorks[i] = new ThreadPoolWorker(synchronizedStackIdleWorkers);
		}

	}

	public void excute(Runnable runnable) throws InterruptedException {
		ThreadPoolWorker worker = (ThreadPoolWorker) synchronizedStackIdleWorkers.remove();
		worker.process(runnable);
	}

	public void stopIdleThreadWorkers() {
		// stopIdleThreadWorkers()实现的逻辑是：
		// ThreadPoolWorker中runWork()的if(noStopRequest)条件判断失败，那么线程ThreadPoolWorker维持的线程消亡。
		try {
			Object[] object = synchronizedStackIdleWorkers.removeAll();
			for (int i = 0; i < object.length; i++)
				((ThreadPoolWorker) object[i]).stopRequest();
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();// 再声明
		}
	}

	public void stopAllThreadWorkers() {
		stopIdleThreadWorkers();

		try {
			Thread.sleep(250);
		} catch (InterruptedException ie) {
		}

		for (int i = 0; i < threadPoolWorks.length; i++) {
			// 如果线程已经消亡，那么isAlive为false
			// 如果还没有消亡，那么就stopRequest
			if (threadPoolWorks[i].isAlive())
				threadPoolWorks[i].stopRequest();
		}
	}

}