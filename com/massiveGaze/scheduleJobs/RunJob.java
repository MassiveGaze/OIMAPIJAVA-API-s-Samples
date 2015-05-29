package com.massiveGaze.scheduleJobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import oracle.iam.identity.utils.Constants;

public class RunJob {
	public void testBug18590285() throws Exception
    {
      Long time = System.currentTimeMillis();
        // Run LDAP User Create and Update Full Reconciliation
        System.out.println("Run LDAP User Create and Update Full Reconciliation");
       // SchedulerUtils.triggerJob(schedulerService, "LDAP User Create and Update Full Reconciliation");
       
    }

}
