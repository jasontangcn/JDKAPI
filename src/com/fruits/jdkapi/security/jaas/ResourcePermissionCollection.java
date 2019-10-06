package com.fairchild.jdkapi.security.jaas;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Enumeration;
import java.util.Hashtable;

public class ResourcePermissionCollection extends PermissionCollection {
	private Hashtable permissions;

	public ResourcePermissionCollection() {
		permissions = new Hashtable();
	}

	/**
	 * @see PermissionCollection#elements()
	 */
	public Enumeration elements() {
		//System.out.println("DefaultResourceActionPermissionCollection.elements()");
		Hashtable list = new Hashtable();
		Enumeration e = permissions.elements();
		while(e.hasMoreElements()) {
			Hashtable table = (Hashtable) e.nextElement();
			list.putAll(table);
		}
		return list.elements();
	}

	/**
	 * @see PermissionCollection#implies(Permission)
	 */
	public boolean implies(Permission permission) {
		if( !(permission instanceof ResourcePermission) )
			throw new IllegalArgumentException("Wrong permission type");
			
		ResourcePermission rcsPermission = (ResourcePermission) permission;
		Hashtable aggregate = (Hashtable) permissions.get(rcsPermission.getName());
		if(aggregate == null)
			return false;

		Enumeration e = aggregate.elements();
		while(e.hasMoreElements()) {
			ResourcePermission p = (ResourcePermission) e.nextElement();
			if(p.implies(permission))
				return true;
		}
		
		return false;
	}

	/**
	 * @see PermissionCollection#add(Permission)
	 */
	public void add(Permission permission) {
		if (isReadOnly())
			throw new IllegalArgumentException("Read only collection.");

		if (!(permission instanceof ResourcePermission))
			throw new IllegalArgumentException("Wrong permission type.");

		// Same permission names may have different relationships.
		// Therefore permissions are aggregated by relationship.
		ResourcePermission rcsPermission = (ResourcePermission) permission;
		String relationShip = rcsPermission.getRelationShip();
		if (relationShip == null)
			relationShip = "none";

		Hashtable aggregate = (Hashtable) permissions.get(rcsPermission.getName());
		if (aggregate != null) {
			ResourcePermission existing = (ResourcePermission) aggregate.get(relationShip);
			if (existing != null)
				rcsPermission = merge(rcsPermission, existing);
		} else {
			aggregate = new Hashtable();
		}

		aggregate.put(relationShip, rcsPermission);
		permissions.put(rcsPermission.getName(), aggregate);
	}

	/**
	 * This is called when the same name is added twice to the collection. The
	 * actions are combine and the name is only stored once in the collection.
	 */
	private ResourcePermission merge(ResourcePermission a, ResourcePermission b) {
		String aActions = a.getActions();
		if (aActions.equals(""))
			return b;

		String bActions = b.getActions();
		if (bActions.equals(""))
			return a;

		return new ResourcePermission(a.getName(), aActions + ", " + bActions);
	}

}
