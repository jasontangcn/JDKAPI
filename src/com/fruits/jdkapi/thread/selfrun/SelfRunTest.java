package com.fruits.jdkapi.thread.selfrun;

public class SelfRunTest {
	public static void main(String[] args) {
		SelfRun selfRun = new SelfRun();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}

		/*
		 * 试验1：我使用了selfRun.run()，观察结果。 针对结果我的解释：
		 * internalThread.sleep()的调用其实作用于main线程了。
		 */
		// selfRun.run();

		selfRun.stopRequest();
	}
}