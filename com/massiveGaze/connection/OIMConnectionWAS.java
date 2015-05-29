package com.massiveGaze.connection;
/*  
 * https://github.com/adharmad/oim-api-samples/tree/master/src/iamsamples/provisioning
 * http://docs.oracle.com/cd/E24179_01/doc.1111/e23377/integratingoimdeprecated.htm#OIASI157
 * */

import java.util.Hashtable;
import java.util.Properties;

import javax.security.auth.login.LoginException;

import oracle.iam.platform.OIMClient;

import com.thortech.xl.util.LocalConfiguration;

public class OIMConnectionWAS {
	private static String ctxFactory = "com.ibm.websphere.naming.WsnInitialContextFactory";
	private static String hostName = "slc08ewt.us.oracle.com"; 
	private static String port = "2802";
	private static String serverURL = "corbaloc:iiop:" + hostName + ":" + port;
	private static OIMClient connectionObject = null;

	private static OIMClient connection() {
		System.out.println("Startup...");
		System.out.println("Getting configuration...");
		Hashtable env = new Hashtable();
		env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, serverURL);
		env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, ctxFactory);
        System.setProperty("java.security.auth.login.config", "D:/OIMAPIclients/R2PS1/config/authws.conf");   //Update path of authwl.conf file according to your environment
        System.setProperty("OIM.AppServerType", "was");  
        System.setProperty("APPSERVER_TYPE", "was");
        System.setProperty("XL.HomeDir","D:/OIMAPIclients/R2PS1");
		OIMClient localConnection = new OIMClient(env);
		try {
			localConnection.login("xelsysadm", "Welcome1");
		} catch (LoginException e) { // TODO Auto-generated catch block
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
	
	public static Hashtable getEnvironment(){
		Hashtable env = new Hashtable();
		env.put(OIMClient.JAVA_NAMING_PROVIDER_URL, serverURL);
		env.put(OIMClient.JAVA_NAMING_FACTORY_INITIAL, ctxFactory);
		return env;
	}
	public static void main(String args[]){
		getConnection();
			}
}
