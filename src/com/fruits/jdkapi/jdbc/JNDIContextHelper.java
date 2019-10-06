/*
 * Created on Apr 10, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fruits.jdkapi.jdbc;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JNDIContextHelper {
	public static Context getRemoteContext() {
		try {
			Properties props = new Properties();
			props.put(Context.INITIAL_CONTEXT_FACTORY, JDBCConstants.INITIAL_CONTEXT_FACTORY);
			props.put(Context.PROVIDER_URL, JDBCConstants.INITIAL_CONTEXT_PROVIDER_URL);
			props.put(Context.SECURITY_PRINCIPAL, JDBCConstants.CREDENTIAL_USERNAME);
			props.put(Context.SECURITY_CREDENTIALS, JDBCConstants.CREDENTIAL_PASSWORD);
			return new InitialContext(props);
		} catch (NamingException e) {
			throw new SystemException(e);
		}
	}

	public static Context getLocalContext() {
		try {
			return new InitialContext();
		} catch (NamingException e) {
			throw new SystemException(e);
		}
	}
}
