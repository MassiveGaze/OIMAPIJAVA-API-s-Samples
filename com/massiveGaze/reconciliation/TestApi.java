package com.massiveGaze.reconciliation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;

import Thor.API.Operations.tcLookupOperationsIntf;

import com.massiveGaze.connection.Platform;
import com.massiveGaze.connection.DataSource;

import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.identity.usermgmt.vo.UserManagerResult;
import oracle.iam.reconciliation.api.ChangeType;
import oracle.iam.reconciliation.api.EventAttributes;
import oracle.iam.reconciliation.api.ReconOperationsService;
import oracle.iam.scheduler.api.SchedulerService;
import oracle.iam.scheduler.exception.SchedulerException;

public class TestApi {
    private static String uniq;
    public static Long usrId;
    private ReconOperationsService reconIntfTest = (ReconOperationsService) Platform.getService(ReconOperationsService.class);;
    protected Connection conn= DataSource.getConnection();
    public static long udUserKey;
    
    public static void main(String[] args) throws NumberFormatException, Exception {
    	new TestApi().testAudJmsEntriesCreatedForEntitlementLDAPGroupsEvenIfNoChange();
    }
    
	public void testAudJmsEntriesCreatedForEntitlementLDAPGroupsEvenIfNoChange() throws NumberFormatException, Exception {
		 uniq = getRandomLong(10000).toString();
		 ResultSet rs = null;
		 Statement stmt = conn.createStatement();
		  rs = stmt.executeQuery("select OBJ_KEY from OBJ where OBJ_NAME='OID Group'" );
	        rs.next();
	        int objKey = rs.getInt("OBJ_KEY");
	        assertEquals(false, rs.next());
	        rs.close();
	        
		tcLookupOperationsIntf lookupIntf = Platform.getService(tcLookupOperationsIntf.class);
		lookupIntf.addLookupValue("Lookup.OID.Group", objKey+"~cn=ManjuGroup"+uniq+",cn=Groups,dc=us,dc=oracle,dc=com", "OID Server~ManjuGroup"+uniq,"en", "US");
		lookupIntf.addLookupValue("Lookup.OID.Group", objKey+"~cn=ManjuGroup"+(uniq+1)+",cn=Groups,dc=us,dc=oracle,dc=com", "OID Server~ManjuGroup"+(uniq+1),"en", "US");
		
			triggerNowScheduledTask("Entitlement List");
	        Thread.sleep(2000);
	        
	        triggerNowScheduledTask("Catalog Synchronization Job");
	        Thread.sleep(2000);
	        
		
	        HashMap<String, Object> usrMap = new HashMap<String, Object>();
	        usrMap.put("act_key", 1l);
	        usrMap.put("User Login", "ManjunathB" + uniq);
	        usrMap.put("First Name", "Manjunath" + uniq);
	        usrMap.put("Last Name", "B"+uniq);
	        usrMap.put("Xellerate Type", "End-User");
	        usrMap.put("Role", "Full-Time");
	        
	      //usrMap.put("usr_password", "Welcome1");
	        usrId = new Long(createUserForAccount(usrMap));

	        HashMap<String, Object> hm = new HashMap<String, Object>();
	        hm.put("User ID", "ManjunathB" + uniq);
	        hm.put("IT Resource Name", "OID Server");	    
	       // hm.put("Password", "welcome1");
	        hm.put("First Name", "Manjunath" + uniq);
	        hm.put("Last Name", "B"+uniq);
	        hm.put("Common Name", "ManjunathB"+ uniq);	
	        hm.put("Container DN", "ou=manjunath,dc=us,dc=oracle,dc=com");
	        hm.put("orclGuid", "abcd1234" + uniq);
	        
            EventAttributes eventAttr = new EventAttributes();
            eventAttr.setEventFinished(false);  
            eventAttr.setChangeType(ChangeType.CHANGELOG);
            
	        long eventId = reconIntfTest.createReconciliationEvent("OID User", hm, eventAttr);

	        reconIntfTest.providingAllMultiAttributeData(eventId, "UserGroup", true);	        
	     // Child Map : Group
	        HashMap m = new HashMap();
	        m.put("GroupName", objKey+"~cn=ManjuGroup"+uniq+",cn=Groups,dc=us,dc=oracle,dc=com");
	        reconIntfTest.addMultiAttributeData(eventId, "UserGroup", m);
	        m = new HashMap();
	        m.put("GroupName", objKey+"~cn=ManjuGroup"+(uniq+1)+",cn=Groups,dc=us,dc=oracle,dc=com");
	        reconIntfTest.addMultiAttributeData(eventId, "UserGroup", m);    
	     
	        
	        reconIntfTest.finishReconciliationEvent(eventId);
	        reconIntfTest.processReconciliationEvent(eventId);
	        
	        reconIntfTest.callingEndOfJobAPI();

	        System.out.println("Event Id Created" + eventId);
	        assertTrue("Event Id should be greater then 0 but was " + eventId, eventId > 0);
	        	
	        wait(eventId, stmt, "Creation Succeeded", 25, 5000l, true);

	        rs = stmt.executeQuery("select count(1) as TOTAL from AUD_JMS where IDENTIFIER=" + usrId );
	        rs.next();
	        int audJmscountBeforeModifyusrDetails = rs.getInt("TOTAL");
	        assertEquals(false, rs.next());
	        rs.close();
	        System.out.println(" AUD_JMS count after Provisioning Account and 2 Group Entitlemnts -> " + audJmscountBeforeModifyusrDetails);
	        
	        triggerNowScheduledTask("Issue Audit Messages Task");
	        Thread.sleep(2000);
	        
	        rs = stmt.executeQuery("select count(*) as count from AUD_JMS where IDENTIFIER =" + usrId);
	        rs.next();
	        assertEquals(0l, rs.getLong("count"));
	        rs.close();
	        
	        hm = new HashMap<String, Object>();
	        hm.put("User ID", "ManjunathB" + uniq);
	        hm.put("IT Resource Name", "OID Server");	    
	       // hm.put("Password", "welcome1");
	        hm.put("First Name", "Manjunath_Modified" + uniq);
	        hm.put("Last Name", "B_Modified"+uniq);
	        hm.put("Common Name", "ManjunathB"+ uniq);	
	        hm.put("Container DN", "ou=manjunath,dc=us,dc=oracle,dc=com");
	        hm.put("orclGuid", "abcd1234" + uniq);
	        eventId = reconIntfTest.createReconciliationEvent("OID User", hm, eventAttr);
	        
	        reconIntfTest.finishReconciliationEvent(eventId);
	        reconIntfTest.processReconciliationEvent(eventId);	        
	        reconIntfTest.callingEndOfJobAPI();

	        System.out.println("Event Id Created After Modify " + eventId);//, eventId > 0);
	        assertTrue("Event Id should be greater then 0 but was " + eventId, eventId > 0);
	       
	        wait(eventId, stmt, "Update Succeeded", 25, 5000l, true);

	        rs = stmt.executeQuery("select count(1) as TOTAL from AUD_JMS where IDENTIFIER=" + usrId );
	        rs.next();
	        int audJmscountAfterModifyusrDetails = rs.getInt("TOTAL");
	        assertEquals(false, rs.next());
	        rs.close();
	        System.out.println(" AUD_JMS count after Modifying Provisioning Account user Details -> " + audJmscountAfterModifyusrDetails);
	        
	        triggerNowScheduledTask("Issue Audit Messages Task");
	        Thread.sleep(2000);
	        rs = stmt.executeQuery("select count(*) as count from AUD_JMS where IDENTIFIER =" + usrId);
	        rs.next();
	        assertEquals(0l, rs.getLong("count"));
	        rs.close();
	        
	        stmt.close();
	        conn.close();
	        assertTrue(" audJmscountBeforeModifyusrDetails is greater than  audJmscountAfterModifyusrDetails ? -> " + eventId, audJmscountBeforeModifyusrDetails > audJmscountAfterModifyusrDetails);
	        System.out.println("*********************END of test case******************");	        
	}

