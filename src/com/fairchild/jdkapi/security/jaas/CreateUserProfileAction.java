package com.fairchild.jdkapi.security.jaas;

import java.security.PrivilegedAction;

/**
 * This is a Sample PrivilegedAction implementation, designed to be used with the Sample application.
 */
public class CreateUserProfileAction implements PrivilegedAction {

	/**
	 * <p>
	 * This Sample PrivilegedAction performs the following operations:
	 * <ul>
	 * <li></i>
	 * <li></i>
	 * <li></i>
	 * </ul>
	 *
	 * @return <code>null</code> in all cases.
	 *
	 * @exception SecurityException
	 *                if the caller does not have permission to perform the
	 *                operations listed above.
	 */
	public Object run() {
		return new com.fairchild.jdkapi.security.jaas.UserProfile();
	}

}
