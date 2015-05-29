/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.massiveGaze.users;
import java.util.*;

import oracle.iam.passwordmgmt.vo.Constants;
import oracle.iam.platform.OIMClient;
import oracle.iam.notification.api.NotificationService;
import oracle.iam.notification.vo.NotificationEvent;


public class MultiThreadNotify {
	public static void main(String [] str)
	{
		for(int i=0; i<500; i++){
			NotifyThread cThread = new NotifyThread("MY_L_THREAD_"+i+"_");
			cThread.start();
		}
	}
}

class NotifyThread extends Thread {
	private String threadName;
	
	NotifyThread (String name) {
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
			
	        NotificationService notificationService = (NotificationService) platform.getService(NotificationService.class);
	        

            // create users
            for(int i=0; i< 500; i++)
            {
            	NotificationEvent event = new NotificationEvent();
    	        event.setTemplateName("CreateUserSelfServiceNotification");
    	        event.setSender(null);
    	        HashMap<String, Object> notificationData = new HashMap<String, Object>();
    	        notificationData.put(Constants.USER_ID, "MYNEWTHREAD_11_30");
    	        notificationData.put(Constants.USER_EMAIL, "MYNEWTHREAD_11_30@oracle.com");

    	        event.setUserIds(new String[] { "MYNEWTHREAD_11_30"});
    	        // Set the values in event object
    	        event.setParams(notificationData);
    	        try{
                boolean result = notificationService.notify(event);
                System.out.println("Notified MYNEWTHREAD_11_30 : " + result ); 
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}