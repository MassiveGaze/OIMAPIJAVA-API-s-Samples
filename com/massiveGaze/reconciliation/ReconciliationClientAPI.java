package com.massiveGaze.reconciliation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.iam.reconciliation.api.ChangeType;
import oracle.iam.reconciliation.api.EventAttributes;
import oracle.iam.reconciliation.api.ReconOperationsService;

import com.massiveGaze.connection.Platform;

/*
 * 	1. bug#14163161
 */
 
public class ReconciliationClientAPI {
  
public static void main(String[] args) {
	   try {
		   List list =new GetLookUpValues().getLookupCodes("1629");
         for (int i=1;i <2;i++){
            ReconOperationsService recService = Platform.getService(ReconOperationsService.class);
            //Add Object Reconciliation Fields for resource Object. No Childern fields.
	            Map<String, Object> mapKeyValue = new HashMap<String,Object>();     
	            //For Dummy Connector
/*	            mapKeyValue.put("USR_LOGIN","USER"+i);
	            mapKeyValue.put("ACCOUNT_NAME","USER Account");
	            mapKeyValue.put("IT Resource","dummyITResName");     */      
	            
	            //For DBUM Connector
	            mapKeyValue.put("IT Resource Name","Oracle DB");
	            mapKeyValue.put("User Name","TUSER"+i);	            
	            mapKeyValue.put("Password","Welcome1");
	            mapKeyValue.put("Authentication Type","PASSWORD");
	            mapKeyValue.put("Account Status","OPEN");
	            mapKeyValue.put("Reference ID","TUSER"+i);
                //Create Even Attribute and call createReconciliationEvent
                EventAttributes eventAttr = new EventAttributes();
                eventAttr.setEventFinished(false);  
                eventAttr.setChangeType(ChangeType.CHANGELOG);
                //long reconEventKey = recService.createReconciliationEvent("dummy", mapKeyValue,eventAttr);
               long reconEventKey = recService.createReconciliationEvent("Oracle DB User", mapKeyValue,eventAttr);
                System.out.println("Recon Event ID -> "+reconEventKey);
                /*********Creating Data set Up for Child Forms *******************************/
               
             
                //Create multi valued AttributeData for Child Forms
                Map<String,Object> mapChldKeyValue = null;
                
            /*    recService.providingAllMultiAttributeData(reconEventKey, "CH1",true);
               //Add Child Form multi valued data  
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU1","One1");                  
                recService.addMultiAttributeData(reconEventKey,"CH1", mapChldKeyValue);
                
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU1","11");                  
                recService.addMultiAttributeData(reconEventKey,"CH1", mapChldKeyValue); 
                
                

                recService.providingAllMultiAttributeData(reconEventKey, "CH2",true);
               //Add Child Form multi valued data  
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU2","1");                  
                recService.addMultiAttributeData(reconEventKey,"CH2", mapChldKeyValue);
                
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU2","2");                  
                recService.addMultiAttributeData(reconEventKey,"CH2", mapChldKeyValue);
                
                
            
                recService.providingAllMultiAttributeData(reconEventKey, "CH3",true);
               //Add Child Form multi valued data  
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU3","Three");                  
                recService.addMultiAttributeData(reconEventKey,"CH3", mapChldKeyValue);
                
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU3","3");                  
                recService.addMultiAttributeData(reconEventKey,"CH3", mapChldKeyValue);
                
                
                
                recService.providingAllMultiAttributeData(reconEventKey, "CH4",true);
               //Add Child Form multi valued data  
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU4","Four4");                  
                recService.addMultiAttributeData(reconEventKey,"CH4", mapChldKeyValue);
                
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU4","44");                  
                recService.addMultiAttributeData(reconEventKey,"CH4", mapChldKeyValue);
                
                
                
                recService.providingAllMultiAttributeData(reconEventKey, "CH5",true);
               //Add Child Form multi valued data  
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU5","Five");                  
                recService.addMultiAttributeData(reconEventKey,"CH5", mapChldKeyValue);
                
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU5","5");                  
                recService.addMultiAttributeData(reconEventKey,"CH5", mapChldKeyValue);
                
                
                
                recService.providingAllMultiAttributeData(reconEventKey, "CH6",true);
               //Add Child Form multi valued data  
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU6","Six");                  
                recService.addMultiAttributeData(reconEventKey,"CH6", mapChldKeyValue);
                
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU6","6");                  
                recService.addMultiAttributeData(reconEventKey,"CH6", mapChldKeyValue);
                
                
                
                recService.providingAllMultiAttributeData(reconEventKey, "CH7",true);
               //Add Child Form multi valued data  
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU7","Seven");                  
                recService.addMultiAttributeData(reconEventKey,"CH7", mapChldKeyValue);
                
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU7","7");                  
                recService.addMultiAttributeData(reconEventKey,"CH7", mapChldKeyValue);
                
                
                recService.providingAllMultiAttributeData(reconEventKey, "CH8",true);
               //Add Child Form multi valued data  
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU8","Eight8");                  
                recService.addMultiAttributeData(reconEventKey,"CH8", mapChldKeyValue);
                
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU8","88");                  
                recService.addMultiAttributeData(reconEventKey,"CH8", mapChldKeyValue);
                */
                
                /*      
                recService.providingAllMultiAttributeData(reconEventKey, "CH1",true);
               //Add Child Form multi valued data  
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU1","1");                  
                recService.addMultiAttributeData(reconEventKey,"CH1", mapChldKeyValue);
                
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU1","2");                  
                recService.addMultiAttributeData(reconEventKey,"CH1", mapChldKeyValue); 
                */
                
                //DBUM Connector 
                
              recService.providingAllMultiAttributeData(reconEventKey, "Privilege List",true);
               //Add Child Form multi valued data  
             
               for(int j=0; j<list.size()-600;j++){
            	 
                mapChldKeyValue = new HashMap<String,Object>();
               mapChldKeyValue.put("Privilege Name",list.get(j));
              // mapChldKeyValue.put("Privilege Name","4~CREATE PROFILE");
                mapChldKeyValue.put("Privilege Admin Option","");  
                recService.addMultiAttributeData(reconEventKey,"Privilege List", mapChldKeyValue);
               // System.out.println(" Added into Recon Service with loop value -> "+j);
               }
                
             /*
                recService.providingAllMultiAttributeData(reconEventKey, "VISA",true);
                //Add Child Form multi valued data  
               // List list =new GetLookUpValues().getLookupCodes("1621");
               // for(int j=0; j<list.size()-40000;j++){
                 mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("VISA NAME","VISA_C856901");
               //  mapChldKeyValue.put("Privilege Name","4~CREATE PROFILE");
                // mapChldKeyValue.put("Privilege Admin Option","");  
                 recService.addMultiAttributeData(reconEventKey,"VISA", mapChldKeyValue);
                }
                
            
                recService.providingAllMultiAttributeData(reconEventKey, "CH0",true);
               //Add Child Form multi valued data  
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU0","Zero0");                  
                recService.addMultiAttributeData(reconEventKey,"CH0", mapChldKeyValue);
                
                mapChldKeyValue = new HashMap<String,Object>();
                mapChldKeyValue.put("CHDLKU0","00");                  
                recService.addMultiAttributeData(reconEventKey,"CH0", mapChldKeyValue);*/
                
                
                recService.finishReconciliationEvent(reconEventKey); 
                recService.processReconciliationEvent(reconEventKey);
                
                //processing event
               //recService.callingEndOfJobAPI();     
                System.out.println("Completed Recon Event Operation -> "+reconEventKey);
         }
                
              }catch(Exception e){
                  e.printStackTrace();              
              }
	  
   }

}