/*
 * Created on 2004-11-1
 *
 */
package com.fruits.jdkapi.jdbc;

import java.sql.ResultSet;

/**
 * @author TomHornson@hotmail.com
 */
public interface PageableResultSet extends ResultSet {
	int getPageCount();

	int getPageRowsCount();

	int getPageSize();

	void gotoPage(int page);

	void setPageSize(int pageSize);

	int getRowsCount();

	void pageFirst() throws java.sql.SQLException;

	void pageLast() throws java.sql.SQLException;

	int getCurrentPage();

}
