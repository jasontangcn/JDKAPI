/*
 * Created on Apr 9, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fruits.jdkapi.jdbc.isolationlevel;

import java.sql.Connection;

import com.fruits.jdkapi.jdbc.UpdateRunnable;

public class UpdateAndUpdate {
	public static void main(String[] args) throws InterruptedException {
		Object lock = new Object();
		Thread updateThread1 = new Thread(new UpdateRunnable(IsolationLevelTestSQLs.UPDATE_ACCOUNT_SQL, Connection.TRANSACTION_READ_COMMITTED, lock), "UpdateThread1");
		Thread updateThread2 = new Thread(new UpdateRunnable(IsolationLevelTestSQLs.UPDATE_ACCOUNT_SQL, Connection.TRANSACTION_READ_COMMITTED, lock), "UpdateThread2");
		updateThread1.start();
		Thread.sleep(3 * 1000);
		updateThread2.start();
	}
}
