package com.massiveGaze.provision;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import oracle.iam.platform.Platform;
import oracle.iam.platform.kernel.ValidationException;
import oracle.iam.platform.kernel.ValidationFailedException;
import oracle.iam.platform.kernel.spi.ConditionalEventHandler;
import oracle.iam.platform.kernel.spi.PreProcessHandler;
import oracle.iam.platform.kernel.vo.AbstractGenericOrchestration;
import oracle.iam.platform.kernel.vo.BulkEventResult;
import oracle.iam.platform.kernel.vo.BulkOrchestration;
import oracle.iam.platform.kernel.vo.EventResult;
import oracle.iam.platform.kernel.vo.Orchestration;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.util.Constants;
import oracle.iam.provisioning.vo.ApplicationInstance;
import oracle.iam.provisioning.vo.Entitlement;
import Thor.API.tcResultSet;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.Operations.tcLookupOperationsIntf;


//idmit.plugins.eventhandler.preprocess.breakglassaccess.IDMITBreakGlassAccessPreProcessHandler
//To make a conditional event handler, your class must implement ConditionalEventHandler, and then write your conditions in the isApplicable method.

public class IDMITBreakGlassAccessPreProcessHandler implements PreProcessHandler, ConditionalEventHandler {
//	private static final CharSequence BREAKGLASS_ACCESS = "breakglass_access";
//	private static final CharSequence CSR_ADMIN = "CSR_SAAS_FA_UBS_ADMIN";
//	private static final CharSequence ARBOR = "arbor"; --test purpose
	String handlerName;
	String _appInstName;

	private void log(String msg) {
		System.out.println("[***" +handlerName+ "***]" + msg);
	}

	@Override
	public void initialize(HashMap<String, String> arg0) {
		handlerName = getClass().getSimpleName();
	}

	/*private boolean checkIfApplicable(AbstractGenericOrchestration orchn){
		Entitlement ent = null;
		String entCode = null;
		HashMap<String, Serializable> orchParams = orchn.getParameters();
		try {
			ent = (Entitlement)orchParams.get(Constants.ORCH_PARAM_ENTITLEMENT);
			entCode = ent.getEntitlementCode();
		} catch (Exception e) {
			log("Exception : " + e.getMessage());
		}
		if(ent == null || entCode == null || !hasRequiredPattern(entCode)) {
			return false;
		}

		log("Entitlement "+entCode+" is a *" + BREAKGLASS_ACCESS + " entitlement.");	

		return true;
	}*/
	
	
	
	private boolean checkIfApplicable(AbstractGenericOrchestration orchn){
		String appInstName;
		appInstName = getAppInstanceName(orchn);

		if(appInstName == null) 
			return false;

		if(!isAppInstConfiguredForBreakGlass(appInstName))
			return false;

		_appInstName = appInstName;
		log("Enhanced check: AppInstName [" + appInstName + "] is configured for BreakGlass");
		return true;
	}


	private boolean isAppInstConfiguredForBreakGlass(String appInstName) {
		try {
			return getLookupValueByLookupNameFilter("Lookup.DBUM.Oracle.Privileges", appInstName) != null;
		} catch (Exception exc) {
			log("Exception checking for '" +appInstName+ "' in 'Lookup.DBUM.Oracle.Privileges'");
		}
		return false;
	}

	private String getAppInstanceName(AbstractGenericOrchestration orchn) {
		HashMap<String, Serializable> orchParams = orchn.getParameters();
		String appInstanceId = (String)orchParams.get(Constants.ORCH_PARAM_APPINSTANCE_KEY);
		ApplicationInstance appInst = getApplicationInstance(appInstanceId);
		if(appInst == null) 
			return null;
		//log("App instance name for this ENT is: " + appInst.getApplicationInstanceName());
		return appInst.getApplicationInstanceName();
	}

	private ApplicationInstance getApplicationInstance(String appInstanceId){
		ApplicationInstance ai = null;
		try {
			ApplicationInstanceService aiService = getService(ApplicationInstanceService.class);
			if(aiService == null) 
				return null;
			if (appInstanceId != null) {
				ai = aiService.findApplicationInstanceByKey(new Long(appInstanceId)); 
			}
		} catch (Exception e) {
			log("Exception getting AppInst based on Id: " + appInstanceId + ". Error: " + e);
		}
		return ai;
	}

