/*
 * Created on Apr 9, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fairchild.jdkapi.jdbc.isolationlevel;

import java.sql.Connection;

import com.fairchild.jdkapi.jdbc.UpdateRunnable;

public class UpdateAndInsert {
	public static void main(String[] args) throws InterruptedException {
		Object lock = new Object();
		Thread updateThread = new Thread(new UpdateRunnable(IsolationLevelTestSQLs.UPDATE_ACCOUNT_SQL, Connection.TRANSACTION_READ_UNCOMMITTED, lock), "UpdateThread");
		Thread insertThread = new Thread(new UpdateRunnable(IsolationLevelTestSQLs.INSERT_ACCOUNT_SQL, Connection.TRANSACTION_READ_UNCOMMITTED, lock), "InsertThread");
		insertThread.start();
		Thread.sleep(3 * 1000);
		updateThread.start();
	}
}
