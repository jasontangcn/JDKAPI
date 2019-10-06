/*
 * Created on Apr 10, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fruits.jdkapi.jdbc;

public class SystemException extends RuntimeException {
	static final long serialVersionUID = -4387516993124229941L;

	public SystemException() {
		super("SystemException: check Database or JNDI settings.");
	}

	public SystemException(String message) {
		super(message);
	}

	public SystemException(String message, Throwable cause) {
	}

	public SystemException(Throwable cause) {
		super(cause);
	}

}
