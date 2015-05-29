package com.massiveGaze.connection;
import oracle.mds.core.MDSInstance;

public class Platform{
	
	public static <T> T getService(Class<T> serviceClass)  {
		
		return OIMConnection.getConnection().getService(serviceClass);
	}
	public static javax.sql.DataSource getOperationalDS(){
		return  oracle.iam.platform.Platform.getOperationalDS();
	}
	public static MDSInstance getMDSInstance(){
		return oracle.iam.platform.Platform.getMDSInstance();
	}
	 
}
