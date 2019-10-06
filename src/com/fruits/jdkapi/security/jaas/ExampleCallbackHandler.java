package com.fruits.jdkapi.security.jaas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class ExampleCallbackHandler implements CallbackHandler {
	/**
	 * @see CallbackHandler#handle(Callback[])
	 */
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof TextOutputCallback) {
				TextOutputCallback textOutputCallback = (TextOutputCallback) callbacks[i];
				switch (textOutputCallback.getMessageType()) {
				case TextOutputCallback.INFORMATION:
					System.out.println(textOutputCallback.getMessage());
					break;
				case TextOutputCallback.ERROR:
					System.out.println("ERROR: " + textOutputCallback.getMessage());
					break;
				case TextOutputCallback.WARNING:
					System.out.println("WARNING: " + textOutputCallback.getMessage());
					break;
				default:
					throw new IOException("Invalid message type: " + textOutputCallback.getMessageType());
				}
			} else if (callbacks[i] instanceof NameCallback) {
				// prompt the user for a userid
				NameCallback nc = (NameCallback) callbacks[i];
				System.out.print(nc.getPrompt());
				System.out.flush();
				nc.setName((new BufferedReader(new InputStreamReader(System.in))).readLine());
			} else if (callbacks[i] instanceof PasswordCallback) {
				// prompt the user for the password
				PasswordCallback pc = (PasswordCallback) callbacks[i];
				System.out.print(pc.getPrompt());
				System.out.flush();
				pc.setPassword(readPassword(System.in));
			} else {
				throw new UnsupportedCallbackException(callbacks[i], "Invalid Callback");
			}
		}
	}

	private char[] readPassword(InputStream in) throws IOException {
		String pwd = (new BufferedReader(new InputStreamReader(in))).readLine();
		return pwd.toCharArray();
	}

}
