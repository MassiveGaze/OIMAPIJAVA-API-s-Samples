/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.massiveGaze.scheduler;

import com.massiveGaze.connection.Platform;
import oracle.iam.scheduler.api.SchedulerService;
import oracle.iam.scheduler.exception.IncorrectScheduleTaskDefinationException;
import oracle.iam.scheduler.exception.SchedulerException;
import oracle.iam.scheduler.vo.JobDetails;
import oracle.iam.scheduler.vo.ScheduledTask;

/**
 *
 *
 */
public class SchedulerJobsStatus {
     SchedulerService schedulerService = Platform.getService(SchedulerService.class);
    public void getDetails() throws IncorrectScheduleTaskDefinationException{
         ScheduledTask task=schedulerService.lookupScheduledTask("LDAP Role Hierarchy Reconciliation");
         System.out.println(""+task.getParameters());
    }
    
    public void getJobStatusEnable_OR_Disbale() throws SchedulerException{
       
       //String[] taskNames = schedulerService.getAllJobs();
        String[] taskNames =schedulerService.getJobs("LDAP Role Hierarchy Reconciliation");
     if ((taskNames != null) && (taskNames.length != 0)) {
       for (String taskname : taskNames) {
         JobDetails eachTaskDetail = schedulerService.getJobDetail(taskname);
            System.out.println(eachTaskDetail.getCronScheduleType());
            System.out.println(eachTaskDetail.getJobListener());
            System.out.println(eachTaskDetail.getJobScheduleType());
        
         if(eachTaskDetail.isTaskStatus()){
                System.out.println( "  Name  of Task Is : '" + eachTaskDetail.getName() +"' nad it is in ENABLE State. ");
         }else{
                System.out.println( "  Name  of Task Is : '" + eachTaskDetail.getName() +"' nad it is in "+eachTaskDetail.isTaskStatus()+" DISABLE State. ");
         }

         //to enable/disable a job true/false
        /*  eachTaskDetail.setTaskStatus(true);
         
           try {
               schedulerService.updateJob(eachTaskDetail);
           } catch (RequiredParameterNotSetException ex) {
               Logger.getLogger(SchedulerJobsStatus.class.getName()).log(Level.SEVERE, null, ex);
           } catch (ParameterValueTypeNotSupportedException ex) {
               Logger.getLogger(SchedulerJobsStatus.class.getName()).log(Level.SEVERE, null, ex);
           } catch (IncorrectScheduleTaskDefinationException ex) {
               Logger.getLogger(SchedulerJobsStatus.class.getName()).log(Level.SEVERE, null, ex);
           } catch (StaleDataException ex) {
               Logger.getLogger(SchedulerJobsStatus.class.getName()).log(Level.SEVERE, null, ex);
           } catch (LastModifyDateNotSetException ex) {
               Logger.getLogger(SchedulerJobsStatus.class.getName()).log(Level.SEVERE, null, ex);
           } catch (SchedulerAccessDeniedException ex) {
               Logger.getLogger(SchedulerJobsStatus.class.getName()).log(Level.SEVERE, null, ex);
           } */
       }  
     } 

    }
    public static void main(String[] args) throws Exception{
		new SchedulerJobsStatus().getJobStatusEnable_OR_Disbale();                
                //new SchedulerJobsStatus().getDetails();
	}
    
    
    
}
