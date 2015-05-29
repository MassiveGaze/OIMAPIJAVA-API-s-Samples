package com.massiveGaze.reconciliation;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

import oracle.core.ojdl.logging.ODLLogger;
import oracle.iam.platform.Platform;
import oracle.iam.reconciliation.api.ChangeType;
import oracle.iam.reconciliation.api.EventAttributes;
import oracle.iam.reconciliation.api.ReconOperationsService;
import oracle.iam.scheduler.vo.TaskSupport;
import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;
import Thor.API.Operations.tcUserOperationsIntf;

import com.identityforge.racf.RacfItResource;
import com.identityforge.racf.RacfLookupMaps;
import com.identityforge.racf.RacfReconTaskAttrs;
import com.identityforge.racf.util.LdapOperationsImpl;

/**
 * ReconcileAllUsersTask.java
 * 
 * @author $Author: Anna Hix $
 */

public class ReconcileAllUsersTask extends TaskSupport {

	private RacfReconTaskAttrs reconAttrs;
	private RacfItResource racfItResource;
	private RacfLookupMaps lookupMaps;

	public static final String ORGNAME_METADATA = "Organization Name";

	public static final String USRROLE_METADATA = "Role";

	public static final String USRTYPE_METADATA = "Xellerate Type";

	public static final String ITRESOURCE_METADATA = "IT Resource";
	protected ReconOperationsService reconIntf;
	protected tcUserOperationsIntf userIntf;
	private LdapOperationsImpl ldapOp;
	private String ldapHost;
	private String ldapPort;
	private String ldapRootContextDn;
	private String ldapPrincipalDn;
	private String ldapPrincipalwd;
	private String ssl;
	private String trustStore;
	private String trustStorePassword;
	private String trustStoreType;
	static Properties props = null;

	private tcITResourceInstanceOperationsIntf resOpsIntf;

	private static ODLLogger oimlogger = ODLLogger.getODLLogger("com.identityforge.racf.tasks.ReconcileAllUsersTask");
	private static final String CURRENT_CLASS_NAME = "ReconcileAllUsersTask " + " ----> ";
	private HashMap theMap = null;

	/**
	 * Main method
	 */
	public void execute(HashMap map) {

		oimlogger.fine(CURRENT_CLASS_NAME + "~~~~~~~~~~Entering execute()~~~~~~~~~~");
		this.theMap = map;
		initialize();

		try {
			reconcileAllUsers();

		} catch (Exception e) {
			e.printStackTrace();
		}

		oimlogger.fine(CURRENT_CLASS_NAME + "~~~~~~~~~~Leaving execute()~~~~~~~~~~");

	}

