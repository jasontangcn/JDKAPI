/*
 * Created on Apr 9, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fruits.jdkapi.jdbc.isolationlevel;

import java.sql.Connection;

import com.fruits.jdkapi.jdbc.UpdateRunnable;

public class InsertAndInsert {
	public static void main(String[] args) throws InterruptedException {
		Object lock = new Object();
		Thread insertThread1 = new Thread(new UpdateRunnable(IsolationLevelTestSQLs.INSERT_ACCOUNT_SQL, Connection.TRANSACTION_SERIALIZABLE, lock), "InsertThread1");
		Thread insertThread2 = new Thread(new UpdateRunnable(IsolationLevelTestSQLs.INSERT_ACCOUNT_SQL2, Connection.TRANSACTION_SERIALIZABLE, lock), "InsertThread2");
		insertThread1.start();
		insertThread2.start();
	}
}
