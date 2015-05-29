package com.massiveGaze.provision;

import static oracle.iam.identity.utils.Constants.FIRSTNAME;
import static oracle.iam.identity.utils.Constants.SEARCH_SORTEDBY;
import static oracle.iam.identity.utils.Constants.SEARCH_SORTORDER;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.iam.platform.entitymgr.spi.entity.Searchable.SortOrder;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;
import oracle.iam.provisioning.api.ApplicationInstanceService;
import oracle.iam.provisioning.api.EntitlementService;
import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.AccountData;
import oracle.iam.provisioning.vo.ApplicationInstance;
import oracle.iam.provisioning.vo.Entitlement;
import oracle.iam.provisioning.vo.EntitlementInstance;
import oracle.iam.provisioning.vo.FormInfo;

import com.massiveGaze.connection.Platform;

public class provisionAccount {
	private static ProvisioningService service=Platform.getService(ProvisioningService.class);
	private static ApplicationInstanceService applicationInstanceService=Platform.getService(ApplicationInstanceService.class);

public static void main(String [] args){
  try {
     ApplicationInstance applicationInstance=applicationInstanceService.findApplicationInstanceByName("appone"); 
        FormInfo formInfo = applicationInstance.getAccountForm();
        System.out.println("Object Type  ->"+applicationInstance.getObjectName());
        System.out.println("ApplicationInstanceName   ->"+applicationInstance.getApplicationInstanceName());
        System.out.println("ResourceName   ->"+applicationInstance.getItResourceName());
        System.out.println("ResourceName   ->"+applicationInstance.getItResourceKey());
        Map parentData = new HashMap();
        parentData.put("UD_APPONE", applicationInstance.getApplicationInstanceName().toString());
        String formKey = String.valueOf(formInfo.getFormKey());
        
        AccountData accountData = new AccountData(formKey, null, parentData);
        Map<Long, List<EntitlementInstance>> entitlemnts = new HashMap<Long, List<EntitlementInstance>>();
        
/*        EntitlementService entSvc = Platform.getService(EntitlementService.class);
        SearchCriteria sc = new SearchCriteria(Entitlement.ENTITLEMENT_DISPLAYNAME,"OU=ravi", SearchCriteria.Operator.CONTAINS);
        Map<String, Object> configParams = new HashMap<String, Object>();
        configParams.put(SEARCH_SORTORDER, SortOrder.ASCENDING);
        configParams.put(SEARCH_SORTEDBY, FIRSTNAME);
        List entitlmentList = entSvc.findEntitlements(sc, (HashMap<String, Object>) configParams);
        System.out.println("****************"+entitlmentList);
        entitlemnts.put(1L, entitlmentList);
        accountData.setEntitlements(entitlemnts);*/
        Account account = new Account(applicationInstance, accountData);
        account.setAccountType(Account.ACCOUNT_TYPE.Primary);
      for(int i=13;i<14;i++) {
          service.provision(""+i, account);
          System.out.println("Provisioned Account for User   ->"+i);
      }

  	}catch (Exception e){
	   System.out.println("In exception block");
	   e.printStackTrace();
  	}
 }
}