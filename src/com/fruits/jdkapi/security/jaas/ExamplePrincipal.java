package com.fruits.jdkapi.security.jaas;import java.security.InvalidParameterException;import java.security.Principal;/** * @see java.security.Principal * @see javax.security.auth.Subject */public class ExamplePrincipal implements Principal {	private String name;	/**	 *	 * @param name	 *            the name for this principal.	 *	 * @exception InvalidParameterException	 *                if the <code>name</code> is <code>null</code>.	 */	public ExamplePrincipal(String name) {		if (name == null)			throw new InvalidParameterException("name cannot be null");		this.name = name;	}	/**	 * Returns the name for this <code>PrincipalExample</code>.	 * 	 * @return the name for this <code>PrincipalExample</code>	 */	public String getName() {		return name;	}	/**
	 *
     */	public String toString() {		return ("PrincipalExample: " + name);	}	/**	 * Compares the specified Object with this <code>ExamplePrincipal</code>.	 * Returns true if the specified object is an instance of	 * <code>ExamplePrincipal</code> and has the same name as this ExamplePrincipal.	 *	 * @param object	 *            Object to be compared	 *	 * @return true if the specified Object is equal to this	 *         <code>PrincipalExample</code>.	 */	public boolean equals(Object object) {		if (object == null)			return false;		if (!(object instanceof ExamplePrincipal))			return false;		if (object == this)			return true;		ExamplePrincipal principal = (ExamplePrincipal) object;		if (getName().equals(principal.getName()))			return true;		return false;	}	/**
     *
     */	public int hashCode() {		return name.hashCode();	}}