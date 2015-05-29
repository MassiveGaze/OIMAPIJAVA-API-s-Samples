package com.massiveGaze.provision;
import java.sql.Connection;
import java.sql.Statement;

import com.massiveGaze.connection.DataSource;


public class CreateEntitlement {
	public static void main(String[] args) throws Exception {
		String codeValue="VISA_C";
		String decodeValue="VISA_D";
		String lKU_Key="1621";
		
		String insertLKVQuery="INSERT INTO LKV (LKV_KEY,LKU_KEY,LKV_ENCODED,LKV_DECODED,LKV_LANGUAGE,LKV_COUNTRY,  LKV_VARIANT,LKV_DISABLED,LKV_DATA_LEVEL,LKV_CREATE,LKV_CREATEBY,LKV_UPDATE,"+
        " LKV_UPDATEBY,LKV_NOTE,LKV_ROWVER)  select LKV_SEQ.NEXTVAL as LKV_KEY, LKU_KEY as LKU_KEY, '" + codeValue + "'||LKV_SEQ.NEXTVAL as LKV_ENCODED, '"+decodeValue+"'||LKV_SEQ.NEXTVAL as LKV_DECODED,"+
        "LKV_LANGUAGE,LKV_COUNTRY,LKV_VARIANT,LKV_DISABLED,LKV_DATA_LEVEL,  LKV_CREATE,LKV_CREATEBY,LKV_UPDATE,LKV_UPDATEBY,NULL AS LKV_NOTE,LKV_ROWVER  from LKV"+
        " where LKU_KEY= "+lKU_Key;
		
		
		System.out.println("Query -> " +insertLKVQuery);
		 Connection con= DataSource.getConnection();
		 Statement statement =con.createStatement();
		 for(int i=0;i<2;i++){
			 statement.execute(insertLKVQuery);
		 }
		 DataSource.closeConnection();
		 
     /* String insertLKUData =" INSERT INTO LKU (LKU_KEY,LKU_TYPE,LKU_TYPE_STRING_KEY,LKU_MEANING,LKU_CREATE,LKU_CREATEBY,LKU_UPDATE,LKU_UPDATEBY,LKU_ROWVER) VALUES (lku_seq.nextval,?,?,?,sysdate,1,sysdate,1,?)";
      Connection con= DataSource.getConnection();
      PreparedStatement prepareStatement =con.prepareStatement(insertLKUData);
      prepareStatement.setString(1, "1");
      prepareStatement.setString(1, "Lookup.disconnected1"); // Name of Lookup which should be Unique
      prepareStatement.setString(1, "Look Up Created for Entitlements");
      prepareStatement.setString(1, "1");
      prepareStatement.setString(1, "1");
      prepareStatement.setString(1, "0000000000000000");
      ResultSet rs=prepareStatement.executeQuery();
      String lookUpKey="";
      while(rs.next()){
    	  lookUpKey= rs.getString("LKU_KEY");
    	  System.out.println(" Look Up Created with Key as ->"+lookUpKey);
      }
      String code ="C";
      String decode="D";
      String insertLKVdata ="   insert into lkv   (LKV_KEY,LKU_KEY,LKV_ENCODED,LKV_DECODED,LKV_LANGUAGE,LKV_COUNTRY,   LKV_VARIANT,LKV_DISABLED,LKV_DATA_LEVEL,LKV_CREATE,LKV_CREATEBY,LKV_UPDATE,   LKV_UPDATEBY,LKV_NOTE,LKV_ROWVER)"
   +" SELECT LKV_SEQ.NEXTVAL AS LKV_KEY, LKU_KEY AS LKU_KEY, "+ code +"||lkv_seq.nextval as lkv_encoded,"+decode+ "||lkv_seq.nextval as lkv_decoded,   lkv_language,lkv_country,lkv_variant,lkv_disabled,lkv_data_level, "
   +" lkv_create,lkv_createby,lkv_update,lkv_updateby,null as lkv_note,lkv_rowver   FROM LKV   where lku_key= "+lookUpKey;
      
      
        EntitlementService entSvc = Platform.getService(EntitlementService.class);
        
        Entitlement ent = new Entitlement();
        
        for(int i=0;i<2;i++){
        //ent.setEntitlementKey(8);
        ent.setObjectKey(41);//Object_Key From AppInstance Table.
        ent.setFormKey(62); //SDK_KEY from SDK table which creates Once we create AppInstance.
        ent.setFormFieldKey(218); //SDC_KEY from SDC table where SDK_KEY=62
        ent.setItResourceKey(41);//IT_Resource Key from AppInstance Field
        //Code-Decode Value need to set For Created LookUp
        ent.setEntitlementCode("Entit-"+i); //Name to keep ENt_Code into ENt_LIST Table
        ent.setEntitlementValue("Entit-"+i);//Value For the Above Code
        ent.setDisplayName("Entit-"+i);
        ent.setDescription("Entit-"+i);
        ent.setLookupValueKey(2479);
        ent.setValid(true); 
        
        entSvc.addEntitlement(ent);
        }*/
	}
}