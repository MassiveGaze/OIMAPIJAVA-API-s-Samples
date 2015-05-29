package com.massiveGaze.provision;

import java.util.HashMap;

import com.massiveGaze.connection.Platform;

import oracle.iam.provisioning.exception.GenericProvisioningException;
import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcFormNotFoundException;
import Thor.API.Exceptions.tcInvalidValueException;
import Thor.API.Exceptions.tcNotAtomicProcessException;
import Thor.API.Exceptions.tcObjectNotFoundException;
import Thor.API.Exceptions.tcOrganizationNotFoundException;
import Thor.API.Exceptions.tcProcessNotFoundException;
import Thor.API.Exceptions.tcProvisioningNotAllowedException;
import Thor.API.Exceptions.tcRequiredDataMissingException;
import Thor.API.Operations.tcFormInstanceOperationsIntf;
import Thor.API.Operations.tcOrganizationOperationsIntf;

public class ProvisionOrganization {

	public static void main(String[] args) throws tcAPIException, tcInvalidValueException, tcNotAtomicProcessException, tcFormNotFoundException, tcRequiredDataMissingException, tcProcessNotFoundException, tcObjectNotFoundException, tcProvisioningNotAllowedException, tcOrganizationNotFoundException, GenericProvisioningException, tcColumnNotFoundException {
		Long actKey=1L;//actKey - You can get it from ACT table.
		Long objKey=1L;// objKey - You can get it from OBJ table.

		
	        tcOrganizationOperationsIntf tcOrgOp = Platform.getService(tcOrganizationOperationsIntf.class);
	        long objInsKey = tcOrgOp.provisionObject(actKey, objKey);

	        tcResultSet rsetAccounts = tcOrgOp.getObjects(actKey);
	        long processInstanceKey = -1;
	        for(int i=0; i<rsetAccounts.getRowCount(); i++) {
	            rsetAccounts.goToRow(i);
	            if (rsetAccounts.getLongValue("Object Instance.Key") == objInsKey) {
	                processInstanceKey = rsetAccounts.getLongValue("Process Instance.Key");
	                if(processInstanceKey == 0 || rsetAccounts.getStringValue("Objects.Object Status.Status").equals("Waiting")){
	                    throw new GenericProvisioningException("Resource in Waiting status");
	                }
	                break;
	            }
	        }

	        tcFormInstanceOperationsIntf tcFrmInsOp = Platform.getService(tcFormInstanceOperationsIntf.class);
	        HashMap<String, String> data = new HashMap<String, String>();
	        // Resource Form data, All the values in String format.
	        data.put("UD_OID_OU_SERVER", "5");
	        data.put("UD_OID_OU_NAME", "actName");
	        data.put("UD_OID_OU_ORGNAME", "5~ou=sales,cn=Users,dc=us,dc=oracle,dc=com");
	        tcFrmInsOp.setProcessFormData(processInstanceKey, data);

	}

}
