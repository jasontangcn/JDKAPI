package com.fruits.jdkapi.security.jaas;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Principal;
import java.util.Iterator;

import javax.security.auth.Subject;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;

/**
 * This sample policy class extends the JAAS policy concept by introducing
 * ownership and relationship to a class instance. JAAS extends the core Java
 * security by controlling permissions based on a Subject (user executing the
 * code). This class extends this further by controlling permissions based on a
 * class instance owner and relationship.
 * <p>
 * For example, all users are allowed to create an Action.class instance but
 * users can only modify the Auction.class instance they created (the instance
 * owned by the individual user). Further more, another user comes along a
 * creates a Bid.class instance corresponding to a particular Auction.class
 * instance. Using this policy class the Bid.class can be designed to allow the
 * the owner of the Auction.class instance to modify certain attributes on the
 * corresponding Bid.class instance by using a special relationship. The
 * attribute could be the accepted or rejected flag.
 * <p>
 * For simplicity this class uses an xml policy file and uses XPATH to search
 * for data. In a real application this class can be designed to retrieve data
 * from a relational database.
 * <p>
 * A policy is defined as follows:<br>
 * [permission][subject][resource][relationship]<br>
 * The [permission], [subject], and the optional [relationship] are defined in
 * the policy file.
 * <p>
 * The policy file uses the format defined in the following example:
 * 
 * <pre>
 * <?xml version="1.0"?>
	<policy>
		<grant codebase="file:/D:/JaasExample.jar">
			<principal classname="com.fruits.jdkapi.security.jaas.ExamplePrincipal" name="users">
				<permission classname="com.fruits.jdkapi.security.jaas.ResourcePermission" 
				            name="com.fruits.jdkapi.security.jaas.UserProfile"
				            actions="create" /> 
				<permission classname="com.fruits.jdkapi.security.jaas.ResourcePermission" 
				            name="com.fruits.jdkapi.security.jaas.UserProfile"
				            actions="write,delete,read" 
							relationship="owner" />			            						
				<permission classname="com.fruits.jdkapi.security.jaas.ResourcePermission" 
				            name="com.fruits.jdkapi.security.jaas.UserProfile"
				            actions="read" 
							relationship="janesManager" />			            									            
			</principal>
		</grant>
		
		<grant codebase="file:/D:/JaasExample/*">
			<principal classname="com.fruits.jdkapi.security.jaas.ExamplePrincipal" name="administrators">
				<permission classname="com.fruits.jdkapi.security.jaas.ResourcePermission" 
				            name="com.fruits.jdkapi.security.jaas.UserProfile"
				            actions="create,delete,read,write,execute" /> 
			</principal>
		</grant>
	</policy>
 * </pre>
 * 
 * In the example policy file above, any user with the usersGroup principal can
 * create a Action.class instance. Any user can read its attributes but only the
 * user that created the instance can update (write) it. The same holds true for
 * the Bid.class instances except that the owner of of the corresponding
 * Auction.class instance can change the bid acception flag.
 * <p>
 * The classes that need this type of protection need to implement the Resource
 * interface. The <code>getOwner()</code> method returns the owner of the class
 * instance. The <code>fulfills(Subject subject, String relationShip)</code>
 * method is used by the ResourcePermission class for dealing with special
 * relationships. The bulk of the work is performed by the
 * <code>implies(Permission p)</code> method of the ResourcePermission class.
 * This class understands the owner relationship. Any other relationship is
 * delegated to the <code>fulfills()</code> method of the resource being
 * protected.
 * <p>
 * To protect a method of a class a ResourcePermission is passed to the
 * AccessController (Core Java security class). For example, the Auction class
 * listed in the above policy file has the following constructor: <code>
 * 	public Auction() {
 *     Permission permission = 
 *            new DefaultResourceActionPermission(
 *                                 "com.fruits.jdkapi.security.jaas.Auction", 
 *                                 "create");    
 *     AccessController.checkPermission(permission);
 *  } 
 * </code> Since only the owner of an Auction instance can write to it, the
 * setter methods of the class look like the following: <code>
 * public void setName(String name) {
 *    Permission permission = 
 *            new DefaultResourceActionPermission(
 *                                  "com.fruits.jdkapi.security.jaas.Auction", 
 *                                  "write", this); 
 *    AccessController.checkPermission(permission);
 *    this.name = name;
 * }
 * </code> The this reference passed in to the ResourcePermission constructor
 * represents the Resource interface that the Auction class implements. Since
 * the relationship listed in the policy file is owner, the ResourcePermission
 * uses this reference to check if the current Subject (user) has a principal
 * that matches the owner of the instance. If another relationship is specified
 * then the ResourcePermission calls the Auction class
 * <code>fulfills(Subject subject, String relationship)</code> method. It is up
 * to the Resource implementing class to provide this logic. For example, the
 * Bid class listed in the policy file has the following methods: <code>
 * public void setAccepted(boolean flag) {
 *    Permission permission = 
 *            new DefaultResourceActionPermission(
 *                                  "com.fruits.jdkapi.security.jaas.Bid", 
 *                                  "accept", this); 
 *    AccessController.checkPermission(permission);
 * }
 * 
 * public boolean fulfills(Subject user, String relationship) { 
 *    if(relationship.equalsIgnoreCase("auctionOwner")) {
 *       String owner = auction.getOwner();
 *       Iterator principalIterator = user.getPrincipals().iterator();
 *       while (principalIterator.hasNext()) {
 *          Principal principal = (Principal)principalIterator.next();
 *          if(principal.getName().equals(owner))
 *             return true;
 *       }
 *    }
 *    return false;										
 * }
 * </code> The relationship String passed in to the fulfills() method is the
 * relationship listed in the policy file. In this case, the "auctionOwner"
 * String is used.
 * <p>
 * By defualt, this class looks for an XML file named ResourcePolicy.xml in the
 * current directory. The system property,
 * <code>com.fruits.jdkapi.security.jaas.policy</code>, may be used to specify another file.
 * 
 * @see javax.security.auth.Policy
 */
