/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.massiveGaze.scheduler;

/**
 *
 *
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import oracle.iam.scheduler.vo.JobDetails;
import oracle.iam.scheduler.vo.JobParameter;

import org.quartz.JobDataMap;

import com.massiveGaze.connection.DataSource;
public class GetSchedulerJobDetailsFromBLOB  {	
	
	public void execute() throws Exception {
		String mname = "execute()";
		String jname = "LDAP Role Membership Reconciliation";//Job Name  which is having issue.
		jname=jname.replaceAll("\\*", "%");
		if (null == jname)
			throw new NullPointerException("JobName is null.");
		jname = jname.replace('*', '%');
		String query = "select job_data from qrtz92_job_details where job_name like '" + jname + "'";
		try {
            PreparedStatement prepStmt = DataSource.getConnection().prepareStatement(query);
           
			ResultSet rs = prepStmt.executeQuery();
			boolean found = false;
			while(rs.next()) {
				found = true;
				Blob b = rs.getBlob("job_data");
				InputStream bis = b.getBinaryStream();
				ObjectInputStream ois = new ObjectInputStream(bis);
				Object obj = ois.readObject();
				if(obj instanceof JobDataMap) {
					print((JobDataMap) obj);
				}				
                            ois.close();
                            bis.close();
			}
			rs.close();
            prepStmt.close();
			if(!found) {
				System.out.println("Job Not found : " + jname);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			DataSource.closeConnection();
        }
	}
	
	private void print(JobDataMap jdm) {
		String mname = "print(JobDataMap)";		
		String[] keys = jdm.getKeys();
		for(String key : keys) {
			Object ov = jdm.get(key);
			if(ov instanceof JobDetails[]) {
				System.out.println( key + " : ");
				JobDetails[] jds = (JobDetails[]) ov;
				for(JobDetails jd : jds) {
					System.out.println( "  name               : " + jd.getName());
					System.out.println( "  class name         : " + jd.getTaskClassName());
					System.out.println( "  task key           : " + jd.getTaskKey());
					System.out.println( "  task name          : " + jd.getTaskName());
					System.out.println( "  method             : " + jd.getMethod());
					System.out.println( "  job schedule type  : " + jd.getJobScheduleType());
					System.out.println( "  job listener       : " + jd.getJobListener());
					System.out.println( "  cron schedule type : " + jd.getCronScheduleType());
					System.out.println( "  last modify date   : " + dateFormat().format(jd.getLastModifyDate()));
					System.out.println( "  retry count        : " + jd.getRetrycount());
					HashMap<String,JobParameter> hmattr = jd.getAttributes();
					System.out.println( "  Attributes : ");
					if(null != hmattr)
					for(String attr : hmattr.keySet()) {
						System.out.println( "    attr : " + attr);
						JobParameter av = hmattr.get(attr);
						System.out.println( "      data type     : " + av.getDataType());
						System.out.println( "      name          : " + av.getName());
						System.out.println( "      parameter key : " + av.getParameterKey());
						Serializable val = av.getValue();
						System.out.println( "      value         : " + val);
					}
					if(jd.getTaskName()!=null){
						jd.setTaskName(null);
					}
					/*HashMap<String,JobParameter> hmparam = jd.getParams();
					if(null != hmparam)
					for(String param : hmparam.keySet()) {
						System.out.println("    param : " + param);
						JobParameter av = hmparam.get(param);
						System.out.println( "      data type     : " + av.getDataType());
						System.out.println( "      name          : " + av.getName());
						System.out.println( "      parameter key : " + av.getParameterKey());
						Serializable val = av.getValue();
						System.out.println( "      value         : " + val);
					}*/
				}
			} else {
				System.out.println( key + " = " + ov);
			}
		}
		
	}
	
	public SimpleDateFormat dateFormat(){
		SimpleDateFormat simpleDate = new SimpleDateFormat("DDMMYY");		
		return simpleDate;
	}
	
	public static void main(String[] args) throws Exception{
		new GetSchedulerJobDetailsFromBLOB().execute();                
	}
	
}
