package com.massiveGaze.users;

import java.util.ArrayList;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserDeleteException;
import oracle.iam.identity.exception.UserLookupException;
import oracle.iam.identity.exception.ValidationFailedException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.UserManagerResult;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.authz.exception.AccessDeniedException;

import com.massiveGaze.connection.OIMConnection;
import com.massiveGaze.connection.Platform;

public class BulkDeleteUsers {

	public static void main(String[] args) throws NoSuchUserException,
			UserLookupException, AccessDeniedException {
		GetUserDeatils userdetails = new GetUserDeatils();
		//String userKey = userdetails.getUserKey("281");
		//System.out.println(" userKey-> " + userKey);
		deleteUser();
	}

	public static void deleteUser() {
		System.out.println("Inside registerUser Method..!");		
		System.out.println("Connection Established With Credentials ..!");
		UserManager usrMgr = Platform.getService(UserManager.class);
		try {
			ArrayList<String> userIDs = new ArrayList<String>();
			//userIDs.add("422");
			userIDs.add("43");
			
		   UserManagerResult result= usrMgr.delete("11021",false); // False
		   
			// because we used usr_key. Use true if using User Login
			System.out.println("Deletion Is ... " + result.getStatus());
		} catch (ValidationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserDeleteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchUserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AccessDeniedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
