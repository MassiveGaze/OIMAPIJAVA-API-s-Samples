/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.massiveGaze.scheduler;

import com.massiveGaze.connection.Platform;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.iam.scheduler.api.SchedulerService;
import oracle.iam.scheduler.exception.IncorrectScheduleTaskDefinationException;
import oracle.iam.scheduler.exception.LastModifyDateNotSetException;
import oracle.iam.scheduler.exception.ParameterValueTypeNotSupportedException;
import oracle.iam.scheduler.exception.RequiredParameterNotSetException;
import oracle.iam.scheduler.exception.SchedulerAccessDeniedException;
import oracle.iam.scheduler.exception.SchedulerException;
import oracle.iam.scheduler.impl.util.SchedulerUtil;
import oracle.iam.scheduler.vo.JobDetails;
import oracle.iam.scheduler.vo.ScheduledTask;

/**
 *
 *
 */
public class LookupSchedulerTask {
    private static SchedulerService schedulerService  = Platform.getService(SchedulerService.class);
    
     public void updateJob(JobDetails job)
                  throws SchedulerException, RequiredParameterNotSetException,
                         ParameterValueTypeNotSupportedException,
                         IncorrectScheduleTaskDefinationException,
                         LastModifyDateNotSetException,
                         SchedulerAccessDeniedException {
         
         schedulerService.updateJob(job);
         
     }
    public static void main(String[] args) throws IncorrectScheduleTaskDefinationException{
        
         try {
             JobDetails[] job =schedulerService.getJobsOfSchedulerTask("LDAP Role Membership Reconciliation");
             new LookupSchedulerTask().updateJob(job[0]);
             
         } catch (SchedulerException ex) {
             Logger.getLogger(LookupSchedulerTask.class.getName()).log(Level.SEVERE, null, ex);
         } catch (RequiredParameterNotSetException ex) {
             Logger.getLogger(LookupSchedulerTask.class.getName()).log(Level.SEVERE, null, ex);
         } catch (ParameterValueTypeNotSupportedException ex) {
             Logger.getLogger(LookupSchedulerTask.class.getName()).log(Level.SEVERE, null, ex);
         } catch (LastModifyDateNotSetException ex) {
             Logger.getLogger(LookupSchedulerTask.class.getName()).log(Level.SEVERE, null, ex);
         } catch (SchedulerAccessDeniedException ex) {
             Logger.getLogger(LookupSchedulerTask.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
}
