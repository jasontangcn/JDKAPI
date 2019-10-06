/*
 * Created on Apr 9, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fairchild.jdkapi.jdbc.isolationlevel;

import java.sql.Connection;

import com.fairchild.jdkapi.jdbc.SelectRunnable;
import com.fairchild.jdkapi.jdbc.SimpleAccountResultSetCallbackHandler;
import com.fairchild.jdkapi.jdbc.UpdateRunnable;

public class SelectAndUpdate {
	public static void main(String[] args) throws InterruptedException {
		Object lock = new Object();
		Thread updateThread = new Thread(new UpdateRunnable(IsolationLevelTestSQLs.UPDATE_ACCOUNT_SQL, Connection.TRANSACTION_READ_UNCOMMITTED, lock), "UpdateThread");
		Thread selectThread = new Thread(new SelectRunnable(IsolationLevelTestSQLs.SELECT_ACCOUNT_SQL, Connection.TRANSACTION_SERIALIZABLE, new SimpleAccountResultSetCallbackHandler(), lock), "SelectThread");
		selectThread.start();
		updateThread.start();
	}
}
