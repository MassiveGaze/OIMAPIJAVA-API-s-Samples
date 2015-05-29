package com.massiveGaze.provision;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.registry.InvalidRequestException;

import com.massiveGaze.connection.Platform;

import oracle.iam.platform.utils.vo.OIMType;
import oracle.iam.request.api.RequestService;
import oracle.iam.request.exception.BulkBeneficiariesAddException;
import oracle.iam.request.exception.BulkEntitiesAddException;
import oracle.iam.request.exception.InvalidRequestDataException;
import oracle.iam.request.exception.RequestServiceException;
import oracle.iam.request.vo.Beneficiary;
import oracle.iam.request.vo.RequestBeneficiaryEntity;
import oracle.iam.request.vo.RequestBeneficiaryEntityAttribute;
import oracle.iam.request.vo.RequestConstants;
import oracle.iam.request.vo.RequestData;


public class EntitlementRequest {
	public long createRevokeEntitlementRequest(RequestService reqSvc) throws InvalidRequestException, InvalidRequestDataException, RequestServiceException, BulkBeneficiariesAddException, BulkEntitiesAddException, oracle.iam.request.exception.InvalidRequestException{
   	 long requestKey=0;
   	 RequestData reqData = new RequestData();
		
		List<Beneficiary> benList = new ArrayList<Beneficiary>();
		
		
		RequestBeneficiaryEntity reqBenEnt = new RequestBeneficiaryEntity();
		reqBenEnt.setRequestEntityType(OIMType.Entitlement);
		reqBenEnt.setEntitySubType("148"); //ENT_LIST_KEY
		reqBenEnt.setOperation(RequestConstants.MODEL_REVOKE_ENTITLEMENT_OPERATION);
		reqBenEnt.setEntityKey("27"); //ENT_ASSIGN_KEY
		
		RequestBeneficiaryEntityAttribute reqBenEntAttr = new RequestBeneficiaryEntityAttribute();
		reqBenEntAttr.setName("ParentAccountId");
		reqBenEntAttr.setType(RequestBeneficiaryEntityAttribute.TYPE.String);
		reqBenEntAttr.setValue("167"); //OIU_KEY
		
		List<RequestBeneficiaryEntityAttribute> reqBenEntAttrList = new ArrayList();
		reqBenEntAttrList.add(reqBenEntAttr);
		reqBenEnt.setEntityData(reqBenEntAttrList);
		
		List<RequestBeneficiaryEntity> reqBenEntList = new ArrayList();
		reqBenEntList.add(reqBenEnt);
		
		Beneficiary ben = new Beneficiary("User","107");
		ben.setTargetEntities(reqBenEntList);
		
		
		benList.add(ben);
		
		reqData.setBeneficiaries(benList);
		reqData.setJustification("Testing to check the time lag for revoke request");
		System.out.println(Calendar.getInstance().getTime());
		String requestId = reqSvc.submitRequest(reqData);
		System.out.println("Request Id: "+requestId);
		System.out.println(Calendar.getInstance().getTime());
   	 return requestKey;
    }
	
	public static void main(String[] args){
		RequestService reqSvc = Platform.getService(RequestService.class);
		try {
			new EntitlementRequest().createRevokeEntitlementRequest(reqSvc);
			
		} catch (InvalidRequestException | InvalidRequestDataException
				| RequestServiceException | BulkBeneficiariesAddException
				| BulkEntitiesAddException
				| oracle.iam.request.exception.InvalidRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
