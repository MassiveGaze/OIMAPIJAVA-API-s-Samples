package com.massiveGaze.orgs;

import java.util.HashMap;

import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.platform.OIMClient;

import com.massiveGaze.connection.OIMConnection;
import com.massiveGaze.password.PasswordPolicy;

public class UpdateOrganizations {
	protected static OIMClient platform = OIMConnection.getConnection();
	protected static OrganizationManager m_orgManagerService =(OrganizationManager) platform.getService(OrganizationManager.class);
	public static String updateOrgPasswordPolicy(String orgKey) throws Exception {
	    String result =null;
	    HashMap<String, Object> updateAttributes = new HashMap<String, Object>();
	    updateAttributes.put(OrganizationManagerConstants.AttributeName.ORG_PASSWORD_POLICY_KEY.getId(),PasswordPolicy.passwordPolicyCreate());
		//updateAttributes.put(OrganizationManagerConstants.AttributeName.ORG_PASSWORD_POLICY_NAME.getId(), "PasswordPolicy90");
	    result= m_orgManagerService.modify(new Organization(orgKey,updateAttributes));	  
	    System.out.println("updateOrgPasswordPolicy with result -> "+result);
	    return orgKey;
	  }	
	
}
