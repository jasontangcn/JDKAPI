/*
 * Created on Apr 9, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fairchild.jdkapi.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleAccountResultSetCallbackHandler implements ResultSetCallbackHandler {
	public void processResultSet(ResultSet rs) throws SQLException {
		while (rs.next()) {
			rs.getString("ownerName");
		}
	}
}
