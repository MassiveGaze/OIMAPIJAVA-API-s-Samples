/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.massiveGaze.users;

import com.massiveGaze.connection.Platform;
import java.util.HashMap;
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;
import oracle.iam.identity.exception.UserModifyException;
import oracle.iam.identity.exception.ValidationFailedException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.utils.Constants;
import oracle.iam.platform.authz.exception.AccessDeniedException;

/**
 *
 *
 */
public class ModifyUserDetails {
    public static void main(String[] args) {
		modify();
	}
	
   	 public static void modify(){
		 UserManager userManager = (UserManager) Platform.getService(UserManager.class);
		 HashMap<String, Object> modifyAttributes=null;
		 long MANAGERKEY = 28;
		 for(int i=15;i <20;i++){
			 modifyAttributes = new HashMap<String, Object>();
			 modifyAttributes.put(Constants.MANAGERKEY,MANAGERKEY);
		 User user= new User("TUSER_Manager" + i,modifyAttributes);
		 try {
			userManager.modify("User Login", "TUSER_Manager" + i, user);
                        
		} catch (ValidationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserModifyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchUserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SearchKeyNotUniqueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 }
	 } 
}
