package com.massiveGaze.password;


import java.util.HashMap;

import oracle.iam.identity.usermgmt.api.UserManagerConstants;
import oracle.iam.platform.context.ContextManager;
import oracle.iam.selfservice.self.selfmgmt.api.AuthenticatedSelfService;

import com.massiveGaze.connection.Platform;

public class AuthenticatedSelfServiceResetPassword {
	 
	 private AuthenticatedSelfService m_authselfservice = Platform.getService(AuthenticatedSelfService.class);
	public static void main(String[] args) {
		try {
			new AuthenticatedSelfServiceResetPassword().testchangePassword();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
public void testchangePassword() throws Exception {
        
	
        String oldpwd ="Welcome1";
        String newpwd = "Welcome2";
        String confirmpwd = "Welcome2";
		
/*        UserManager userManager = platform.getService(UserManager.class);
        HashMap<String, Object> createAttrsMap = new HashMap<String, Object>();
       // createAttrsMap = utility.getAttributesInMap(m_authUserProps, "CREATE.createAttributes");
        createAttrsMap.put("User Login", "TUSER10");
        createAttrsMap.put("First Name", "TEST10");
        createAttrsMap.put("Last Name", "USER10");
        createAttrsMap.put("act_key", "1");
        createAttrsMap.put("usr_password", "Welcome1");
        createAttrsMap.put("Xellerate Type", "End-User");
        createAttrsMap.put("Role","Full-Time");
        long actkey = Long.parseLong(createAttrsMap.get("act_key").toString());     
        createAttrsMap.put("act_key", actkey);
        createAttrsMap.put(UserManagerConstants.AttributeName.PASSWORD.getId(), oldpwd.toCharArray());
        createAttrsMap.put("Start Date", new java.util.Date());		
        createAttrsMap.put("Email", "TEST.USER10@oracle.com");
        UserManagerResult result = userManager.create(new User(null,createAttrsMap));
        System.out.println("ENTITYID = " + result.getEntityId());
		
       
        
        String usrKey = result.getEntityId();*/
       // setUserInContext("TUSER2", "6");
        System.out.println("###################"+ContextManager.getOIMUser());  
     
        try {
           
            //Attempt to change password 
            m_authselfservice.changePassword(oldpwd.toCharArray(),newpwd.toCharArray(),confirmpwd.toCharArray());
            //m_authselfservice.changePassword("10981", "Pass_12345".toCharArray(),false);  
            ContextManager.clearContext();
            System.out.println("###### PASSWORD UPDATED #############");  
        
            
        } catch (Exception e) {
            e.printStackTrace();            
         } finally {
           
             ContextManager.clearContext();
            }
    }
		public void setUserInContext(String userName, String userKey){
		    if(ContextManager.getContext() != null) {
		    ContextManager.popContext();
		  }
		  ContextManager.pushContext(null, null, null);
		  HashMap<String,String> map = new HashMap<String,String>();
		  map.put(UserManagerConstants.AttributeName.USER_KEY.getId(), userKey);
		  ContextManager.setOIMUser(userName);
		  ContextManager.setUserDetails(map);
		  
		}


}
