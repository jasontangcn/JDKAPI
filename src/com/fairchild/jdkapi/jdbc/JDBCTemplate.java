/*
 * Created on Apr 9, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fairchild.jdkapi.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCTemplate {
	public static void executeTransactionalQuery(String sql, int isolationLevel, ResultSetCallbackHandler resultSetCallbackHandler, Object lock) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// lock.notify();
			// lock.wait();
			conn = ConnectionHelper.getConnection();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(isolationLevel);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (null != resultSetCallbackHandler)
				resultSetCallbackHandler.processResultSet(rs);
			// lock.wait(10 * 1000);
			conn.commit();
		} catch (Exception e) {
			try {
				if ((null != conn) && !conn.isClosed())
					conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			if (e instanceof SystemException)
				throw (SystemException) e;
			throw new SystemException(e);
		} finally {
			try {
				if (null != rs)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (null != ps)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if ((null != conn) && !conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			lock.notify();
		}
	}

	public static int executeTransactionalUpdate(String sql, int isolationLevel, Object lock) {
		Connection conn = null;
		PreparedStatement ps = null;
		int count = Integer.MIN_VALUE;
		try {
			// lock.notify();
			// lock.wait();
			conn = ConnectionHelper.getConnection();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(isolationLevel);
			ps = conn.prepareStatement(sql);
			count = ps.executeUpdate();
			// lock.wait(10 * 1000);
			conn.commit();
			return count;
		} catch (Exception e) {
			try {
				if ((null != conn) && !conn.isClosed())
					conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			if (e instanceof SystemException)
				throw (SystemException) e;
			throw new SystemException(e);
		} finally {
			try {
				if (null != ps)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if ((null != conn) && !conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			lock.notify();
		}
	}

	public static void executeQuery(String sql, int isolationLevel, ResultSetCallbackHandler resultSetCallbackHandler) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = ConnectionHelper.getConnection();
			conn.setTransactionIsolation(isolationLevel);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (null != resultSetCallbackHandler)
				resultSetCallbackHandler.processResultSet(rs);
		} catch (Exception e) {
			if (e instanceof SystemException)
				throw (SystemException) e;
			throw new SystemException(e);
		} finally {
			try {
				if (null != rs)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (null != ps)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if ((null != conn) && !conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static int executeUpdate(String sql, int isolationLevel) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = ConnectionHelper.getConnection();
			conn.setTransactionIsolation(isolationLevel);
			ps = conn.prepareStatement(sql);
			return ps.executeUpdate();
		} catch (Exception e) {
			if (e instanceof SystemException)
				throw (SystemException) e;
			throw new SystemException(e);
		} finally {
			try {
				if (null != ps)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if ((null != conn) && !conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
