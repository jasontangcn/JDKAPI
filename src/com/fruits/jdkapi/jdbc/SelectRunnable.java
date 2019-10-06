/*
 * Created on Apr 9, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fruits.jdkapi.jdbc;

public class SelectRunnable implements Runnable {
	private String sql;
	private int isolationLevel;
	private ResultSetCallbackHandler resultSetCallbackHandler;
	private Object lock;

	public SelectRunnable(String sql, int isolationLevel, ResultSetCallbackHandler resultSetCallbackHandler, Object lock) {
		this.sql = sql;
		this.isolationLevel = isolationLevel;
		this.resultSetCallbackHandler = resultSetCallbackHandler;
		this.lock = lock;
	}

	public void run() {
		JDBCTemplate.executeTransactionalQuery(sql, isolationLevel, resultSetCallbackHandler, lock);
	}
}
