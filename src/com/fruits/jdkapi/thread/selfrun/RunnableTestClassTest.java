package com.fairchild.jdkapi.thread.selfrun;

public class RunnableTestClassTest {
	public static void main(String[] args) {
		RunnableTestClass testClassObj = new RunnableTestClass();
		Thread testClassThread = new Thread(testClassObj);
		testClassThread.start();

		System.out.println("Enter try block.");
		try {
			/*
			 * 特别注意： sleep是static的，只能作用于Thread.currentThread()，不能用于 一个线程将另外的线程置于休眠状态。
			 * 经过本人的试验
			 * ，这里的testClassThread.sleep(30000)实际效果是将main即Thread.currentThread(
			 * )线程休眠了30000s 精简的说：testClassThread.sleep(int
			 * millisSecond)就相当于调用了Thread.sleep(int millisSecond) 可能在Java语言中，类对象.静态变量
			 * == 类.静态变量。 语法上，类对象.静态变量是可以通过的。
			 */
			long startTime = System.currentTimeMillis();
			Thread.sleep(1000);
			System.out.println(System.currentTimeMillis() - startTime);
			System.out.println("AAAAA");

			startTime = System.currentTimeMillis();
			// Thread.currentThread().sleep(1000);
			System.out.println(System.currentTimeMillis() - startTime);
			System.out.println("BBBBB");

			// Thread.currentThread().sleep(10000);
			// testClassThread.sleep(10000);
			// Thread.currentThread().sleep(10000);
			// Thread.sleep(1000);
			System.out.println("CCCCC");
			// testClassThread.sleep(3000);
		} catch (InterruptedException e) {
		}

	}
}