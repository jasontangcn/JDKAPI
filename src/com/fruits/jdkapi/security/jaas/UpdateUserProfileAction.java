package com.fruits.jdkapi.security.jaas;

import java.security.PrivilegedAction;

public class UpdateUserProfileAction implements PrivilegedAction {
	private String userName;
	private UserProfile profile;
	private String owner;

	/**
	 * @return the <code>UserProfile</code> instance.
	 *
	 * @exception SecurityException
	 *                if the caller does not have permission to perform the
	 *                operation.
	 */
	public Object run() {
		if (userName != null)
			profile.setUserName(userName);

		if (owner != null)
			profile.setOwner(owner);

		return profile;
	}

	/**
	 * Sets the userName
	 * 
	 * @param userName
	 *            The userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the userName
	 * 
	 * @return Returns a String
	 */
	public String getUserName() {
		return userName;
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
	 * Sets the owner
	 * 
	 * @param owner
	 *            The owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

}