	public void initialize() {

		// BEGIN INITILIAIZATION!

		oimlogger.fine(CURRENT_CLASS_NAME + "~~~~~~~~~~Entering initialize()~~~~~~~~~~");

		reconAttrs = new RacfReconTaskAttrs();
		racfItResource = new RacfItResource(this);
		lookupMaps = new RacfLookupMaps(this);

		// Get the reconciliation task attributes
		getReconTaskAttributes();

		// Get server properties
		// Initialize the RACF IT Resource instance parameters
		racfItResource.initialize(reconAttrs.getItResource());

		// Initialize the LDAP Controller
		try {

			ldapHost = racfItResource.getLdapHost();
			ldapPort = racfItResource.getLdapPort();
			ldapRootContextDn = racfItResource.getRootContext();
			ldapPrincipalDn = racfItResource.getDn();
			ldapPrincipalwd = racfItResource.getPassword();
			ssl = racfItResource.getSsl();
			trustStore = racfItResource.getTrustStore();
			trustStorePassword = racfItResource.getTrustStorePassword();
			trustStoreType = racfItResource.getTrustStoreType();

			// ###############################################
			// #### START DEBUG LOGGING ######################
			// ###############################################
			oimlogger.fine("ReconcileAllUsersTask Parameter Variables passed are: " + "pServerName = [" + ldapHost + "], "
					+ "pPort = [" + ldapPort + "], " + "pRootContext = [" + ldapRootContextDn + "], " + "pPrincipalDN = ["
					+ ldapPrincipalDn + "], " + "pPrincipalPwd = [**********], ssl = [" + ssl + "], " + "trustStore = ["
					+ trustStore + "], " + "trustStorePassword = [" + trustStorePassword + "], " + "trustStoreType = ["
					+ trustStoreType + "], ] ");

			if (ldapHost == null || ldapPort == null || ldapRootContextDn == null || ldapPrincipalDn == null
					|| ldapPrincipalwd == null || ssl == null)
				throw new Exception("Created With Null Arguments");

			if (ldapHost.trim().length() == 0 || ldapPort.trim().length() == 0 || ldapRootContextDn.trim().length() == 0
					|| ldapPrincipalDn.trim().length() == 0 || ldapPrincipalwd.trim().length() == 0

			) {
				throw new Exception("Created With empty Arguments");
			}

			try {

				// ############################################################
				// #### CREATE LDAP INSTANCE WITH CONNECTION INFORMATION ######
				// ############################################################
				ldapOp =
						new LdapOperationsImpl(ldapHost, ldapPort, ldapRootContextDn, ldapPrincipalDn, ldapPrincipalwd, ssl,
								trustStore, trustStorePassword, trustStoreType);

			} catch (Exception e) {
				oimlogger.severe("ReconcileAllUsersTask Error while getting LdapOperationsImpl instance...  " + e.getMessage());
				e.printStackTrace();
				throw new Exception(e);
			}
		} catch (Exception e) {
			oimlogger.severe("Exception occured while instantiating LDAPOperations Controller:" + e.getMessage());

		}

		oimlogger.fine(CURRENT_CLASS_NAME + "~~~~~~~~~~Leaving initialize()~~~~~~~~~~");

	}

