package com.massiveGaze.users;

import com.massiveGaze.connection.Platform;
import java.util.HashMap;
import java.util.Hashtable;
import javax.security.auth.login.LoginException;
 
import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserAlreadyExistsException;
import oracle.iam.identity.exception.UserCreateException;
import oracle.iam.identity.exception.UserDisableException;
import oracle.iam.identity.exception.UserEnableException;
import oracle.iam.identity.exception.UserLockException;
import oracle.iam.identity.exception.UserManagerException;
import oracle.iam.identity.exception.UserUnlockException;
import oracle.iam.identity.exception.ValidationFailedException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.OIMClient;
 
public class AllUserOperations {
     
    UserManager userManager=(UserManager) Platform.getService(UserManager.class);
     
    public AllUserOperations() {
        super();
    }
    public static void main(String[] arg) {
         
        AllUserOperations oim=new AllUserOperations();        
        oim.createUser("Manjunath");    //comment if you are calling any other methods below
        //oim.lockUser("sachinTen");    //uncomment to lock user
        //oim.unLockUser("sachinten");  //uncomment to unlock user 
        //oim.disableUser("sachinTen");   //uncomment to disabel user
        //oim.enableUser("sachinTen");    //uncomment to enable user
        //oim.resetPassword("sachinTen");   //uncommnet to reset password
                 
    }
   
     
    public void createUser(String userId) {                                             //Function to create User
        HashMap<String, Object> userAttributeValueMap = new HashMap<String, Object>();
                userAttributeValueMap.put("act_key", new Long(1));
                userAttributeValueMap.put("User Login", userId);
                userAttributeValueMap.put("First Name", "Sachin");
                userAttributeValueMap.put("Last Name", "Ten");
                userAttributeValueMap.put("Email", "Sachin.Ten@abc.com");
                userAttributeValueMap.put("usr_password", "Password123");
                userAttributeValueMap.put("Role", "OTHER");
                User user = new User("Sachin", userAttributeValueMap);
        try {
            userManager.create(user);
            System.out.println("\nUser got created....");
        } catch (ValidationFailedException e) {
            e.printStackTrace();
        } catch (UserAlreadyExistsException e) {
            e.printStackTrace();
        } catch (UserCreateException e) {
            e.printStackTrace();
        }
    }
    public void disableUser(String userId) {                        //Function to disable user
        try {
            userManager.disable(userId, true);
            System.out.print("\n Disabled user Successfully");
        } catch (ValidationFailedException e) {
            e.printStackTrace();
        } catch (UserDisableException e) {
            e.printStackTrace();
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }
    }
    public void enableUser(String userId) {                         //Function to enable user
        try {
            userManager.enable(userId, true);
            System.out.print("\n Enabled user Successfully");
        } catch (ValidationFailedException e) {
            e.printStackTrace();
        } catch (UserEnableException e) {
            e.printStackTrace();
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }
    }
    public void resetPassword(String userId){                       //Function to reset user password
 
 
        try {
            userManager.resetPassword(userId, true,true);          //Random Password will be set and will be sent to user mail if notifications are enabled
            System.out.println("Reset Password done...");
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        } catch (UserManagerException e) {
            e.printStackTrace();
        }
    }
    public void lockUser(String userId) {       //Function to Lock User
        try {
            userManager.lock(userId, true,true);
        } catch (ValidationFailedException e) {
            e.printStackTrace();
        } catch (UserLockException e) {
            e.printStackTrace();
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }
    }
    public void unLockUser(String userId) {       //Function to Unlock user
        try {
            userManager.unlock(userId, true);
        } catch (ValidationFailedException e) {
            e.printStackTrace();
        } catch (UserUnlockException e) {
            e.printStackTrace();
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }
    }
}