/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.massiveGaze.connection;

/**
 *
 *
 */

import java.util.Hashtable;
import java.util.Properties;
import javax.security.auth.login.LoginException;
import oracle.iam.platform.OIMClient;
import com.massiveGaze.properties.OIMProfileReader;
import com.thortech.xl.util.LocalConfiguration;

public class OIMConnection {
	private static String ctxFactory = null;
	private static String serverURL = null;
	private static String appServerType = null;
	private static OIMClient connectionObject = null;
	private static OIMProfileReader properties=null;
	
	private static String username = "xelsysadm";
	private static String password = "Welcome1";	
	
	
	private static OIMClient connection() {
                
		System.out.println("Startup...");
		System.out.println("Getting configuration...");		
		//getProperties();
		//setSystemProperties();
		OIMClient localConnection = new OIMClient(getEnvironment());
		try {
			localConnection.login(username, password.toCharArray());
		} catch (LoginException e) { 
			e.printStackTrace();
		}

		Properties props = LocalConfiguration.getLocalCoreServerConfiguration();
		System.out.println(props.toString());
		return localConnection;
	}

	public static synchronized OIMClient getConnection() {

		if (connectionObject == null) {
			connectionObject = connection();
		}
		return connectionObject;
	}
	
	public static void closeConnection() {
		connectionObject.logout();
	}
	
	public static Hashtable<String, String> getEnvironment(){
		getProperties();
		setSystemProperties();
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, serverURL);
		env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, ctxFactory);
		return env;
	}
	
	public static void getProperties(){
        properties = new OIMProfileReader();
		appServerType=properties.getString("appserver.type");
		serverURL=properties.getString("oim.jndi.url");
		ctxFactory=properties.getString("bpelprovider.initial.factory");	
		System.out.println(" serverURL -> "+serverURL);
	}
	
	public static void setSystemProperties(){

		//Update path of authwl.conf file according to your environment
		if(System.getenv("ADE_VIEW_ROOT")==null || System.getenv("ADE_VIEW_ROOT").equals("")){
			System.setProperty("java.security.auth.login.config","config/authwl.conf");
		}else{
        System.setProperty("java.security.auth.login.config", System.getenv("ADE_VIEW_ROOT")+"/iam/iam-dist/server/config/authwl.conf");   
		}
		
        System.setProperty("OIM.AppServerType", appServerType);  
        System.setProperty("APPSERVER_TYPE", appServerType);
        System.setProperty("XL.HomeDir","");
        // SSL Port connection    http://oimreferences.blogspot.in/2013/07/oim-java-api-to-connect-oim-ssl-port.html
        /*
        System.setProperty("javax.net.ssl.trustStoreType","JKS");
        System.setProperty("javax.net.ssl.trustStore","C:/Program Files (x86)/Java/jre1.7.0_45/lib/security/cacerts");
        System.setProperty("javax.net.ssl.trustStorePassword","changeit");
        */
        // SSL Port connection
	}
	
	public static void main(String args[]){
		getConnection();
       // closeConnection();
			}
}