	/**
	 * Reconciles all users
	 */
	public void reconcileAllUsers() {

		oimlogger.fine(CURRENT_CLASS_NAME + "~~~~~~~~~~Entering reconcileAllUsers()~~~~~~~~~~");

		Enumeration<?> answer = null;
		Vector<String> listedUsers = null;
		boolean isIncrementalRecon = false;

		try {
			// ####################################################
			// #### CONNECT TO LDAP IDF-GATEWAY SERVER ############
			// ####################################################
			ldapOp.setLdapConnection();

			oimlogger.fine("About Search for All Users");

			// ####################################################
			// #### CODE FOR TESTING - COMMENT OUT FOR RELEASE ####
			// ####################################################
			String res = null;
			if (theMap.get("UsersList") != null) {
				res = theMap.get("UsersList").toString();
			}

			if (res != null && !res.equals("")) {
				isIncrementalRecon = true;
				String[] users = theMap.get("UsersList").toString().split(",");
				listedUsers = new Vector<String>(Arrays.asList(users));

				answer = listedUsers.elements();
			} else {

				answer = ldapOp.search("ou=People," + ldapRootContextDn, "objectClass", "*");
			}

			// ###################################################
			// ###################################################
			// ###################################################

			oimlogger.fine("Completed Search for All Users");

		} catch (Exception e) {
			oimlogger.severe("Error Getting All IDS");
			e.printStackTrace();
		}

		reconIntf = Platform.getService(ReconOperationsService.class);
		userIntf = Platform.getService(tcUserOperationsIntf.class);

		while (answer.hasMoreElements()) {

			try {
				oimlogger.fine("Looping through each ID");

				String attrValue = "";
				if (isIncrementalRecon)
					attrValue = answer.nextElement().toString();
				else {
					SearchResult result = (SearchResult) answer.nextElement();
					Attributes uattrs = result.getAttributes();

					attrValue = (String) uattrs.get("uid").get();
				}

				// Set user id to uppercase
				attrValue.toUpperCase();

				// Search for the individual user
				NamingEnumeration<?> singleUser = ldapOp.search("ou=People," + ldapRootContextDn, "uid", attrValue);

				if (singleUser == null) {
					oimlogger.severe("SEARCH RESULT FOR USER [" + attrValue + "] WAS NULL");
					continue;
				}

				SearchResult values = (SearchResult) singleUser.nextElement();
				Attributes attrs = values.getAttributes();

				if (getTrusted() == true) {

					String uid = (String) attrs.get("uid").get();

					if (uid.indexOf("\\") >= 0) {

						if (uid.indexOf("#") >= 0) {
							String newId = null;
							if (uid.indexOf("\\") == 0) {
								newId = uid.substring(1);
								uid = newId;

								oimlogger.fine("NEW ID: " + newId);
							} else {
								String idf1 = uid.substring(0, uid.indexOf("\\"));
								String idf2 = uid.substring(uid.indexOf("\\") + 1);
								newId = idf1 + idf2;
								uid = newId;
								oimlogger.fine("NEW ID: " + newId);
							}
						}
					}

					if (attrs.get("uid") != null) {

						if (theMap.get("uidcase") != null) {

							String uidcase = theMap.get("uidcase").toString();
							if (uidcase.equalsIgnoreCase("upper")) {
								String newUid = uid.toUpperCase();
								attrs.put("uid", newUid);
							} else {
								String newUid = uid.toLowerCase();
								attrs.put("uid", newUid);
							}
						}

					}

					// ####################################################
					// #### MUST RECON USER IN XELLERATE FIRST ############
					// ####################################################
					if (reconcileXellerateUser(getReconXellerateUserMap(attrs)) == true) {

						oimlogger.info("Reconciled Xellerate User..." + attrValue);

						// ##########################################
						// #### CREATE NEW IDF RESOURCE FOR #########
						// #### THE NEW XELLERATE USER ##############
						// ##########################################
						oimlogger.fine("Starting Reconciliation For User..." + attrValue);

						if (reconcileIdfResource(attrs) == true) {
							oimlogger.fine("Reconciled User..." + attrValue);
						} else {
							oimlogger.severe("Error reconciling User..." + attrValue);
							throw new NamingException("Error reconciling User..." + attrValue);
						}

					}

				} else {

					// ###############################################
					// #### RECONCILE IDF RESOURCE ###################
					// ###############################################
					oimlogger.fine("Starting Non-Trusted Reconciliation For User..." + attrValue);

					if (reconcileIdfResource(attrs) == true) {
						oimlogger.info("Reconciled User..." + attrValue);
					} else {
						oimlogger.info("Error Reconciling User..." + attrValue);
					}
				}

			} catch (Exception e) {
				oimlogger.severe("Error within reconcile()");
				e.printStackTrace();
			}
		}

		try {
			// // Call ReconOperationsService.callingEndOfJobAPI() to process the recon events
			// oimlogger.fine("Preparing to call callingEndOfJobAPI()...");
			// reconIntf.callingEndOfJobAPI();

			// Disconnect the LDAP connection
			oimlogger.fine("Disconnecting connection to LDAP");
			ldapOp.disconnectLdapConnection();
		} catch (Exception ee) {
			oimlogger.fine("Error getting disconnecting Connection: " + ee.toString());
		}

		oimlogger.fine(CURRENT_CLASS_NAME + "~~~~~~~~~~Leaving reconcileAllUsers()~~~~~~~~~~");
	}

	/**
	 * @param xlAttributes
	 * @return
	 */
	public boolean reconcileXellerateUser(Map<String, Object> xlAttributes) {

		oimlogger.fine(CURRENT_CLASS_NAME + "~~~~~~~~~~Entering reconcileXellerateUser(Map attributes)~~~~~~~~~~");

		try {
			String objectName = getReconAttrs().getTrustedResourceObjectName();

			// Create the EventAttributes object
			EventAttributes reconEventAttributes = new EventAttributes();

			// No child data, so we close the event
			reconEventAttributes.setEventFinished(true);

			if (reconIntf.ignoreEvent(objectName, xlAttributes) == false) {
				long eventKey = reconIntf.createReconciliationEvent(objectName, xlAttributes, reconEventAttributes);
				oimlogger.info("Xellerate User reconciled... userKey: " + eventKey);
			}

		} catch (Exception e) {
			oimlogger.severe("Error reconciling Xellerate User..." + e.toString());
			oimlogger.fine(CURRENT_CLASS_NAME + "~~~~~~~~~~Leaving reconcileXellerateUser(Map)~~~~~~~~~~");
			return false;
		}
		oimlogger.fine(CURRENT_CLASS_NAME + "~~~~~~~~~~Leaving reconcileXellerateUser(Map)~~~~~~~~~~");

		return true;
	}

