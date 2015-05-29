package com.massiveGaze.users;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.SearchKeyNotUniqueException;
import oracle.iam.identity.exception.UserModifyException;
import oracle.iam.identity.exception.ValidationFailedException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.usermgmt.vo.UserManagerResult;
import oracle.iam.identity.utils.Constants;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.authz.exception.AccessDeniedException;

import com.massiveGaze.orgs.createOrganizations;
import com.massiveGaze.password.ResetPasswordAsXelysyadmin;
import com.massiveGaze.connection.Platform;
import com.thortech.xl.util.LocalConfiguration;

public class createUsers {
	private static final String USERS_XELLERATE_TYPE = "Users.Xellerate Type";
	private static final String USERS_LAST_NAME = "Users.Last Name";
	private static final String USERS_FIRST_NAME = "Users.First Name";
	private static final String USERS_USER_ID = "Users.User ID";
	private static final String ORGANIZATIONS_KEY = "Organizations.Key";
	private static final String USERS_END_DATE = "Users.End Date";
	private static final String USERS_PASSWORD = "Users.Password";
	private static final String USERS_ROLE = "Users.Role";
	private static final String USERS_MANAGERKEY = "Users.Manager Key";
	private static  OIMClient oimClient = null;
	
	
	public static void main(String[] args) {
		
		createUsers();
		//modify();
	}
	
	 public static int getRandomNumber() {
	        java.util.Random r = new java.util.Random(Calendar.getInstance()
	                .getTimeInMillis());
	        int randint = Math.abs(r.nextInt()) % 1000;     
	        return randint;
	    }
	
	public static void createUsers(){
		try{
			System.out.println("Inside registerUser Method..!");
		
			System.out.println("Connection Established Without Credentials ..!");
			UserManager userManager = (UserManager) Platform.getService(UserManager.class);		
			Properties props = LocalConfiguration
					.getLocalCoreServerConfiguration();
			System.out.println(props.toString());
			long orgkey = 1;

			 long MANAGERKEY = 41;

			HashMap<String, Object> createAttributes=null;
		
			for(int i=380;i< 440;i++){
		
			createAttributes = new HashMap<String, Object>();
			
			// create users
				//String i ="_Manager2";//getRandomNumber();
				createAttributes.put(Constants.USERID, "TUSER" + i);
				createAttributes.put(Constants.FIRSTNAME, "TEST" + i);
				createAttributes.put(Constants.LASTNAME, "USER" + i);				
				//createAttributes.put(Constants.ORGKEY,  Long.parseLong(createOrganizations.createorg()));
				createAttributes.put(Constants.ORGKEY, orgkey);
				createAttributes.put(Constants.PASSWORD, "Welcome1");
				createAttributes.put(Constants.EMAIL, "TUSER" + i+ "@oracle.com");
				createAttributes.put(Constants.EMPTYPE, "Full-Time");
				createAttributes.put(Constants.USERTYPE,"End-User Administrator");
				createAttributes.put(Constants.MANAGERKEY,MANAGERKEY);
				//createAttributes.put(Constants.AUTOMATICALLY_DELETED_ON,new Date(2014,6,11));
				System.out.println("  ID -> "+i);
				UserManagerResult result = userManager.create(new User(null,createAttributes));
				System.out.println("getEntityId  user."+result.getEntityId());
				System.out.println("Status  user."+result.getStatus());
				
				System.out.println("getSucceededResults  user."+result.getSucceededResults());
				ResetPasswordAsXelysyadmin.resetPassword(result.getEntityId());
				
			}	
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	 private static Map<String, Object> setChallengeQuestiones() throws Exception {
    	 System.out.println("Setting  Challenge Questions ....");
        Map<String, Object> mapQnA = new HashMap<String, Object>();			
		mapQnA.put("What is your mother's maiden name?", "4");
		mapQnA.put("What is your favorite color?", "5");
		//mapQnA.put("What is the city of your birth?", "6");
		mapQnA.put("What is the name of your pet?", "6");

        return mapQnA;
    }

	 
	 public <T> T getService(Class<T> serviceClass) throws Exception{
			return oimClient.getService(serviceClass);
		}
	 
	 public static void modify(){
		 UserManager userManager = (UserManager) Platform.getService(UserManager.class);
		 HashMap<String, Object> modifyAttributes=null;
		 long MANAGERKEY = 5;
		 long orgkey = 1;
		 for(int i=1;i <1000;i++){
			 modifyAttributes = new HashMap<String, Object>();
			 modifyAttributes.put(Constants.MANAGERKEY,MANAGERKEY);
			// modifyAttributes.put(Constants.ORGKEY, orgkey);
		 User user= new User("TESTUSER" + i,modifyAttributes);
		 try {
			userManager.modify("User Login", "TESTUSER" + i, user);
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
