/*
 * Created on Apr 9, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fairchild.jdkapi.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetCallbackHandler {
	public void processResultSet(ResultSet rs) throws SQLException;
}
