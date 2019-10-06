/*
 * Created on Apr 9, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fruits.jdkapi.jdbc.isolationlevel;

public class IsolationLevelTestSQLs {
	public static final String SELECT_ACCOUNT_SQL = "SELECT * FROM account WHERE ownerName = 'Louis'";
	public static final String UPDATE_ACCOUNT_SQL = "UPDATE account SET balance = 50000 WHERE ownerName = 'Jack'";
	public static final String INSERT_ACCOUNT_SQL = "INSERT INTO account(id, ownerName, balance) VALUES ('9922f9be-d073-4838-b16f-8952353ca146', 'Louis', 90000)";
	public static final String INSERT_ACCOUNT_SQL2 = "INSERT INTO account(id, ownerName, balance) VALUES ('9922f9be-d073-4838-b16f-8952353ca145', 'Jack', 90000)";
}
