package com.fairchild.jdkapi.security.jaas;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;

/**
 * This LoginModule example authenticates users with a password. 
 * Users and passwords are stored in an XML file like the following example:
 * 
 * <pre>
 * <?xml version="1.0"?>
 * <users>
 * 	  <user uid="admin"  password="passw0rd" />
 *    <user uid="jane"   password="passw0rd" />
 *    <user uid="john"   password="passw0rd" />
 *    <user uid="lou"    password="passw0rd" />	
 * </users>
 * </pre>
 * 
 * When a user successfully authenticates a ExamplePrincipal instance with the
 * uid as the name is created and added to the users Subject object.
 * <p>
 * This LoginModule also uses a user's group XML file like the following example:
 * 
 * <pre>
 * <?xml version="1.0"?>
 * <groups>
 *    <group gid="administrators" users="admin" />
 *    <group gid="users"     users="jane,admin,john,lou" />
 *    <group gid="managers"  users="lou" />
 * </groups>
 * </pre>
 * 
 * A ExamplePrincipal is created with the gid as the name for each group a user is a member when authentication is successful.
 * <p>
 * Both the users and the user's group file are specified as options in the login configuration file. For example,
 * 
 * <pre>
 * Sample {
 *   com.fairchild.jdkapi.security.jaas.ExampleLoginModule required debug=true userFile="users.xml" groupFile="groups.xml";
 * };
 * </pre>
 * <p>
 * The LoginModule also recognizes the debug option.
 * 
 * @see java.security.Principal
 * @see javax.security.auth.Subject
 * @see com.fairchild.jdkapi.security.jaas.ExamplePrincipal
 */
public class ExampleLoginModule implements LoginModule {
	private Subject subject;
	private CallbackHandler callbackHandler;
	private Map sharedState;
	private Map options;
	private boolean debug = false;

	private Document userDoc = null;
	private Document groupDoc = null;

	private String username;
	private char[] password;
	
	private boolean succeeded = false;
	private boolean commited = false;
	/**
	 * Initializes the <code>LoginModule</code>.
	 *
	 * @param subject
	 *            the <code>Subject</code> to be authenticated.
	 *
	 * @param callbackHandler
	 *            a <code>CallbackHandler</code> for prompting and retrieving
	 *            the userid and password from the user.
	 *
	 * @param sharedState
	 *            shared <code>LoginModule</code> state.
	 *
	 * @param options
	 *            options specified in the login configuration file for this
	 *            <code>LoginModule</code>.
	 */
	public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		this.sharedState = sharedState;
		this.options = options;

		// initialize configuration options
		debug = "true".equalsIgnoreCase((String) options.get("debug"));
		String userFile = (String) options.get("userFile");
		String groupFile = (String) options.get("groupFile");

		if (userFile == null)
			throw new InvalidParameterException("No userFile option specified in the configuration file for this LoginModule.");

		if (groupFile == null)
			throw new InvalidParameterException("No groupFile option specified in the configuration file for this LoginModule.");

		if (debug) {
			System.out.println("Users file: " + userFile);
			System.out.println("Group file: " + groupFile);
		}