	/**
	 * Creates new IdentityForge resource (i.e., Racf, Top Secret, or ACF2 )
	 * 
	 * @param userAttrs
	 *            <code>Attributes</code>
	 * @return true/false <code>boolean</code>
	 */
	public boolean reconcileIdfResource(Attributes userAttrs) {

		oimlogger.fine(CURRENT_CLASS_NAME + "~~~~~~~~~~Entering reconcileIdfResource(Attributes attrs)~~~~~~~~~~");

		long eventKey = -1;

		try {

			String objectName = getReconAttrs().getResourceObjectName();
			oimlogger.fine("Using resource object... " + objectName);

			// ################################################################################ //
			// #### PERFORM SINGLE VALUED RECON FIRST ######################################### //
			// ################################################################################ //
			Map<String, Object> idfSingleAttrs = getIdfResourceSingleMap(userAttrs);

			if (userAttrs.get("revoke") != null) {

				String revoke = (String) userAttrs.get("revoke").get();

				if (revoke.equalsIgnoreCase("yes") || revoke.equalsIgnoreCase("y")) {
					idfSingleAttrs.put("Status", "Disabled");
				} else {
					idfSingleAttrs.put("Status", "Enabled");
				}

			}

			if (userAttrs.get("uid") != null) {

				String uid = (String) userAttrs.get("uid").get();

				if (uid.indexOf("\\") >= 0) {

					if (uid.indexOf("#") >= 0) {
						String newId = null;
						if (uid.indexOf("\\") == 0) {
							newId = uid.substring(1);
							uid = newId;

							oimlogger.fine("NEW ID: " + newId);
						} else {
							String idf1 = uid.substring(0, uid.indexOf("\\"));
							String idf2 = uid.substring(uid.indexOf("\\") + 1);
							newId = idf1 + idf2;
							uid = newId;
							oimlogger.fine("NEW ID: " + newId);
						}
					}
				}

				if (theMap.get("uidcase") != null) {

					String uidcase = theMap.get("uidcase").toString();
					if (uidcase.equalsIgnoreCase("upper")) {
						String newUid = uid.toUpperCase();
						idfSingleAttrs.put("uid", newUid);
					} else {
						String newUid = uid.toLowerCase();
						idfSingleAttrs.put("uid", newUid);
					}
				}

			}

			// Get the specific IT resource server key
			long itResKey = -1;
			String itResource = getReconAttrs().getItResource();

			HashMap tempMap = new HashMap();
			tempMap.put("IT Resources.Name", itResource);

			try {
				resOpsIntf = Platform.getService(tcITResourceInstanceOperationsIntf.class);

				tcResultSet rs = resOpsIntf.findITResourceInstances(tempMap);
				rs.goToRow(0);
				itResKey = rs.getLongValue("IT Resource.Key");

			} catch (Exception ex) {
				oimlogger.severe("Error retrieving IT resource key within ReconcileAllUsersTask(): " + ex.toString());
				ex.printStackTrace();
			}

			oimlogger.fine("IT RESOURCE KEY = [" + itResKey + "]");

			// Is OIM R2?
			boolean usingR2 = false;
			if (theMap.get("R2") != null) {

				String r2value = theMap.get("R2").toString().trim();
				if (r2value.equalsIgnoreCase("true") || r2value.equalsIgnoreCase("t") || r2value.equalsIgnoreCase("yes")
						|| r2value.equalsIgnoreCase("y")) {
					usingR2 = true;
				}
			}

			// Create the EventAttributes object
			EventAttributes reconEventAttributes = new EventAttributes();

			// Keep the event open
			reconEventAttributes.setEventFinished(false);
			
			reconEventAttributes.setChangeType(ChangeType.CHANGELOG);

			// ignoreEvent() implementation
			boolean isParentDataUnchanged = reconIntf.ignoreEvent(objectName, idfSingleAttrs);
			boolean isChildDataUnchanged = true;
			boolean isReconNeeded = true;
			oimlogger.fine("ignoreEvent() returned " + Boolean.toString(isParentDataUnchanged));

			if (isParentDataUnchanged) {

				String[] multiAttrs = null;
				if (theMap.get("MultiValuedAttributes") != null) {
					multiAttrs = theMap.get("MultiValuedAttributes").toString().split(",", 0);
				}

				Map<String, String> childTableValues = null;

				for (int i = 0; i < multiAttrs.length; i++) { // Check ignoreEvent for each mutli-valued attribute
					String multiValueFieldName = multiAttrs[i]; // Get the attribute name
					Attribute attrib = null;

					if (multiValueFieldName.equalsIgnoreCase("memberOf"))
						attrib = userAttrs.get("groups");
					else
						attrib = userAttrs.get(multiValueFieldName);

					if (attrib != null) {
						attrib.remove("REVOKED");
						attrib.remove("revoked");
					}

					if (attrib != null && attrib.size() > 0) {

						NamingEnumeration<?> values = attrib.getAll();

						while (values.hasMoreElements()) {

							childTableValues = new HashMap<String, String>();
							String attr = (String) values.nextElement();

							oimlogger.fine("Adding attribute [" + attrib.getID() + "] with value [" + attr
									+ "] to the reconciliation event");

							childTableValues.put(multiValueFieldName, attr);
						}

						Map[] childTableMap = { childTableValues };

						isChildDataUnchanged =
								reconIntf
										.ignoreEventAttributeData(objectName, idfSingleAttrs, multiValueFieldName, childTableMap);
						oimlogger.fine("ignoreEventAttributeData() [" + multiValueFieldName + "] returned "
								+ Boolean.toString(isChildDataUnchanged));

						if (!isChildDataUnchanged)
							break;

					}
				}

				// If the child data changed, create a recon event, leave it open
				if (!isChildDataUnchanged) {
					try {
						eventKey = reconIntf.createReconciliationEvent(objectName, idfSingleAttrs, reconEventAttributes);
						oimlogger.fine("Recon event created with eventKey:" + eventKey);
					} catch (Exception e) {
						oimlogger.severe("Recon event creation failed --- " + e.getMessage());
					}
				} else {
					oimlogger.fine("IGNORING EVENT - No recon is needed for this event.");
					isReconNeeded = false;
				}

			} else {

				eventKey = reconIntf.createReconciliationEvent(objectName, idfSingleAttrs, reconEventAttributes);

				oimlogger.fine("User event key..." + eventKey);
			}

			if (isReconNeeded) {
				// ################################################################################ //
				// #### AVAILABLE MULTI-VALUED ATTRIBUTES ######################################### //
				// #### memberOf, attributes ###################################################### //
				// ################################################################################ //

				String[] multiAttrs = null;
				if (theMap.get("MultiValuedAttributes") != null) {
					multiAttrs = theMap.get("MultiValuedAttributes").toString().split(",", 0);
				}

				Map<String, String> childTableValues = null;

				for (int i = 0; i < multiAttrs.length; i++) {

					String multiAttrName = multiAttrs[i];
					oimlogger.fine("Processing multi-valued attributes...");
					Attribute attrib = null;

					if (multiAttrName.equalsIgnoreCase("memberOf"))
						attrib = userAttrs.get("groups");
					else
						attrib = userAttrs.get(multiAttrName);

					if (attrib != null) {
						attrib.remove("REVOKED");
						attrib.remove("revoked");
					}

					if (attrib != null && attrib.size() > 0) {

						NamingEnumeration<?> values = attrib.getAll();

						while (values.hasMoreElements()) {

							childTableValues = new HashMap<String, String>();
							String attr = "";

							if (usingR2) {
								attr = itResKey + "~" + values.nextElement().toString().trim();
							} else {
								attr = (String) values.nextElement();
							}

							oimlogger.info(
								"Adding [" + multiAttrName + "] with value [" + attr + "] to the reconciliation event");

							childTableValues.put(multiAttrName, attr);

							reconIntf.addDirectMultiAttributeData(eventKey, multiAttrName, childTableValues);

						}
						
					}
					
					reconIntf.providingAllMultiAttributeData(eventKey, multiAttrName, false);

				}
			}

			// Make sure the recon event was created
			if (eventKey > -1) {

				// ################################################################################ //
				// #### FINISH THE RECON FOR THIS PARTICULAR USER ################################# //
				// ################################################################################ //
				reconIntf.finishReconciliationEvent(eventKey);
			}

		} catch (Exception e) {
			e.printStackTrace();
			oimlogger.severe("Error reconciling User..." + e.toString());
			oimlogger.fine(CURRENT_CLASS_NAME + "~~~~~~~~~~Leaving reconcileIdfResource(Attributes)~~~~~~~~~~");
			return false;
		}

		oimlogger.fine(CURRENT_CLASS_NAME + "~~~~~~~~~~Leaving reconcileIdfResource(Attributes)~~~~~~~~~~");
		return true;
	}

