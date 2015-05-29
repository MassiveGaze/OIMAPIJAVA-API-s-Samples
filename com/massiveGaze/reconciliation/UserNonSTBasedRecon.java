package com.massiveGaze.reconciliation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import oracle.iam.platform.context.ContextAwareNumber;
import oracle.iam.platform.context.ContextAwareString;
import oracle.iam.platform.context.ContextManager;
import oracle.iam.platform.utils.SuperRuntimeException;
import oracle.iam.reconciliation.api.ReconOperationsService;
import oracle.iam.reconciliation.impl.Constants;

import org.junit.Test;

import com.massiveGaze.connection.Platform;
public class UserNonSTBasedRecon{

    private AtomicInteger threadCount =new AtomicInteger(0);

    @Test
    public void testCreateUsersUsingNonScheduleTaskConnectorWithThreads() throws Exception {

        Thread t = new CreateEvent();
        t.start();
        Thread t2 = new CreateEvent();
        t2.start();

        while (true) {
            Thread.currentThread().sleep(5000);
            if (threadCount.get() == 2){
                ReconOperationsService reconServ =Platform.getService(ReconOperationsService.class);
                reconServ.callingEndOfJobAPI();
                break;
            }
        }
    }

    public class CreateEvent extends Thread {

        @Override
        public void run() {

            String uniq2 = "MBANAPPA";
            long jobId = getRandomNumber();
            ContextManager.setValue(Constants.JOB_HISTORY_ID, new ContextAwareNumber(jobId));
            ContextManager.setValue(Constants.JOB_NAME_CONTEXT, new ContextAwareString(jobId +""));
            ReconOperationsService recon;
            try {

                recon = Platform.getService(ReconOperationsService.class);
                int count = 50;
                HashMap<String, String> hm = new HashMap<String, String>();
                ArrayList<Long> eventKeys = new ArrayList<Long>();
                for (int i = 0; i < count; i++) {
                    hm.put("UserLogin", uniq2 + "ThreadTest" + i);
                    hm.put("FirstName", uniq2 + "Thread" + i);
                    hm.put("lastname", "Test");
                    hm.put("Type", "End-User");
                    hm.put("OrganizationName", "Xellerate Users");
                    hm.put("EmpType", "Full-Time");
                    hm.put("Middlename", "MID");
                    System.out.println("Creating Recon event i ="+ i);
                    long rceKey = recon.createReconciliationEvent("XellerateUser", hm, true);
                    eventKeys.add(rceKey);
                }
                assertEquals(count, eventKeys.size());
            } catch (Exception e) {
                throw new SuperRuntimeException(e.getMessage(), e);
            } finally {
                threadCount.set(threadCount.get()+1);
                ContextManager.popContext();
            }
        }
        public  int getRandomNumber() {
            java.util.Random r = new java.util.Random(Calendar.getInstance() .getTimeInMillis());
            int randint = Math.abs(r.nextInt()) % 1000;     
            return randint;
        }
    }
    
    
}