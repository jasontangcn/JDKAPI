package com.fairchild.jdkapi.security.jaas;

import javax.security.auth.Subject;

/**
 * This interface is used by implementing classes that want to provide class
 * instance authorization.
 * 
 * @see XMLPolicyFile
 */
public interface Resource {

	/**
	 * Called by the ResourcePermission class when the <code>owner</code>
	 * relationship is specified in the policy.
	 * 
	 * @return the owner of the implementor's class instance.
	 */
	public String getOwner();

	/**
	 * Called when relationships other than the <code>owner</code> relationship
	 * is specified in the policy. It is the responsibility of the implementing
	 * class to provide the logic.
	 * 
	 * @param subject
	 *            the Subject executing the code.
	 * @param relationShip
	 *            the relationship defined in the policy
	 * 
	 * @return true if provided subject executing the code fulfills the
	 *         specified relationship in the implementing class instance.
	 *         Returns false otherwise.
	 */
	public boolean fulfills(Subject subject, String relationShip);
}