	/**
	 * @param attrs
	 * @return
	 * @throws NamingException
	 */
	public Map<String, Object> getIdfResourceSingleMap(Attributes attrs) throws NamingException {

		oimlogger.fine(CURRENT_CLASS_NAME + "~~~~~~~~~~Entering getIdfResourceSingleMap(Attributes attrs)~~~~~~~~~~");

		HashMap<String, Object> xlMappings = new HashMap<String, Object>();
		xlMappings.put("IT", reconAttrs.getItResource());
		oimlogger.fine("IT Resource: [" + reconAttrs.getItResource() + "]");

		String[] singleAttrs = null;
		try {
			singleAttrs = getReconciliationAttributes();
		} catch (Exception e) {
			oimlogger.severe("Failed to retrieve reconciliation attributes from Scheduled Task form.");
			e.printStackTrace();
		}

		for (int i = 0; i < singleAttrs.length; i++) {

			String attrName = singleAttrs[i];

			if (attrs.get(attrName) != null) {

				if (attrName.equalsIgnoreCase("passwordExpire")) {
					String pwdExpire = attrs.get(attrName).get().toString();
					if (pwdExpire.equalsIgnoreCase("true"))
						xlMappings.put(attrName, "1");
					else
						xlMappings.put(attrName, "0");

				} else {
					String attrValue = attrs.get(attrName).get().toString();
					if (attrValue.indexOf("*NONE") >= 0)
						xlMappings.put(attrName, "");
					else
						xlMappings.put(attrName, attrValue);
				}
			} else {
				if (!attrName.equalsIgnoreCase("userPassword")) {
					oimlogger.fine("Did Not Receive attName: " + attrName);
					xlMappings.put(attrName, "");
				}
			}

		}

		oimlogger.fine(CURRENT_CLASS_NAME + "~~~~~~~~~~Leaving getIdfResourceSingleMap(Attributes)~~~~~~~~~~");
		return xlMappings;
	}

