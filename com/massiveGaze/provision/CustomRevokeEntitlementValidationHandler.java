package com.massiveGaze.provision;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

import oracle.core.ojdl.logging.ODLLogger;
import oracle.iam.platform.Platform;
import oracle.iam.platform.authopss.exception.AccessDeniedException;
import oracle.iam.platform.kernel.ValidationException;
import oracle.iam.platform.kernel.ValidationFailedException;
import oracle.iam.platform.kernel.spi.ValidationHandler;
import oracle.iam.platform.kernel.vo.BulkOrchestration;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.exception.EntitlementInstanceNotFoundException;
import oracle.iam.provisioning.exception.EntitlementNotProvisionedException;
import oracle.iam.provisioning.resources.LRB;
import oracle.iam.provisioning.util.ProvisioningUtil;
import oracle.iam.provisioning.vo.EntitlementInstance;

import com.thortech.xl.dataaccess.tcClientDataAccessException;
import com.thortech.xl.dataaccess.tcDataProvider;
import com.thortech.xl.dataaccess.tcDataSet;
import com.thortech.xl.dataaccess.tcDataSetException;
import com.thortech.xl.dataobj.PreparedStatementUtil;
import com.thortech.xl.dataobj.util.XLDatabase;
import com.thortech.xl.orb.dataaccess.tcDataAccessException;

public class CustomRevokeEntitlementValidationHandler implements ValidationHandler {
	
	String className = "RevokeEntitlementValidationHandler()::";
	public ODLLogger logger = ODLLogger.getODLLogger("com.provision");
	private String userEntitlementsQuery = "SELECT COUNT(*) AS COUNT FROM ENT_ASSIGN, USR WHERE ENT_ASSIGN.USR_KEY = USR.USR_KEY AND ENT_ASSIGN.ENT_STATUS='Provisioned' AND ENT_ASSIGN.ENT_ASSIGN_KEY=? AND USR.USR_KEY=?";

