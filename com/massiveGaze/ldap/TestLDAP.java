package com.massiveGaze.ldap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.massiveGaze.connection.Platform;
import com.massiveGaze.ldap.LDAPConnection;

import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.api.OrganizationManagerConstants;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.usermgmt.vo.UserManagerResult;
import oracle.iam.identity.utils.Constants;
import oracle.iam.passwordmgmt.api.PasswordMgmtService;
import oracle.iam.passwordmgmt.vo.PasswordPolicyInfo;



public class TestLDAP {
	static PasswordMgmtService passwordmgmtService=Platform.getService(PasswordMgmtService.class);
	static OrganizationManager m_orgManagerService =Platform.getService(OrganizationManager.class);
	static UserManager usrMgmnt = Platform.getService(UserManager.class);
	static DirContext ctx=null;
	public static void main(String[] args) {
		
		try {
			new TestLDAP().pwdNeverExpireForPwdPolicyAsExpireDaysAfterIsNull();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void pwdNeverExpireForPwdPolicyAsExpireDaysAfterIsNull() throws Exception {
		//CreatePasswordPolicy
		int i=5;
		ctx = (DirContext) LDAPConnection.getLDAPConnection();
		String policyName = "PasswordPolicy9"+i ; 
        PasswordPolicyInfo passPolicyInfo = new PasswordPolicyInfo();
        passPolicyInfo.setName(policyName);
        passPolicyInfo.setShortDesc(policyName + " description");
        passPolicyInfo.setPasswordExpiresAfterInDays(90);
        passPolicyInfo.setPriority(10000);            
        PasswordPolicyInfo searchPassPolicyInfo = new PasswordPolicyInfo();
        passPolicyInfo = passwordmgmtService.create(passPolicyInfo);        
        searchPassPolicyInfo = passwordmgmtService.getDetails(passPolicyInfo.getName());  
        System.out.println("Password Policy Created with id "+passPolicyInfo.getId());
       
        System.out.println(" Search Password Policy Created with id "+searchPassPolicyInfo.getId());
      
        //Create Organization
		Organization org = new Organization();
		org.setAttribute("Organization Name", "myorg"+i);
		org.setAttribute("Organization Customer Type", "Department");
		org.setAttribute("Organization Status", "Active");		
		String orgKey = m_orgManagerService.create(org);	
		System.out.println("Organization Created with ID "+orgKey);
        //Update Organization With PasswordPolicy Key
	    HashMap<String, Object> updateAttributes = new HashMap<String, Object>();
	    updateAttributes.put(OrganizationManagerConstants.AttributeName.ORG_PASSWORD_POLICY_KEY.getId(),passPolicyInfo.getId());
	    orgKey= m_orgManagerService.modify(new Organization(orgKey,updateAttributes));	
	    System.out.println(" Modified Organization Adding Password Policy for Org ID "+orgKey);
        //CreateUser with Above Created Organization
	    HashMap<String, Object> createAttributes = new HashMap<String, Object>();
		String userId =""+i;
	    createAttributes.put(Constants.USERID, "TUSER" + userId);
		createAttributes.put(Constants.FIRSTNAME, "TEST" + userId);
		createAttributes.put(Constants.LASTNAME, "USER" + userId);
		createAttributes.put(Constants.ORGKEY, Long.parseLong(orgKey));
		createAttributes.put(Constants.PASSWORD, "Welcome1");
		createAttributes.put(Constants.EMAIL, "TUSER" + userId+ "@oracle.com");
		createAttributes.put(Constants.EMPTYPE, "Full-Time");
		createAttributes.put(Constants.USERTYPE,"End-User Administrator");				
		UserManagerResult result = usrMgmnt.create(new User(null,createAttributes));
	    //Update setPasswordExpiresAfterInDays to Null for Password Never Expire
		System.out.println(" User Created with ID "+   result.getEntityId());
		PasswordPolicyInfo passPolicyObjAsNull = new PasswordPolicyInfo();
		passPolicyObjAsNull.setPasswordExpiresAfterInDays(null);
		passPolicyObjAsNull.setName("PasswordPolicy9"+i);
		passwordmgmtService.update(passPolicyObjAsNull);
		System.out.println(" Updated Password Policy to Null  ");
		 //Change Password to Update USR_PWD_EXPIRE_DATE Column		
		usrMgmnt.changePassword(result.getEntityId(),"Pass_12345".toCharArray() , false);
		System.out.println(" Chnaged User Password for User +  "+result.getEntityId());
		//get USR_PWD_EXPIRE_DATE value
		Set<String> searchAttrs = new HashSet<String>();		
		searchAttrs.add("First Name");
		searchAttrs.add("Last Name");		
		searchAttrs.add("usr_pwd_expire_date");
		User userLookup = usrMgmnt.getDetails(result.getEntityId(), searchAttrs, false);
		String usr_pwd_expire_date=(String)userLookup.getAttributes().get("usr_pwd_expire_date");
		System.out.println(" usr_pwd_expire_date  From DB ->  "+usr_pwd_expire_date);
		//Get LDAP Attributes for the created User.
		String SearchCtrlString = "(&(objectclass=inetOrgPerson)(uid="+"MBANAPPA" + userId + "))";
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration results = ctx.search(LDAPConnection.getUserContainer(), SearchCtrlString, controls);
		String obPasswordExpiryDate=null;
		while (results.hasMore()) {
			SearchResult searchResult = (SearchResult) results.next();
			Attributes attributes = searchResult.getAttributes();
			Attribute attrObPWDExpiry = attributes.get("obpasswordexpirydate");
			if(attrObPWDExpiry==null){
				System.out.println(" attrObPWDExpiry is NUll   ");
					attrObPWDExpiry=attributes.get("orclpwdexpirationdate");
			}
			if(attrObPWDExpiry!=null)
            obPasswordExpiryDate = (String) attrObPWDExpiry.get();                 
		
			System.out.println(" obPasswordExpiryDate is    "+obPasswordExpiryDate);
			System.out.println(" usr_pwd_expire_date is "+usr_pwd_expire_date);
		}
		/*usrMgmnt.delete(result.getEntityId(), false);
	    m_orgManagerService.delete(orgKey, false);
        passwordmgmtService.delete(policyName);*/
	}
}
