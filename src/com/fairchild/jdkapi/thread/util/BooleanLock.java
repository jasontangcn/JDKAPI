/*
 * Created on 2004-10-24
 */
package com.fairchild.jdkapi.thread.util;

/**
 * @author TomHornson@hotmail.com
 *
 */
public class BooleanLock {
	private boolean value;

	public BooleanLock() {
		this(false);
	}

	public BooleanLock(boolean initialValue) {
		value = initialValue;
	}

	public synchronized void setValue(boolean newValue) {
		if (newValue != value) {
			value = newValue;
			notifyAll();
		}
	}

	public synchronized boolean isTrue() {
		return value;
	}

	public synchronized boolean isFalse() {
		return !value;
	}

	public synchronized boolean waitToSetTrue(long msTimeout) throws InterruptedException {
		boolean success = waitUtilFalse(msTimeout);
		if (success) {
			setValue(true);
		}
		return success;
	}

	public synchronized boolean waitToSetFalse(long msTimeout) throws InterruptedException {
		boolean success = waitUtilTrue(msTimeout);
		if (success) {
			setValue(false);
		}
		return success;
	}

	public synchronized boolean waitUtilTrue(long msTimeout) throws InterruptedException {
		return waitUtilStateIs(true, msTimeout);
	}

	public synchronized boolean waitUtilFalse(long msTimeout) throws InterruptedException {
		return waitUtilStateIs(false, msTimeout);
	}

	public synchronized boolean waitUtilStateIs(boolean state, long msTimeout) throws InterruptedException {
		if (msTimeout == 0L) {
			if (value != state) {
				wait();
			}
			return true;
		}
		long endTime = System.currentTimeMillis() + msTimeout;
		long msRemaing = msTimeout;

		while ((value != state) && (msRemaing > 0L)) {
			wait(msRemaing);
			msRemaing = endTime - System.currentTimeMillis();
		}
		return (value == state);
	}
}
