package com.massiveGaze.password;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserManagerException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.platform.authz.exception.AccessDeniedException;

import com.massiveGaze.connection.Platform;

public class ResetPasswordAsXelysyadmin {
	
	public static void resetPassword(String userKey) throws NoSuchUserException, UserManagerException, AccessDeniedException{
		UserManager userManager = (UserManager) Platform.getService(UserManager.class);	
		userManager.changePassword(userKey,"Pass_12345".toCharArray() , false);
		
	}
	public static void main(String[] args) throws NoSuchUserException, UserManagerException, AccessDeniedException {
		resetPassword("11021");
	}

}
