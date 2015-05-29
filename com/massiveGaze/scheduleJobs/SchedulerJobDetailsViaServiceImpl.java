package com.massiveGaze.scheduleJobs;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import oracle.iam.scheduler.api.SchedulerService;
import oracle.iam.scheduler.exception.SchedulerException;
import oracle.iam.scheduler.vo.JobDetails;
import oracle.iam.scheduler.vo.JobParameter;
import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcUserNotFoundException;
import Thor.API.Operations.tcSchedulerOperationsIntf;

import com.massiveGaze.connection.Platform;
import com.thortech.xl.vo.TaskAttributeDetails;

public class SchedulerJobDetailsViaServiceImpl {
	private static SchedulerService schedulerService  = Platform.getService(SchedulerService.class);
	public static void main(String[] args) throws SchedulerException {
		// TODO Auto-generated method stub
		HashMap<String, String> tsaParams = new HashMap<String, String>();
	//	tsaParams.put("TSA_NAME", "Latest Token");
		try {
			new SchedulerJobDetailsViaServiceImpl().testStopAllScheduledTasks();
		} catch (tcAPIException | tcUserNotFoundException
				| tcColumnNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*Vector jobsParams = filterJobsAttribute(tsaParams);
		Iterator itr = jobsParams.iterator();
		while (itr.hasNext()) {
			System.out.println("**********************************************");
			TaskAttributeDetails taskAttributeDetails = (TaskAttributeDetails) itr.next();
			System.out.println(" TSA_KEY -> "+taskAttributeDetails.getTsaKey());
			System.out.println(" TSA_NAME -> "+taskAttributeDetails.getTsaName());
			System.out.println(" TSA_VALUE -> "+taskAttributeDetails.getTsaValue());
			System.out.println(" TSK_KEY -> "+taskAttributeDetails.getTskKey());
			System.out.println("**********************************************");
		}
		*/
	}
	
	public static Vector filterJobsAttribute(HashMap<String, String> tsaParams) throws SchedulerException{
		Vector jobsFiltered = new Vector();
		String tskKey= tsaParams.get("TSK_KEY");
		String tsaName = tsaParams.get("TSA_NAME");
		String tsaValue = tsaParams.get("TSA_VALUE");
		Vector filteredAttributes = new Vector();
		String hashKey;
		JobDetails[] jds =schedulerService.getAllJobDetails();
		//JobDetails[] jds =schedulerService.getJobsOfSchedulerTask("Evaluate User Policies");
		System.out.println("------------------------------------------->"+schedulerService.getStatusOfJob("Evaluate User Policies"));
        System.out.println("------------------------------------------->"+schedulerService.getStatus());
		System.out.println("Lenght of AllJobDetails "+jds.length);
		if(null!=jds){
			for(int i =0;i<jds.length ;i++)	{
				JobDetails jobDetail = jds[i];
				Vector v = new Vector();
				v.addElement(jobDetail);
				jobsFiltered.addElement(v);
			}
		}
		
		JobDetails jobDetail = null;
		System.out.println("Lenght of jobsFiltered Having vector Objects"+jobsFiltered.size());
		Iterator itr = jobsFiltered.iterator();
		while (itr.hasNext()) {		
			System.out.println("");
			System.out.println("");
			System.out.println("");
			Vector v = (Vector) itr.next();
			System.out.println("Lenght of Vector Objects Inside  1 jobsFiltered Object is "+v.size());
			Iterator itrInner = v.iterator();			

			while (itrInner.hasNext()) {
				Object obj = itrInner.next();				
				if (obj instanceof JobDetails) {
					jobDetail = (JobDetails) obj;
				}
				
			}
			System.out.println("");
			if(null!=jobDetail){
				System.out.println("JobDetails Object Getting Params ");
				HashMap<String, JobParameter> jobParameters = jobDetail.getParams();
				tskKey= jobDetail.getTaskKey();
				System.out.println( "  name               : " + jobDetail.getName());
				System.out.println( "  class name         : " + jobDetail.getTaskClassName());
				System.out.println( "  task key           : " + jobDetail.getTaskKey());
				System.out.println( "  task name          : " + jobDetail.getTaskName());
				System.out.println( "  method             : " + jobDetail.getMethod());
				System.out.println( "  job schedule type  : " + jobDetail.getJobScheduleType());
				System.out.println( "  job listener       : " + jobDetail.getJobListener());
				System.out.println( "  cron schedule type : " + jobDetail.getCronScheduleType());				
				System.out.println( "  last modify date   : " + dateFormat().format(jobDetail.getLastModifyDate()));
				System.out.println( "  retry count        : " + jobDetail.getRetrycount());				
				if(null!=jobParameters && jobParameters.size()>0){
					Iterator itrJobParams = jobParameters.keySet().iterator();
					while (itrJobParams.hasNext()) {
						hashKey=itrJobParams.next().toString();
						Object obj = jobParameters.get(hashKey);
						JobParameter jp = null;
						String jobParamTSAKey = "";
						String jobParamValue = "";
						String jobParamName = "";
						if (null != obj && obj instanceof JobParameter) {
							jp = (JobParameter) obj;
							jobParamName = jp.getName();
							jobParamTSAKey = jp.getParameterKey();
							System.out.println( "      data type     : " + jp.getDataType());
							System.out.println( "      name          : " + jp.getName());
							System.out.println( "      parameter key : " + jp.getParameterKey());
							Serializable val = jp.getValue();
							System.out.println( "      value         : " + val);							
							System.out.println();
							if (null != tsaName && null!=tsaValue) {								
								String key = tsaName;
								String value = tsaValue;
								if (null != tskKey && !"".equals(jobParamValue)	&& null != value && key.equals(jobParamName) && value.equals(jobParamValue)) {
									TaskAttributeDetails taskAttributeDetails = new TaskAttributeDetails();
									taskAttributeDetails.setTsaKey(jobParamTSAKey);
									taskAttributeDetails.setTsaName(jobParamName);
									taskAttributeDetails.setTsaValue(jobParamValue);
									taskAttributeDetails.setTskKey(tskKey);
									filteredAttributes.add(taskAttributeDetails);
								}							

							}else if(null!=tsaName)	{
								String key = tsaName;								
								if (null != tskKey &&  key.equals(jobParamName)) {								
									TaskAttributeDetails taskAttributeDetails = new TaskAttributeDetails();
									taskAttributeDetails.setTsaKey(jobParamTSAKey);
									taskAttributeDetails.setTsaName(jobParamName);
									taskAttributeDetails.setTsaValue(jobParamValue);
									taskAttributeDetails.setTskKey(tskKey);
									filteredAttributes.add(taskAttributeDetails);	
									System.out.println(" 	----------------------------------------");
									System.out.println(" 	TSA_KEY -> "+jobParamTSAKey);
									System.out.println(" 	TSA_NAME 	-> "+jobParamName);
									System.out.println("	TSA_VALUE  -> "+jobParamValue);
									System.out.println(" 	TSK_KEY		    -> "+tskKey);
									System.out.println(" 	----------------------------------------");
								}
	
							}
						}
					}
				}else{
					System.out.println(" 	jobParameters Null in the task Name -> "+jobDetail.getTaskName());
				}
			}			
		}
		return filteredAttributes;
	}
	
	
	public static SimpleDateFormat dateFormat(){
		SimpleDateFormat simpleDate = new SimpleDateFormat("DDMMYY");	
		String x=(String)null;
		return simpleDate;
	}
	
	
	
    public void testStopAllScheduledTasks() throws tcAPIException,   tcUserNotFoundException, tcAPIException,    tcColumnNotFoundException {
			tcSchedulerOperationsIntf schedIntf = Platform.getService(tcSchedulerOperationsIntf.class);//getConnection().getSchedulerOperationsUtility();
			SchedulerService schService = Platform.getService(SchedulerService.class);//getConnection().getSchedulerManager();
			Writer writer = null;
			Date startDate = new Date();
			// Define a list of tasks that should not be deactivated (native tasks of OIM)
			List<String> nativeTasks = Arrays.asList("Attestation Grace Period Expiry Checker","Automated Retry of Failed Async Task","Automatically Unlock User","Enable User After Start Date","Initiate Attestation Processes","Issue Audit Messages Task","Orchestration Process Cleanup Task","Password Expiration Task","Password Warning Task","Request Execution Scheduled Task","Retry Failed Reconciliation Events","Set User Deprovisioned Date","Set User Provisioned Date","Task Escalation","Task Timed Retry");
			try {
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ScheduledTasks.csv"), "utf-8"));
			// Search all disabled tasks. Interface class is only one that returns disabled status, but is very slow. So use a single request to recover all disabled tasks
			// (There are much more active than disabled tasks).
				HashMap attrs = new HashMap();
				attrs.put("Task Scheduler.Disable","1");
				tcResultSet disabledTasks = schedIntf.findScheduleTasks(attrs);
				System.out.println("Found " + disabledTasks.getTotalRowCount() + " disabled schedules.");
				// Recover a list of all scheduled tasks. Service class is much faster for this purpose.
				JobDetails[] jobDetails = schService.getAllJobDetails();
				for (JobDetails job : jobDetails){
				// Check, if task is scheduled.
				if ("Periodic".equalsIgnoreCase(job.getJobScheduleType())){
				// Check, if task is one of the native ones
				if (!nativeTasks.contains(job.getName())){
				// Check, if task is already disabled.
				boolean foundDisabled = false;
				if (disabledTasks != null && disabledTasks.getTotalRowCount() > 0){
				for (int i = 0; i < disabledTasks.getTotalRowCount(); i++){
				 disabledTasks.goToRow(i);
				 if (job.getName().equals(disabledTasks.getStringValue("Task Scheduler.Name"))){
				     foundDisabled = true;
				     break;
				 }
				}
				} 
				if (!foundDisabled){
				//Disable task.
				long taskKey = Long.parseLong(job.getTaskKey());
				HashMap updates = new HashMap();
				updates.put("Task Scheduler.Disable","1");
				schedIntf.updateScheduleTask(taskKey, updates);
				// Write task name in file.
				writer.write(job.getName() + "\n");
				System.out.println("Task " + job.getName() + " was successfully disabled.");
				}
				}
				}
				}
				Date endDate = new Date();
				long diff = endDate.getTime() - startDate.getTime();
				System.out.println("Took " + diff / 1000 + " seconds.");
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			} finally {
			try {
				writer.close();
				} catch (Exception ex) {}
	}
	
	
}
	
	
		
		
}
