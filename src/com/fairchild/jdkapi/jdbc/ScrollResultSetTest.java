/*
 * Created on 2004-9-15
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.fairchild.jdkapi.jdbc;

import java.sql.*;

/**
 * @author Administrator
 *
 *         To change the template for this generated type comment go to Window -
 *         Preferences - Java - Code Generation - Code and Comments
 */
public class ScrollResultSetTest {
	public static void main(String[] args) {
		String dbDriverName = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		String dbURL = "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=EnterpriseManagementSys";
		Connection conn = null;
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			Class.forName(dbDriverName);
			conn = DriverManager.getConnection(dbURL, "sa", "123456");
			// 又没有起作用,shit!
			conn.setReadOnly(true);
			statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			/*
			 * 我们讲Rows、Columns组成的ResultSet看作矩形。
			 * 
			 * 那么resultSetType决定ResultSet两个方面的特性：
			 * 1)是否可以使用absolute、last、afterLast、beforeFirst等来移动row定位光标。
			 * 2)是否同步于数据库。
			 * 
			 * 而resultSetConcurrency决定ResultSet另外两个特性：
			 * 1)是否可以重复读(即re-read)row的column。
			 * 2)ResultSet是否可以Update。
			 * 
			 * 但是在MS SQL Server2000 (SP2 MS Driver)下，
			 * 发现resultSetConcurrency是否设置FORWARD_ONLY不会影响resultSetType的FORWARD_ONLY特性。
			 * 但是resultSetType设置为TYPE_FORWARD_ONLY后 ，不管resultSetConcurrency设置为什么，都不可以re-read。
			 */
			resultSet = statement.executeQuery("SELECT * FROM EnterpriseAccount ");
			// resultSet.next();
			resultSet.last();
			System.out.println(resultSet.getString("account"));
			System.out.println(resultSet.getString("password"));
			System.out.println(resultSet.getString("username"));
			resultSet.updateString("password", "123456");
			resultSet.updateRow();
			// while(resultSet.next()){
			   /*
				 * resultSet.absolute(2); 
				 * System.out.println(resultSet.getString("username"));
				 * System.out.println(resultSet.getString("password"));
				 * //System.out.println(resultSet.getString("username"));
				 * 
				 * resultSet.absolute(1);
				 * System.out.println(resultSet.getString("username"));
				 * System.out.println(resultSet.getString("password"));
				 * //System.out.println(resultSet.getString("username"));
				 * resultSet.absolute(2);
				 * System.out.println(resultSet.getString("username"));
				 * System.out.println(resultSet.getString("password"));
				 * System.out.println(resultSet.getString("username")); 
			   }
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (null != statement)
					statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if ((null != conn) && (!conn.isClosed()))
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
