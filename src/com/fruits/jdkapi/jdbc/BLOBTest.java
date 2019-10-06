/*
 * Created on 2004-10-29
 *
 */
package com.fruits.jdkapi.jdbc;

/**
 * @author TomHornson@hotmail.com
 */
import java.sql.*;
import java.io.*;

//import com.microsoft.jdbc.base.BaseBlob;

public class BLOBTest {
	public static void main(String[] args) throws Exception {
		Connection conn = null;
		Statement statement = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String sql = null;

		BufferedInputStream is = null;
		byte[] bytes = null;
		int length = 0;

		String content = null;

		// BaseBlob blob = new BaseBlob();

		try {
			is = new BufferedInputStream(BLOBTest.class.getResourceAsStream("/blob.txt"));
			length = is.available();
			if (length == 0)
				throw new RuntimeException("blob.txt is empty.");
			bytes = new byte[length];
			is.read(bytes);

			content = new String(bytes);
			System.out.println(content);
			// blob.setBytes(0,bytes);
			/*
			 * for(int i = 0; i < length; i++){ System.out.println(bytes[i]); }
			 */
			/*
			 * System.out.println(bytes.length);
			 * 
			 * String content = new String(bytes,"utf-8");
			 * 
			 * System.out.println(content.length());
			 * System.out.println(content);
			 */
			/*
			 * byte[] bytes = content.getBytes("ISO-8859-1");
			 * System.out.println(bytes.length);
			 * String gbk = new String(bytes,"GBK");
			 * String iso88591 = new String(gbk.getBytes("GBK"),"ISO-8859-1");
			 * System.out.println(gbk);
			 */

		} finally {
			try {
				if (null != is)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (null != conn)
					conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");

		try {
			conn = DriverManager.getConnection("jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=JDBCStudy", "sa", "123456");
			// statement = conn.createStatement();
			preparedStatement = conn.prepareStatement("INSERT INTO nclobtest(name, blobFieldName) VALUES (?, ?)");
			/*
			 * SQL Server does not have function EMPTY_BLOB and does not have types BLOB and CLOB.
			 * sql = "INSERT INTO blobtest(name, blobFieldName) VALUES ('BlobTest',EMPTY_BLOB())";
			 * statement.executeUpdate(sql);
			 * sql = "SELECT blobFieldName FROM blobtest WHERE name = 'BlobTest'";
			 * resultSet = statement.executeQuery(sql);
			 * resultSet.next(); 
			 * blob = (BaseBlob)rs.getBlob("blobFieldName");
			 * blob.setBytes(0, bytes);
			 */

			/*
			 * preparedStatement.setString(1, "Louis");
			 * preparedStatement.setBlob(1, blob);
			 * preparedStatement.executeUpdate();
			 */
			preparedStatement.setString(1, "Louis");
			preparedStatement.setString(2, content);
			preparedStatement.executeUpdate();
		} finally {
			try {
				if (null != statement)
					statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (null != preparedStatement)
					preparedStatement.close();
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