		// Setup the users XML DOM tree used for quering
		FileInputStream fis = null;
		try {
			fis = new FileInputStream((String) options.get("userFile"));
			InputSource in = new InputSource(fis);
			DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
			dfactory.setNamespaceAware(true);
			userDoc = dfactory.newDocumentBuilder().parse(in);
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

		// Setup the group XML DOM tree used for retrieving the groups a user is a member of.
		try {
			fis = new FileInputStream((String) options.get("groupFile"));
			InputSource in = new InputSource(fis);
			DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
			dfactory.setNamespaceAware(true);
			groupDoc = dfactory.newDocumentBuilder().parse(in);
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
	 * Prompts the user for a username and password.
	 *
	 * @return true if the authentication succeeded, or false if this
	 *         LoginModule should be ignored
	 *
	 * @exception FailedLoginException
	 *                if the authentication fails.
	 *
	 * @exception LoginException
	 *                if the <code>LoginModule</code> is unable to authenticate.
	 */
	public boolean login() throws LoginException {
		if (callbackHandler == null)
			throw new LoginException("Error: CallbackHandler cannot be null");

		Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("userid: ");
		callbacks[1] = new PasswordCallback("password: ", false);

		try {
			callbackHandler.handle(callbacks);
			username = ((NameCallback) callbacks[0]).getName();
			char[] tmpPassword = ((PasswordCallback) callbacks[1]).getPassword();

			if (tmpPassword == null) {
				// treat a NULL password as an empty password
				tmpPassword = new char[0];
			}
			password = new char[tmpPassword.length];
			System.arraycopy(tmpPassword, 0, password, 0, tmpPassword.length);
			((PasswordCallback) callbacks[1]).clearPassword();
		} catch (java.io.IOException e) {
			throw new LoginException(e.getMessage());
		} catch (UnsupportedCallbackException e) {
			throw new LoginException("Error: " + e.getMessage());
		}

		if (debug) {
			System.out.println("LoginModuleExample: username = " + username);
			System.out.println("LoginModuleExample: password = " + String.valueOf(password));
		}

		// Check the username and password
		if (isValidUser(username, password)) {
			// authentication succeeded
			if (debug)
				System.out.println("LoginModuleExample: authentication succeeded.");
			succeeded = true;
			return true;
		} else {
			// authentication failed
			if (debug)
				System.out.println("LoginModuleExample: authentication failed.");
			succeeded = false;
			// clear the values
			username = null;
			password = null;
			throw new FailedLoginException("Invalid username or password");
		}
	}

	/**
	 * This method is called if the LoginContext's overall authentication
	 * succeeded (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules succeeded).
	 * <p>
	 * If this LoginModule's own authentication attempt succeeded (checked by
	 * retrieving the private state saved by the <code>login</code> method),
	 * then this method associates the relevant <code>ExamplePrincipal</code>
	 * with the <code>Subject</code> located in the <code>LoginModule</code>. 
	 * If this LoginModule's own authentication attempted failed, then this method
	 * removes any state that was originally saved.
	 *
	 * @exception LoginException
	 *                if the commit fails.
	 *
	 * @return true if this LoginModule's own login and commit attempts  succeeded, or false otherwise.
	 */
	public boolean commit() throws LoginException {
		if (!succeeded) return succeeded;

		subject.getPrincipals().add(new ExamplePrincipal(username));
		subject.getPrincipals().addAll(getUserGroups(username));

		username = null;
		password = null;

		commited = true;
		return true;
	}

	/**
	 * This method is called if the LoginContext's overall authentication
	 * failed. (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules did not succeed).
	 * <p>
	 * If this LoginModule's own authentication attempt succeeded (checked by
	 * retrieving the private state saved by the <code>login</code> and
	 * <code>commit</code> methods), then this method cleans up any state that
	 * was originally saved.
	 *
	 * @exception LoginException
	 *                if the abort fails.
	 *
	 * @return false if this LoginModule's own login and/or commit attempts
	 *         failed, and true otherwise.
	 */
	public boolean abort() throws LoginException {
		if (!succeeded)
			return succeeded;

		if (succeeded && !commited) {
			// login succeeded but overall authentication failed
			succeeded = false;
			username = null;
			password = null;
		} else {
			// overall authentication succeeded and commit
			// succeeded, but someone else's commit failed.
			logout();
		}

		return true;
	}

	/**
	 * Logouts a Subject.
	 * <p>
	 * This method removes the <code>ExamplePrincipal</code> instances that were
	 * added by the <code>commit</code> method.
	 *
	 * @exception LoginException
	 *                if the logout fails.
	 *
	 * @return true if this method succeeded, or false if this LoginModule
	 *         should be ignored.
	 */
	public boolean logout() throws LoginException {
		subject.getPrincipals().clear();
		succeeded = false;
		succeeded = commited;
		username = null;
		password = null;

		return true;
	}

	/**
	 * Searches the users XML file for the specified username and password.
	 */
	private boolean isValidUser(String username, char[] password) {
		try {
			// Set up the xpath string to retrieve the user info.
			StringBuffer xpath = new StringBuffer();
			xpath.append("/users/user[@uid=\"");
			xpath.append(username);
			xpath.append("\"][@password=\"");
			xpath.append(new String(password));
			xpath.append("\"]");

			NodeIterator nodeIter = XPathAPI.selectNodeIterator(userDoc, xpath.toString());
			if ((nodeIter.nextNode()) != null)
				return true;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return false;
	}

	/**
	 * Searches the user's group XML file and returns a collection of
	 * PrincipalExamples for each group a user is a member of.
	 */
	private Collection getUserGroups(String username) {
		Collection collection = new ArrayList();

		try {
			// Set up the xpath string to retrieve the user info
			StringBuffer xpath = new StringBuffer();
			xpath.append("/groups/group[true()=contains(@users,\"");
			xpath.append(username);
			xpath.append("\")]");

			// Create a new PrincipalExample for
			// each group the user is a member of.
			NodeIterator nodeIter = XPathAPI.selectNodeIterator(groupDoc, xpath.toString());
			Node node = null;
			while ((node = nodeIter.nextNode()) != null)
				collection.add(getGroupPrincipal(node));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		return collection;
	}

	/**
	 * Returns a PrincipalExample for the specified (Group) node.
	 */
	private Principal getGroupPrincipal(Node node) throws Exception {
		NamedNodeMap map = node.getAttributes();
		Attr attrGid = (Attr) map.getNamedItem("gid");
		return new ExamplePrincipal(attrGid.getValue());
	}
}