	/*private boolean hasRequiredPattern(String entCode) {
		if(entCode.toLowerCase().contains(BREAKGLASS_ACCESS) || entCode.split(",")[0].contains(CSR_ADMIN))
//				|| entCode.toLowerCase().contains(ARBOR))
				return true;
		
		return false;
	}*/

	@Override
	public boolean isApplicable(AbstractGenericOrchestration orchn) {
		return checkIfApplicable(orchn);
	}

	@Override
	public boolean cancel(long arg0, long arg1,
			AbstractGenericOrchestration arg2) {
		return false;
	}

	@Override
	public void compensate(long arg0, long arg1,
			AbstractGenericOrchestration arg2) {
	}


	@Override
	public EventResult execute(long processId, long eventId, Orchestration orchestration) throws ValidationException, ValidationFailedException {
		try {
			log("In execute()");
			if(!checkIfApplicable(orchestration))
				return new EventResult();
			
			HashMap<String, Serializable> orchParams = orchestration.getParameters();
			Entitlement entitlement = (Entitlement) orchParams.get(Constants.ORCH_PARAM_ENTITLEMENT);
			String entitlementDisplayName = entitlement.getEntitlementValue();
			String beneficiaryKey = (String) orchParams.get(Constants.ORCH_PARAM_BENEFICIARY_KEY);
			HashMap orchParamsChildDataMap = (HashMap)orchParams.get(Constants.ORCH_PARAM_CHILD_DATA);
			String days = getLookupValueByLookupNameFilter("Lookup.PDIT.BreakGlass.Setup", _appInstName.concat("~").concat(entitlement.getEntitlementCode()));
			if(days == null) {
				log(entitlementDisplayName + " is setup without prefixing AppInstName tilde. Pls check.");
				return new EventResult();
			}
			Date endDate = getEntitlementEndDate(days);
			log("Benefeciary: " + beneficiaryKey);
			log("Entitlement assigned: " + entitlementDisplayName);
			log("Entitlement END DATE is set to value:  " + endDate);
			orchParamsChildDataMap.put("endDate", endDate);
			return new EventResult();
		} catch (Exception exc) {
			return new EventResult();
		}
	}

	/**=================================================
	 *=================UTILS=========================	
	 *================================================**/	
	private Date getEntitlementEndDate(String minsToRevoke){
		int mins = Integer.parseInt(minsToRevoke);
		Calendar calender = new GregorianCalendar();
		log("GENERIC CODE - CURRENT DATE: " + calender.getTime());
		calender.add(Calendar.MINUTE, mins);
		return calender.getTime();
	}


	//	public static java.sql.Time forwardRoll( java.util.Date startDate, int period, int amount )
	//	{
	//		GregorianCalendar gc = new GregorianCalendar();
	//		gc.setTime(startDate);
	//		gc.add(period, amount);
	//		return new java.sql.Time(gc.getTime().getTime());
	//	}

	private String getLookupValueByLookupNameFilter(String lookupName, String lookupCode){
		//log("Searching for " + lookupCode + " in " + lookupName);
		String value = null;
		tcLookupOperationsIntf lookupIntf = getService(tcLookupOperationsIntf.class);
		Map<String, String> filters = new HashMap<String, String>();
		filters.put("Lookup Definition.Lookup Code Information.Code Key", lookupCode);
		try {
			tcResultSet resultSet = lookupIntf.getLookupValues(lookupName, filters);
			if(!resultSet.isEmpty()){
				resultSet.goToRow(0);
				value = resultSet.getStringValue("Lookup Definition.Lookup Code Information.Decode");
			}
		} catch (tcAPIException e) {
			log("TcAPI EXC: " + e);
		} catch (tcInvalidLookupException e) {
			log("Invalid Lookup EXC: " + e);
		} catch (tcColumnNotFoundException e) {
			log("Column Not Found EXC: " + e);
		}
		//log("Value of '" + lookupCode + "' in Lookup: '" +lookupName+"' is: " + value);
		return value;
	}
	private <T> T getService(Class<T> serviceClass) {
		return Platform.getService(serviceClass);
	}
	/**=================================================
	 *=================UTILS=========================	
	 *================================================**/	 

	@Override
	public BulkEventResult execute(long arg0, long arg1, BulkOrchestration arg2) {
		log("REQUEST CAME IN BULK ORCHESTRATION. NUMBER OF REQUEST PARAMS ARE: " + arg2.getBulkParameters().length);
		return new BulkEventResult();
	}
}
