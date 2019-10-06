/*
 * Created on Apr 10, 2005
 * Author: TomHornson(at)hotmail.com
 */
package com.fruits.jdkapi.jdbc;

public class TransactionException extends Exception {
	static final long serialVersionUID = -4387516993124229941L;

	public TransactionException() {
		super("TransactionException.");
	}

	public TransactionException(String message) {
		super(message);
	}

	public TransactionException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransactionException(Throwable cause) {
		super(cause);
	}
}