	 protected boolean triggerNowScheduledTask(String taskName) throws Exception {
	        SchedulerService svc = Platform.getService(SchedulerService.class);
	        try {
	            svc.triggerNow(taskName);
	            return true;
	        } catch (SchedulerException e) {
	            e.printStackTrace();
	        }
	        return false;

	    }
    public Long getRandomLong(int maxValue) {
        Random random = new Random();
        long token = random.nextInt(maxValue);
        return token;
    }
    
    private String createUserForAccount(HashMap usrMap) throws Exception {
        UserManager usrMgr = (UserManager) Platform.getService(UserManager.class);
        UserManagerResult result = usrMgr.create(new User(null, usrMap));
        System.out.println("user created with userkey " + result.getEntityId());
        return result.getEntityId();
    }
    protected void wait(long reKey, Statement stmt, String eventStatus, int count, long sleepTime, boolean failCheck) throws SQLException, InterruptedException {
        ResultSet rs = null;
        String es = null;
        String eventNote = null;
        for (int counter = 0; counter < count; counter++) {
            rs = stmt.executeQuery("select * from RECON_EVENTS where RE_KEY=" + reKey);
            rs.next();
            es = rs.getString("RE_STATUS");
            eventNote = rs.getString("RE_NOTE");
            rs.close();
            if (!eventStatus.equals(es) && ((failCheck && !((null != es) && es.endsWith("Failed"))) || !failCheck)) {
                Thread.sleep(sleepTime);
                continue;
            }
            break;
        }
        assertEquals("ReKey=" + reKey + " note=" + eventNote, eventStatus, es);
    }
}