	protected void getReconTaskAttributes() {
		try {

			oimlogger.fine(CURRENT_CLASS_NAME + "~~~~~~~~~~Entering getReconTaskAttributes()~~~~~~~~~~");
			// These are required fields
			reconAttrs.setItResourceName(theMap.get("IT Resource").toString());

			String oimXellerateObject = theMap.get("Trusted Resource Object").toString();
			String racfResourceObject = theMap.get("Resource Object").toString();

			if (racfResourceObject == null || racfResourceObject.equals("false")) {
				reconAttrs.setResourceObjectName("");
			} else {
				reconAttrs.setResourceObjectName((String) racfResourceObject);
			}

			if (oimXellerateObject == null || oimXellerateObject.equals("false")) {
				reconAttrs.setTrustedResourceObjectName("");
			} else {
				reconAttrs.setTrustedResourceObjectName((String) oimXellerateObject);
			}

			// These are non-required fields

			String mva = theMap.get("MultiValuedAttributes").toString();

			if (mva != null && !mva.trim().equals("")) {
				reconAttrs.parseAndSetMultiValAttrs(mva);
			}

			oimlogger.fine(CURRENT_CLASS_NAME + "~~~~~~~~~~Leaving getReconTaskAttributes()~~~~~~~~~~");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * @return Returns the racfItResource.
	 */
	public RacfItResource getRacfItResource() {
		return racfItResource;
	}

	/**
	 * @param racfItResource
	 *            The racfItResource to set.
	 */
	public void setRacfItResource(RacfItResource adItRes) {
		this.racfItResource = adItRes;
	}

	/**
	 * @return Returns the lookupMaps.
	 */
	public RacfLookupMaps getLookupMaps() {
		return lookupMaps;
	}

	/**
	 * @param lookupMaps
	 *            The lookupMaps to set.
	 */
	public void setLookupMaps(RacfLookupMaps lookupMaps) {
		this.lookupMaps = lookupMaps;
	}

	/**
	 * @return Returns the reconAttrs.
	 */
	public RacfReconTaskAttrs getReconAttrs() {
		return reconAttrs;
	}

	/**
	 * @param reconAttrs
	 *            The reconAttrs to set.
	 */
	public void setReconAttrs(RacfReconTaskAttrs reconAttrs) {
		this.reconAttrs = reconAttrs;
	}

	/**
	 * Retrieves an array of Strings representing the attributes to be reconciled. Attributes are listed on the scheduled task
	 * form.
	 * 
	 * @return String array of reconciliation attributes
	 * @throws Exception
	 */
	public String[] getReconciliationAttributes() throws Exception {

		return theMap.get("SingleValueAttributes").toString().split(",", -1);
	}

	/**
	 * Retrieves whether trusted reconciliation is to be performed. Trusted reconciliation property is listed on the scheduled
	 * task form.
	 * 
	 * @return true/false
	 * @throws Exception
	 */
	public boolean getTrusted() throws Exception {

		boolean value = false;
		if ((theMap.get("TrustedReconciliation").toString().equalsIgnoreCase("true"))
				|| (theMap.get("TrustedReconciliation").toString().equalsIgnoreCase("y"))
				|| (theMap.get("TrustedReconciliation").toString().equalsIgnoreCase("yes")))
			value = true;

		return value;
	}

	/**
	 * @param attrs
	 * @return
	 * @throws NamingException
	 */
	public Map<String, Object> getReconXellerateUserMap(Attributes attrs) throws NamingException {

		Map<String, Object> xlMappings = new HashMap<String, Object>();

		xlMappings.put("uid", attrs.get("uid").get().toString());
		xlMappings.put("givenName", attrs.get("givenName").get().toString());
		xlMappings.put("sn", attrs.get("sn").get().toString());
		xlMappings.put("Role", "Full-Time");
		xlMappings.put("Xellerate Type", "End-User");
		xlMappings.put("Organization Name", "Xellerate Users");

		return xlMappings;

	}

	/**
	 * @param results
	 * @param columnName
	 * @return
	 * @throws tcColumnNotFoundException
	 * @throws tcAPIException
	 */
	public static long getKey(tcResultSet results, String columnName) throws tcColumnNotFoundException, tcAPIException {

		if (results.getRowCount() == 0) {
			throw new tcAPIException("ResultSet is empty");
		} else if (results.getRowCount() > 1) {
			throw new tcAPIException("ResultSet not unique");
		} else {
			results.goToRow(0);
			return results.getLongValue(columnName);
		}
	}

	public HashMap getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAttributes() {
		// TODO Auto-generated method stub

	}

}
