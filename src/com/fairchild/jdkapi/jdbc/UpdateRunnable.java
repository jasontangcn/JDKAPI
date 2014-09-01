/*
 * Created on Apr 9, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fairchild.jdkapi.jdbc;

public class UpdateRunnable implements Runnable {
	private String sql;
	private int isolationLevel;
	private Object lock;

	public UpdateRunnable(String sql, int isolationLevel, Object lock) {
		this.sql = sql;
		this.isolationLevel = isolationLevel;
		this.lock = lock;
	}

	public void run() {
		JDBCTemplate.executeTransactionalUpdate(sql, isolationLevel, lock);
	}
}
