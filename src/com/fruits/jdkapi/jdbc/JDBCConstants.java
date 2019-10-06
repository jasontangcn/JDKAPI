/*
 * Created on Apr 10, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fruits.jdkapi.jdbc;

public class JDBCConstants {
	public static final String INITIAL_CONTEXT_FACTORY = "com.apusic.naming.jndi.CNContextFactory";
	public static final String INITIAL_CONTEXT_PROVIDER_URL = "iiop://localhost:6888";
	public static final String CREDENTIAL_USERNAME = "admin";
	public static final String CREDENTIAL_PASSWORD = "admin";
	/*
	 * DataSource
	 */
	/*
	 * 0: local 1: remote
	 */
	public static final int DATASOURCE_REMOTE_OR_LOCAL = 0;
	public static final String DATASOURCE_URL = "jdbc/sqlserver/dataSource";
	public static final String DATASOURCE_J2EE_STANDARD_URL = "java:comp/env/jdbc/sqlserver/dataSource";
	/*
	 * UserTransaction
	 */
	public static final String USERTRANSACTION = "/UserTransaction";
}
