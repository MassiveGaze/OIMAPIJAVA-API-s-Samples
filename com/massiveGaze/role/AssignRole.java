package com.massiveGaze.role;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.identity.rolemgmt.vo.RoleManagerResult;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.usermgmt.vo.UserManagerResult;

import com.massiveGaze.connection.Platform;


 
public class AssignRole  {

  
    /* protected static OrganizationManager m_orgManagerService ; */
     /*
      * Initialize the context and login with client supplied environment
     */
public static void main(String [] args)
{
  try {
        System.out.println("Creating client....");
       
    String roleName = "helloRole1";
RoleManager roleMgr = Platform.getService(RoleManager.class);
RoleManagerResult roleResult = null;
HashMap<String, Object> createAttributes = new HashMap<String, Object>();
createAttributes.put("Role Name", roleName);
createAttributes.put("Role Display Name", roleName);
createAttributes.put("Role Description", roleName + " Description");
Role role = new Role(createAttributes);
roleResult = roleMgr.create(role);
String entityId = roleResult.getEntityId();
System.out.println("Created role with key = " + entityId);

/* Create 50 users with same firstname */
    UserManager userService = Platform.getService(UserManager.class);
	 for(int i=0;i<51;i++) {
	     String FirstName = "TEST";
	     String LastName = "USER"+i;
	     System.out.println("User First name is:" +FirstName );
	     System.out.println("User Last Name is :" +LastName);
	    HashMap<String, Object> userAttributeValueMap = new HashMap<String, Object>();
	    userAttributeValueMap.put("act_key", 1L);
	    userAttributeValueMap.put("First Name", FirstName);
	    userAttributeValueMap.put("Last Name", LastName);
	    userAttributeValueMap.put("Role","Full-Time");
	    userAttributeValueMap.put("Xellerate Type","End-User"); 
	    
	    UserManagerResult result = userService.create(new User(null, userAttributeValueMap)); 
	    String usr_key = result.getEntityId();
	    System.out.println("User key is :" +result.getEntityId());
	    
	    Set userKeys = new HashSet();
	    userKeys.add(usr_key);
	    System.out.print("Timestamp before calling grantRole :" +new Date());
	    roleMgr.grantRole("58811",userKeys);
	    System.out.print("Timestamp after calling grantRole :" +new Date());
	 }

  }catch (Exception e){
   System.out.println("In exception block");
   e.printStackTrace();
  }
 }
}
