package com.massiveGaze.orgs;

import com.massiveGaze.connection.OIMConnection;

import oracle.iam.identity.exception.NoSuchOrganizationException;
import oracle.iam.identity.exception.OrganizationAlreadyDeletedException;
import oracle.iam.identity.exception.OrganizationDeleteException;
import oracle.iam.identity.exception.OrganizationDeleteSubOrgsExistException;
import oracle.iam.identity.exception.OrganizationDeleteSubOrgsUsersExistException;
import oracle.iam.identity.exception.OrganizationDeleteUsersExistException;
import oracle.iam.identity.exception.OrganizationOrphanedPublishedEntitiesException;
import oracle.iam.identity.orgmgmt.api.OrganizationManager;
import oracle.iam.platform.OIMClient;
import oracle.iam.platform.authz.exception.AccessDeniedException;

public class DeleteOrganizations {
	protected static OIMClient platform = OIMConnection.getConnection();
	protected static OrganizationManager m_orgManagerService =(OrganizationManager) platform.getService(OrganizationManager.class);
	
	public void deleteOrganizations() throws OrganizationDeleteException, NoSuchOrganizationException, OrganizationAlreadyDeletedException, OrganizationDeleteSubOrgsExistException, OrganizationDeleteSubOrgsUsersExistException, OrganizationDeleteUsersExistException, OrganizationOrphanedPublishedEntitiesException, AccessDeniedException {

		//for (int i = 4; i <100 ; i++) {
			String orgKey= Integer.toString(81);
			try{
				m_orgManagerService.delete(orgKey, false);
			}catch(Exception e){
				System.out.println(" orgKey -> "+ orgKey);
			}
	//	}
		 System.out.println("Done");
	}

}
