package com.massiveGaze.users;

import java.util.HashSet;
import java.util.Set;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.oimcommon.impl.UserServiceImpl;
import oracle.iam.platform.authz.exception.AccessDeniedException;

import com.massiveGaze.connection.Platform;

public class GetUserDeatils {
	public String getUserKey(String userKey) throws NoSuchUserException, UserLookupException, AccessDeniedException {
		// Get user details..
		System.out.println("Inside registerUser Method..!");
	
		System.out.println("Connection Established With Credentials ..!");
		UserManager userManager = (UserManager) Platform.getService(UserManager.class);
		Set<String> searchAttrs = new HashSet<String>();
		searchAttrs.add("Status");
		searchAttrs.add("Role");
		searchAttrs.add("First Name");
		searchAttrs.add("Last Name");
		searchAttrs.add("usr_timezone");
		searchAttrs.add("usr_locale");
		searchAttrs.add("Postal Code");
		searchAttrs.add("usr_pwd_expire_date");
		User userLookup = userManager.getDetails(userKey, new UserServiceImpl().getAllUserAttributes(), false);	
		System.out.println("key : " + userLookup.getEntityId());
		System.out.println("attributes : " + userLookup.getAttributes());
		System.out.println("usr_pwd_expire_date : " + (String)userLookup.getAttributes().get("usr_pwd_expire_date"));

		System.out.println("Done");
		return userLookup.getEntityId();
	}
	
	
public static void main(String[] args) {

	try {
		new GetUserDeatils().getUserKey("4");
		
	} catch (NoSuchUserException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}catch (UserLookupException e){
	e.printStackTrace();
	}catch( AccessDeniedException e){
	e.printStackTrace();
	}
}
}
