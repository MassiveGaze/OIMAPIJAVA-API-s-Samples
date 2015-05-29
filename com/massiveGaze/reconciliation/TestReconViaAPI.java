package com.massiveGaze.reconciliation;
import java.util.HashMap;
import java.util.Map;

import oracle.iam.reconciliation.api.ChangeType;
import oracle.iam.reconciliation.api.EventAttributes;
import oracle.iam.reconciliation.api.ReconOperationsService;

import com.massiveGaze.connection.Platform;

 
public class TestReconViaAPI {
  
public static void main(String[] args) {
	   try {

            ReconOperationsService recService = Platform.getService(ReconOperationsService.class);
            //Add Object Reconciliation Fields for resource Object. No Childern fields.
	            Map<String, Object> mapKeyValue = new HashMap<String,Object>();         
	          
	           mapKeyValue.put("Account Name","TUSER3");
	           mapKeyValue.put("IT resource","TestITResourceName");           
	            
                //Create Even Attribute and call createReconciliationEvent
                EventAttributes eventAttr = new EventAttributes();
                eventAttr.setEventFinished(false);  
                eventAttr.setChangeType(ChangeType.REGULAR);
                long reconEventKey = recService.createReconciliationEvent("TESTResource", mapKeyValue,eventAttr);
                System.out.println("Recon Event ID -> "+reconEventKey);
                /*********Creating Data set Up for Child Forms *******************************/
               
             
                //Create multi valued AttributeData for Child Forms
                Map<String,Object> mapChldKeyValue = null;               
           
                recService.providingAllMultiAttributeData(reconEventKey, "Group Name",true);
               //Add Child Form multi valued data  
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU","fgdfg");                  
                recService.addMultiAttributeData(reconEventKey,"Group Name", mapChldKeyValue);
                
                
                recService.finishReconciliationEvent(reconEventKey); 
                recService.processReconciliationEvent(reconEventKey);
                
                //processing event
               //recService.callingEndOfJobAPI();     
                System.out.println("Completed Recon Event Operation -> "+reconEventKey);
                
              }catch(Exception e){
                  e.printStackTrace();              
              }
   }

}