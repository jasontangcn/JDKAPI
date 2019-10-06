package com.fruits.jdkapi.security.jaas;

import java.security.PrivilegedAction;

public class ReadUserProfileAction implements PrivilegedAction {
	private String userName;
	private String owner;
	private UserProfile profile;

	/**
	 * @return the <code>UserProfile</code> instance.
	 *
	 * @exception SecurityException
	 *                if the caller does not have permission to perform the
	 *                operation.
	 */
	public Object run() {
		userName = profile.getUserName();
		owner = profile.getOwner();
		return profile;
	}

	/**
	 * Gets the profile
	 * 
	 * @return Returns a UpdateUserProfileAction
	 */
	public UserProfile getProfile() {
		return profile;
	}

	/**
	 * Sets the profile
	 * 
	 * @param profile
	 *            The profile to set
	 */
	public void setProfile(UserProfile profile) {
		this.profile = profile;
	}

	/**
	 * Gets the owner
	 * 
	 * @return Returns a String
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Gets the userName
	 * 
	 * @return Returns a String
	 */
	public String getUserName() {
		return userName;
	}
}
