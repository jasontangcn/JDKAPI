/*
 * Created on Apr 9, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fairchild.jdkapi.jdbc.isolationlevel;

import java.sql.Connection;

import com.fairchild.jdkapi.jdbc.SelectRunnable;
import com.fairchild.jdkapi.jdbc.SimpleAccountResultSetCallbackHandler;

public class SELECT_SELECT {
	public static void main(String[] args) throws InterruptedException {
		Object lock = new Object();
		Thread selectThread1 = new Thread(new SelectRunnable(IsolationLevelTestSQLs.SELECT_ACCOUNT_SQL, Connection.TRANSACTION_SERIALIZABLE, new SimpleAccountResultSetCallbackHandler(), lock), "SelectThread1");
		Thread selectThread2 = new Thread(new SelectRunnable(IsolationLevelTestSQLs.SELECT_ACCOUNT_SQL, Connection.TRANSACTION_SERIALIZABLE, new SimpleAccountResultSetCallbackHandler(), lock), "SelectThread2");
		selectThread1.start();
		selectThread2.start();
	}
}