/**
 * @author jastang
 *
 */
public class XMLPolicyFile extends javax.security.auth.Policy {
	private String filename = JAASConstants.XML_POLICY_FILE_NAME;
	private Document doc = null;

	/**
	 * Creates a new SamplePolicy
	 */
	public XMLPolicyFile() {
		refresh();
	}

	/**
	 * Creates a DOM tree document from the default XML file or from the file
	 * specified by the system property,
	 * <code>com.fruits.jdkapi.security.jaas.policy</code>. This DOM tree
	 * document is then used by the <code>getPermissions()</code> in searching
	 * for permissions.
	 * 
	 * @see javax.security.auth.Policy#refresh()
	 */
	public void refresh() {
		String file = System.getProperty("com.fruits.jdkapi.security.jaas.policy");
		if (file != null)
			this.filename = file;

		FileInputStream fis = null;
		try {
			// Set up a DOM tree to query
			fis = new FileInputStream(this.filename);
			InputSource in = new InputSource(fis);
			DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
			dfactory.setNamespaceAware(true);
			doc = dfactory.newDocumentBuilder().parse(in);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Retrieve the Permissions granted to the Principals of the specified
	 * Subject associated with the specified CodeSource.
	 * 
	 * @param subject
	 *            the Subject whose associated Principals and the specified
	 *            CodeSource is used to determine permissions returned by this method.
	 * 
	 * @param codeSource
	 *            the location specified by this CodeSource and the Principals
	 *            associated with the specified Subject is used to determine the
	 *            permissions returned by this method. This parameter may be null.
	 * 
	 * @return the Collection of Permissions granted to the Subject and
	 *         CodeSource location specified in the provided subject and
	 *         codeSource parameters.
	 * 
	 * @see javax.security.auth.Policy#getPermissions(Subject, CodeSource)
	 */
	public PermissionCollection getPermissions(Subject subject, CodeSource codeSource) {
		Permissions collection = new Permissions();
		try {
			// Iterate through all of the subjects principals
			Iterator it = subject.getPrincipals().iterator();
			while (it.hasNext()) {
				Principal principal = (Principal) it.next();
				// Set up the xpath string to retrieve all the relevant permissions
				// Sample xpath string:
				// "/policy/grant[@codebase=\"*.jar\"]/principal[@classname=\"*\"][@name=\"*\"]/permission"
				StringBuffer xpath = new StringBuffer();

				xpath.append("/policy/grant/principal[@classname=\"");
				// xpath.append("/policy/grant[@codebase=\"");
				// String file = codeSource.getLocation().getFile();
				// xpath.append(file.substring(file.lastIndexOf("/") + 1));
				// xpath.append("\"]/principal[@classname=\"");
				xpath.append(principal.getClass().getName());
				xpath.append("\"][@name=\"");
				xpath.append(principal.getName());
				xpath.append("\"]/permission");

				NodeIterator nodeIter = XPathAPI.selectNodeIterator(doc, xpath.toString());
				Node node = null;
				while ((node = nodeIter.nextNode()) != null) {
					CodeSource codebase = getCodebase(node.getParentNode().getParentNode());
					// If the codebase equals to null then no codebase was specified in the XML file.
					// If the codebase is not null then check if it implies the codesource specified with the current request.
					if (codebase == null || codebase.implies(codeSource)) {
						Permission permission = getPermission(node);
						collection.add(permission);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		if (collection != null)
			return collection;
		else {
			// If the permission is not found here then delegate it to the standard java Policy class instance.
			java.security.Policy policy = java.security.Policy.getPolicy();
			return policy.getPermissions(codeSource);
		}
	}

	/**
	 * Returns a Permission instance defined by the provided permission Node
	 * attributes.
	 */
	private Permission getPermission(Node node) throws Exception {
		NamedNodeMap map = node.getAttributes();
		Attr attrClassname = (Attr) map.getNamedItem("classname");
		Attr attrName = (Attr) map.getNamedItem("name");
		Attr attrActions = (Attr) map.getNamedItem("actions");
		Attr attrRelationship = (Attr) map.getNamedItem("relationship");

		if (attrClassname == null)
			throw new RuntimeException();

		Class[] types = null;
		Object[] args = null;

		// Check if the name is specified 
		// if no name is specified then because the types and the args variables above are null the default constructor is used.
		if (attrName != null) {
			String name = attrName.getValue();
			// Check if actions are specified then setup the array sizes accordingly
			if (attrActions != null) {
				String actions = attrActions.getValue();
				// Check if a relationship is specified then setup the array sizes accordingly
				if (attrRelationship == null) {
					types = new Class[2];
					args = new Object[2];
				} else {
					types = new Class[3];
					args = new Object[3];
					String relationship = attrRelationship.getValue();
					types[2] = relationship.getClass();
					args[2] = relationship;
				}

				types[1] = actions.getClass();
				args[1] = actions;

			} else {
				types = new Class[1];
				args = new Object[1];
			}

			types[0] = name.getClass();
			args[0] = name;
		}

		String classname = attrClassname.getValue();
		Class permissionClass = Class.forName(classname);
		Constructor constructor = permissionClass.getConstructor(types);
		return (Permission) constructor.newInstance(args);
	}

	/**
	 * Returns a CodeSource object defined by the provided grant Node attributes.
	 */
	private CodeSource getCodebase(Node node) throws Exception {
		CodeSource codebase = null;
		if (node.getNodeName().equalsIgnoreCase("grant")) {
			NamedNodeMap map = node.getAttributes();

			Attr attrCodebase = (Attr) map.getNamedItem("codebase");
			if (attrCodebase != null) {
				String codebaseValue = attrCodebase.getValue();
				codebase = new CodeSource(new URL(codebaseValue), new CodeSigner[0]); // 2nd parameter
			}
		}

		return codebase;
	}

}
