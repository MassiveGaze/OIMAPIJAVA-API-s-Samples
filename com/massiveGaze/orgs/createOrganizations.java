package com.massiveGaze.orgs;

import java.util.Calendar;

import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.identity.orgmgmt.vo.Organization;
import oracle.iam.platform.OIMClient;

import com.massiveGaze.connection.OIMConnection;

public class createOrganizations {	
	protected static OIMClient platform = OIMConnection.getConnection();
	protected static OrganizationManager m_orgManagerService ;
 

	 public static int getRandomNumber() {
	        java.util.Random r = new java.util.Random(Calendar.getInstance()
	                .getTimeInMillis());
	        int randint = Math.abs(r.nextInt()) % 1000;     
	        return randint;
	    }

	public static String createorg() throws Exception {
		m_orgManagerService = (OrganizationManager) platform.getService(OrganizationManager.class);
		String result=null;
		Organization org = new Organization();
	//	for(int i=0; i< 100;i++){
		org.setAttribute("Organization Name", "Salute");
		org.setAttribute("Organization Customer Type", "Department");
		org.setAttribute("Organization Status", "Active");		
		result = m_orgManagerService.create(org);		   
		System.out.println("Organization   Key : " + result);	
	//	}
		System.out.println("Done");
		//System.out.println("Updated Password with key "+updateOrgPasswordPolicy(result));
		return result; 
	}
	
	
	public static void main(String[] args) throws Exception {
		createOrganizations createorgObj = new createOrganizations();
		createorgObj.createorg();		
		//createorgObj.deleteOrganizations();
		
	}
}