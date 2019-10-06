/*
 * Created on Apr 9, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fruits.jdkapi.jdbc.isolationlevel;

import java.sql.Connection;

import com.fruits.jdkapi.jdbc.SelectRunnable;
import com.fruits.jdkapi.jdbc.SimpleAccountResultSetCallbackHandler;
import com.fruits.jdkapi.jdbc.UpdateRunnable;

public class SelectAndInsert {
	public static void main(String[] args) throws InterruptedException {
		Object lock = new Object();
		Thread insertThread = new Thread(new UpdateRunnable(IsolationLevelTestSQLs.INSERT_ACCOUNT_SQL, Connection.TRANSACTION_SERIALIZABLE, lock), "InsertThread");
		Thread selectThread = new Thread(new SelectRunnable(IsolationLevelTestSQLs.SELECT_ACCOUNT_SQL, Connection.TRANSACTION_REPEATABLE_READ, new SimpleAccountResultSetCallbackHandler(), lock), "SelectThread");
		selectThread.start();
		insertThread.start();
	}
}
