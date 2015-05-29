package com.massiveGaze.ldap;

import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class LDAPConnection {

	static String oidUrl,oidUserName,oidPassword,userContainer;
    private static final String TEST_PROPERTIES_FILE = "data/ldapsync.properties";
    static{
	System.out.println("Connection Started ");
		try{
			Properties eventProps = new Properties();
			eventProps.load(new FileInputStream(TEST_PROPERTIES_FILE));
			  oidUrl = eventProps.getProperty("LDAP.OID.URL");
	            oidUserName = eventProps.getProperty("LDAP.OID.Username");
	            oidPassword = eventProps.getProperty("LDAP.OID.Password");
	            userContainer = eventProps.getProperty("LDAP.UserContainer");
	            System.out.println("oidUrl- > "+oidUrl);
	            System.out.println("oidUserName- > "+oidUserName);
	            System.out.println("userContainer- > "+userContainer);
			
		}catch(Exception e){
			System.out.println("Connection Failed With Exception ");
			e.printStackTrace();
		}
}
	public static DirContext getLDAPConnection() throws Exception {
		Hashtable<String,String> attrs = new Hashtable<String,String>();
        attrs.put(Context.PROVIDER_URL, oidUrl);
        attrs.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
		attrs.put(Context.SECURITY_PRINCIPAL, oidUserName);
		attrs.put(Context.SECURITY_CREDENTIALS, oidPassword);
		DirContext context = new InitialDirContext(attrs);
		return context;
    }
	public static String getUserContainer(){
		return userContainer;
	}
	
}
