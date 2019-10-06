package com.fruits.jdkapi.security.jaas;

import java.security.AccessController;
import java.security.Permission;
import java.security.Principal;
import java.util.Iterator;

import javax.security.auth.Subject;

public class UserProfile implements Resource {
	private String userName;
	private String owner = JAASConstants.USERNAME_JANE;

	public UserProfile() {
		Permission permission = new com.fruits.jdkapi.security.jaas.ResourcePermission("com.fruits.jdkapi.security.jaas.UserProfile", "create");
		AccessController.checkPermission(permission);
	}

	public void setUserName(String userName) {
		Permission permission = new com.fruits.jdkapi.security.jaas.ResourcePermission("com.fruits.jdkapi.security.jaas.UserProfile", "write", this);
		AccessController.checkPermission(permission);

		this.userName = userName;
	}

	public String getUserName() {
		Permission permission = new com.fruits.jdkapi.security.jaas.ResourcePermission("com.fruits.jdkapi.security.jaas.UserProfile", "read", this);
		AccessController.checkPermission(permission);

		return userName;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String newOwner) {
		Permission permission = new com.fruits.jdkapi.security.jaas.ResourcePermission("com.fruits.jdkapi.security.jaas.UserProfile", "write", this);
		AccessController.checkPermission(permission);

		this.owner = newOwner;
	}

	public boolean fulfills(Subject subject, String relationShip) {
		if (JAASConstants.USERNAME_JANESMANAGER.equalsIgnoreCase(relationShip) && getOwner().equals(JAASConstants.USERNAME_JANE)) {
			Iterator it = subject.getPrincipals().iterator();
			while (it.hasNext()) {
				Principal principal = (Principal) it.next();
				if (principal.getName().equals(JAASConstants.USERNAME_LOU))
					return true;
			}
		}

		return false;
	}

}
