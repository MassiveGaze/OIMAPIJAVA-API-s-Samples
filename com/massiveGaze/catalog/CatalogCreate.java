package com.massiveGaze.catalog;

import java.util.List;
import java.util.ArrayList;

import com.massiveGaze.connection.Platform;

import oracle.iam.catalog.api.CatalogService;
import oracle.iam.catalog.vo.Catalog;
import oracle.iam.catalog.vo.Result;
import oracle.iam.catalog.vo.MetaData;
import oracle.iam.platform.utils.vo.OIMType;

public class CatalogCreate{
	protected CatalogService serviceObj = null;
	
	public static void main(String[] args) throws Exception{

		CatalogCreate create = new CatalogCreate();
		try {
			create.execute();
		} catch (Exception ex) {
			System.out.println("EXCEPTION: " + ex.getMessage());
			ex.printStackTrace();
		}
		return;
	}

	
	protected void execute() throws Exception{

		/*
		 * output from a create catalog item
		 */

		// updateTime: 2013-01-18 17:07:16.0
		// approverUser: nullapprover
		// UserLogin: null
		// approverRoleDisplayName: null
		// certifierUserLogin: null
		// certifierRoleDisplayName: null
		// fulFillMentUserLogin: null
		// fulFillMentRoleDisplayName: null
		// approverRole: null
		// certifierUser: null
		// certfierRole: null
		// fulFillMentUser: null
		// fulFillMentRole: null
		// certifiable: true
		// riskScoreUpdateTime: null
		// itemRisk: 0
		// id: 60
		// entityKey: 1
		// entityType: ApplicationInstance
		// parentEntityKey: null
		// parentEntityType: ApplicationInstance
		// categoryName: ApplicationInstance
		// requestable: true
		// userDefinedTags: null
		// tags: badging Disconnected Badge Access badging badging
		// deleted: false
		// entityName: badging
		// entityDisplayName: Badge Accessentity
		// Description: Badge Access for physical access control
		// auditObjectives: null
		// metadata: []
		// createBy: null
		// updateBy: null

		Result result = null; // OIMClient API
		
		List<MetaData> metadata = new ArrayList<MetaData>();
		MetaData mdata = null;

		mdata = new MetaData();
		mdata.setValue("JK");
		metadata.add(mdata);

		mdata = new MetaData();
		mdata.setValue("Test");
		metadata.add(mdata);

		mdata = new MetaData();
		mdata.setValue("foo");
		metadata.add(mdata);

		System.out.println("__BEGIN__");

		/*
		 * add the attribute values
		 */
		for(int i=0;i < 1050;i++){
			Catalog catalog = new Catalog();
		catalog.setItemRisk(0);
		catalog.setCertifiable(true);
		catalog.setEntityKey("1"); // must match existing resource object
		catalog.setEntityType(OIMType.valueOf("Entitlement"));
		catalog.setParentEntityType(OIMType.valueOf("ApplicationInstance"));
		catalog.setCategoryName("Entitlement");
		catalog.setRequestable(true);
		catalog.setTags("TestEnt");
		catalog.setDeleted(false);
		catalog.setEntityName("Code"+i);
		catalog.setEntityDisplayName("Decode"+i);
		catalog.setEntityDescription("Create catalog item using api");
		catalog.setMetadata(metadata);
		System.out.println("Catalog object created.");

		serviceObj=Platform.getService(CatalogService.class);
		result = serviceObj.addCatalogItems(catalog);
		}
		System.out.println("Creation status: '" + result.isStatusFlag() + "'");

		System.out.println("__END__");

		return;
	}
}
