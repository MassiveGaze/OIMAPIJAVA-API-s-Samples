/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.massiveGaze.scheduler;
import java.util.*;

import oracle.iam.identity.utils.Constants;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.platform.OIMClient;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.usermgmt.vo.UserManagerResult;




public class MultiThreadCreateUsers {
	public static void main(String [] str)
	{
		for(int i=2; i<102; i++){
			CreateUserThread cThread = new CreateUserThread("MANJUNATH_THREAD"+i+"_");
			cThread.start();
		}
	}
}

class CreateUserThread extends Thread {
	private String threadName;
	
	CreateUserThread (String name) {
		threadName = name;
	}
	
	public void run() {
		try {
			System.out.println(" Started Thread : "+ threadName);
			String ctxFactory = "weblogic.jndi.WLInitialContextFactory";
			
			String hostName = "adc2120034.us.oracle.com";
			String port = "7001";
			String serverURL = "t3://"+hostName+":"+port;
			Hashtable env = new Hashtable();
			env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, serverURL);
			env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL,ctxFactory);
                         System.setProperty("java.security.auth.login.config", "/scratch/mbanappa/view_storage/mbanappa_FA/iam/dist/oracle.oim.design_console/designconsole/config/authwl.conf");
			OIMClient platform = new OIMClient(env);
			platform.login("xelsysadm", "Welcome1");
			System.out.println("Logged in as XELSYSADM");
			
	        UserManager userManager = (UserManager) platform.getService(UserManager.class);
	        
	        HashMap<String,Object> createAttributes = new HashMap<String,Object>();


            // create users
            for(int i=2; i< 102; i++)
            {
                createAttributes.put(Constants.USERID, threadName+i);
                createAttributes.put(Constants.FIRSTNAME,threadName+i);
                createAttributes.put(Constants.LASTNAME,threadName+i);
                createAttributes.put(Constants.ORGKEY, Long.parseLong("1"));
                createAttributes.put(Constants.PASSWORD,"Welcome1");
                createAttributes.put(Constants.EMAIL, threadName+i+"@oracle.com");
                createAttributes.put(Constants.EMPTYPE,"Full-Time");
                createAttributes.put(Constants.USERTYPE,"End-User");

                UserManagerResult result = userManager.create(new User(null, createAttributes));
                System.out.println("Created  user. "+ threadName+i); 
            }
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}