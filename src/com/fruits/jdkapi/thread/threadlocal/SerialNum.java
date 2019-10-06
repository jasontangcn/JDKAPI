package com.fruits.jdkapi.thread.threadlocal;

public class SerialNum {
	private int nextNumber = 0;
	private ThreadLocal serialNum = new ThreadLocal() {
		protected synchronized Object initialValue() {
			return new Integer(nextNumber++);
		}
	};

	public int get() {
		return ((Integer) serialNum.get()).intValue();
	}

	public static void main(String[] args) {
		final SerialNum sn = new SerialNum();
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {
				public void run() {
					// System.out.println(new SerialNum().get());
					System.out.println(sn.get());
				}
			}).start();
	    /*
	     *Thinking(TomHornson@hotmail.com)
	     情形1：如果nextNumber、serialNum、get()全部是static，
				并且run()运行语句：System.out.println(new SerialNum().get())
				那么很显然10个线程运行在10个对象上，每个线程初始化时，将注册 线程和线程局部变量值键值对。
			    那么由于nextNumber是static的，线程局部变量值不断增加。
	     情形2：nextNumber、serialNum、get()分别定义为：
				private int nextNumber = 0；
				private ThreadLocal serialNum ...
				public int get()...
				run()运行语句：System.out.println(new SerialNum().get())
				那么SerialNum是对象级的，自然全部为0。
				因为线程初始化时，虽然还是每个线程在一个对象上运行。
				但是nextNumber是对象实例变量，初始为0。
	     情况3：
	            如果nextNumber、serialNum、get()分别定义为：
				private int nextNumber = 0；
				private ThreadLocal serialNum ...
				public int get()...
			    并且final SerialNum sn = new SerialNum();定义一个对象，
			    然后让10个线程运行sn.get()，那么在线程进入对象时，线程局部变量初始化，nextNumber不断增加。
			    从这个角度上说，线程局部变量在对象（线程将要运行于上）级关联线程。
				In other words,对象维护各线程局部变量。
	     */
		}
	}
}