	@Override
	public void initialize(HashMap<String, String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validate(long processId, long eventId, Orchestration orchestration)
			throws ValidationException, ValidationFailedException {
		String METHOD_NAME = "validate()::", userId="",entitlementInstanceId="";
		logger.entering(className, METHOD_NAME);
		HashMap orchParameters = orchestration.getParameters();
		long entitlementInstanceKey;
		Serializable[] params = null;
		logger.log(Level.FINE, "processid: "+processId+" eventId: "+eventId+" Orchestration parameters: "+orchParameters);
		
		try
	    {
	      userId = (String)orchParameters.get("BeneficiaryKey");
	      logger.logp(Level.INFO, getClass().getName(), METHOD_NAME, "userId: " + userId);
	    }
	    catch (Exception e)
	    {
	      params = new Serializable[] { getClass().getName(), userId, e.getMessage() };
	      logger.logp(Level.SEVERE, getClass().getName(), METHOD_NAME, "IAM-4060000", params);
	      throw ProvisioningUtil.createValidationFailedException("IAM-4060000", null, processId, e, params);
	    }
	    
	    try
	    {
	      entitlementInstanceId = (String)orchParameters.get("EntitlementInstanceKey");
	      logger.logp(Level.INFO, getClass().getName(), METHOD_NAME, "entitlementInstanceId: " + entitlementInstanceId);
	      entitlementInstanceKey = Long.parseLong(entitlementInstanceId);
	    }
	    catch (Exception e)
	    {
	      params = new Serializable[] { getClass().getName(), entitlementInstanceId, e.getMessage() };
	      logger.logp(Level.SEVERE, getClass().getName(), METHOD_NAME, "IAM-40600011", params);
	      throw ProvisioningUtil.createValidationFailedException("IAM-40600011", null, processId, e, params);
	    }
	    
	    tcDataProvider dataProvider = this.getOIMDataProviderInstance(logger);
	    try {
			if(this.isEntitlementAssignedToUser(userId, entitlementInstanceId, dataProvider)){
				ProvisioningService provisioningService = Platform.getService(ProvisioningService.class);
				EntitlementInstance entitlementInstance = provisioningService.getEntitlementInstance(entitlementInstanceKey);
				logger.logp(Level.INFO, getClass().getName(), METHOD_NAME, "Found EntitlementInstance for entitlementInstanceId: " + entitlementInstanceId);
				logger.logp(Level.FINE, getClass().getName(), METHOD_NAME, "EntitlementInstance retrieved: "+entitlementInstance);
				if(entitlementInstance!=null){
					orchestration.addParameter("EntitlementInstance", entitlementInstance);
				}else{
					params = new Serializable[] { getClass().getName(), entitlementInstanceId, userId };
			        logger.logp(Level.SEVERE, getClass().getName(), METHOD_NAME, "IAM-40600013", params);
			        String errorMessage = LRB.DEFAULT.getString("IAM-40600013", params);
			        throw new EntitlementNotProvisionedException("IAM-40600013", errorMessage, params);
				}
				
			}else{
				params = new Serializable[] { getClass().getName(), entitlementInstanceId, userId };
		        logger.logp(Level.SEVERE, getClass().getName(), METHOD_NAME, "IAM-40600013", params);
		        String errorMessage = LRB.DEFAULT.getString("IAM-40600013", params);
		        throw new EntitlementNotProvisionedException("IAM-40600013", errorMessage, params);
			}
		} catch (tcDataAccessException e) {
			params = new Serializable[] { getClass().getName(), entitlementInstanceId, userId };
			logger.log(Level.SEVERE, "tcDataAccessException",e);
		    throw ProvisioningUtil.createValidationFailedException("IAM-40600014", null, processId, e, params);
		} catch (SQLException e) {
			params = new Serializable[] { getClass().getName(), entitlementInstanceId, userId };
			logger.log(Level.SEVERE, "SQLException",e);
		    throw ProvisioningUtil.createValidationFailedException("IAM-40600014", null, processId, e, params);
		} catch (tcDataSetException e) {
			params = new Serializable[] { getClass().getName(), entitlementInstanceId, userId };
			logger.log(Level.SEVERE, "tcDataSetException",e);
		    throw ProvisioningUtil.createValidationFailedException("IAM-40600014", null, processId, e, params);
		} catch (AccessDeniedException e) {
			params = new Serializable[] { getClass().getName(), entitlementInstanceId, userId };
			logger.log(Level.SEVERE, "AccessDeniedException",e);
		    throw ProvisioningUtil.createValidationFailedException("IAM-40600014", null, processId, e, params);
		} catch (EntitlementInstanceNotFoundException e) {
			params = new Serializable[] { getClass().getName(), entitlementInstanceId, userId };
			logger.log(Level.SEVERE, "EntitlementInstanceNotFoundException",e);
		    throw ProvisioningUtil.createValidationFailedException("IAM-40600014", null, processId, e, params);
		} catch (Exception e) {
			params = new Serializable[] { getClass().getName(), entitlementInstanceId, userId };
			logger.log(Level.SEVERE, "GenericProvisioningException",e);
		    throw ProvisioningUtil.createValidationFailedException("IAM-40600014", null, processId, e, params);
		}finally{
			if(dataProvider!=null){
				this.closeDataProvider(dataProvider, logger);
			}
		}
	    
	    
	}
	
	public tcDataProvider getOIMDataProviderInstance(ODLLogger logger){
		String methodName = "getOIMDataProviderInstance()::";
		tcDataProvider dataProvider = null;
		logger.entering(this.getClass().getName(), methodName);
		logger.log(Level.FINE, " Instantiating tcBaseUtility class...");
		XLDatabase database = XLDatabase.getInstance();
		dataProvider = database.getDataBase();
		logger.log(Level.FINE, " Getting dataProvider instance....");
		logger.exiting(this.getClass().getName(), methodName);
		return dataProvider;
	}
	
	public void closeDataProvider(tcDataProvider dataProvider, ODLLogger logger){
		String methodName = "closeDataProvider()::";
		logger.entering(this.getClass().getSimpleName(), methodName);
		try {
			if(dataProvider!=null && dataProvider.isOpen()){
				dataProvider.close();
				logger.log(Level.FINE, "tcDataProvider instance closed successfully.");
			}
		} catch (tcDataAccessException e) {
			logger.log(Level.SEVERE, "tcDataAccessException", e);
		} catch (tcClientDataAccessException e) {
			logger.log(Level.SEVERE, "tcClientDataAccessException", e);
		}
		logger.exiting(this.getClass().getSimpleName(), methodName);
	}
	
	/**
	 * @param userDetails
	 * @return
	 * @throws SQLException
	 * @throws tcDataSetException 
	 * @throws tcDataAccessException 
	 * @throws Exception
	 */
	public boolean isEntitlementAssignedToUser(String userKey, String entitlementAssignmentKey, tcDataProvider dataProvider) throws SQLException, tcDataSetException, tcDataAccessException {
		String methodName = "isEntitlementAssignedToUser()::";
		tcDataSet dataSet = null;
		boolean entitlementAssignedToUser=false;
		PreparedStatementUtil preparedStatementUtil = new PreparedStatementUtil();
		logger.entering(this.getClass().getSimpleName(), methodName);
		logger.log(Level.FINE,"Parameters passed to the method userKey: "+userKey+" entitlementAssignmentKey: "+entitlementAssignmentKey+", dataprovider: "+dataProvider);
		try {
				if(userKey!=null && userKey.trim().length()>0 && entitlementAssignmentKey!=null && entitlementAssignmentKey.trim().length()>0
						&& dataProvider!=null){
					dataSet = new tcDataSet();
					String dbQuery = this.userEntitlementsQuery;
					preparedStatementUtil.setStatement(dataProvider, dbQuery);
					preparedStatementUtil.setLong(1, Long.parseLong(entitlementAssignmentKey));
					preparedStatementUtil.setLong(2, Long.parseLong(userKey));
					logger.log(Level.FINE, " Query to be executed is: "+dbQuery);
					preparedStatementUtil.execute();
					dataSet = preparedStatementUtil.getDataSet();
					if(!dataSet.isEmpty()){
						logger.log(Level.FINE, " Number of records fetched are: "+dataSet.getRowCount());
						if(dataSet.getString("COUNT").equalsIgnoreCase("1")){
							entitlementAssignedToUser=true;
						}else{
							logger.logp(Level.SEVERE, getClass().getName(), methodName, "Entitlement assignment key "+entitlementAssignmentKey+" is not assigned to user "+userKey);
						}
					}
				}else{
					logger.log(Level.SEVERE, " Invalid parameters passed to the method - [userKey: "+userKey+" entitlementKey: "+entitlementAssignmentKey+" dataProvider: "+dataProvider+"]");
				}
		} catch (tcDataSetException e) {
			logger.log(Level.SEVERE, "tcDataSetException", e);
			throw e;
		}finally{
			if(dataSet!=null){
				dataSet = null;
			}
		}
		logger.exiting(this.getClass().getSimpleName(), methodName);
		return entitlementAssignedToUser;
	}
	
	@Override
	public void validate(long arg0, long arg1, BulkOrchestration arg2)
			throws ValidationException, ValidationFailedException {
		// TODO Auto-generated method stub
		
	}

}
