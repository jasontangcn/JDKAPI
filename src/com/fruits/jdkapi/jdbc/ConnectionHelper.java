/*
 * Created on Apr 8, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fairchild.jdkapi.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.sql.DataSource;

public final class ConnectionHelper {
	private static Connection getRemoteConnection() {
		try {
			return ((DataSource) JNDIContextHelper.getRemoteContext().lookup(JDBCConstants.DATASOURCE_URL)).getConnection();
		} catch (NamingException e) {
			throw new SystemException(e);
		} catch (SQLException e) {
			throw new SystemException(e);
		}
	}

	private static Connection getLocalConnection() {
		try {
			return ((DataSource) JNDIContextHelper.getRemoteContext().lookup(JDBCConstants.DATASOURCE_J2EE_STANDARD_URL)).getConnection();
		} catch (NamingException e) {
			throw new SystemException(e);
		} catch (SQLException e) {
			throw new SystemException(e);
		}
	}

	public static Connection getConnection() {
		int remoteOrLocal = JDBCConstants.DATASOURCE_REMOTE_OR_LOCAL;
		if (remoteOrLocal == 0)
			return getLocalConnection();
		return getRemoteConnection();
	}
}
