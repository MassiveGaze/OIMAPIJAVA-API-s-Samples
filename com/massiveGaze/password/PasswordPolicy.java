package com.massiveGaze.password;

import com.massiveGaze.connection.OIMConnection;

import oracle.iam.passwordmgmt.api.PasswordMgmtService;
import oracle.iam.passwordmgmt.vo.PasswordPolicyInfo;

public class PasswordPolicy {
	static PasswordMgmtService passwordmgmtService = OIMConnection.getConnection().getService(PasswordMgmtService.class);
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		passwordPolicyCreate();
		//deletePolicy("PasswordPolicy90");
	}
	
	public static String passwordPolicyCreate() throws Exception {
		//Create Password Policy
        String name = "PasswordPolicy1" ; 
        PasswordPolicyInfo passPolicyInfo = new PasswordPolicyInfo();
        passPolicyInfo.setName(name);
        passPolicyInfo.setShortDesc(name + " description");
        passPolicyInfo.setPasswordExpiresAfterInDays(90);
               
        PasswordPolicyInfo searchPassPolicyInfo = new PasswordPolicyInfo();
        passPolicyInfo = passwordmgmtService.create(passPolicyInfo);        
        searchPassPolicyInfo = passwordmgmtService.getDetails(passPolicyInfo.getName());                 
        System.out.println("Password Policy Key -> " +passPolicyInfo.getId()) ;
        System.out.println("Search Password Policy Key -> "+searchPassPolicyInfo.getId()) ;  
        System.out.println("getPasswordExpiresAfterInDays - >"+passPolicyInfo.getPasswordExpiresAfterInDays());
        return passPolicyInfo.getId();
	}
	public static void deletePolicy(String name){
		// passwordmgmtService.delete(name) ;
		PasswordPolicyInfo passPolicyInfo = new PasswordPolicyInfo();
	    passPolicyInfo.setPasswordExpiresAfterInDays(null);
		passPolicyInfo.setName("PasswordPolicy1");
		passwordmgmtService.update(passPolicyInfo);
		 passPolicyInfo=passwordmgmtService.getDetails(passPolicyInfo.getName());
		 System.out.println(passPolicyInfo.getId());
		 System.out.println(passPolicyInfo.getPasswordExpiresAfterInDays());
	}

}
