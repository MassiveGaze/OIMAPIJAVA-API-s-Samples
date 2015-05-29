package com.massiveGaze.catalog;

import java.util.List;

import com.testConnection.Platform;

import oracle.iam.catalog.api.CatalogService;
import oracle.iam.catalog.vo.CatalogSearchCriteria;
import oracle.iam.catalog.vo.CatalogSearchResult;
import oracle.iam.catalog.vo.Catalog;

public class CatalogSearch{

	protected CatalogService serviceObj = null;
   public static void main(String[] args) throws Exception  {
      CatalogSearch test = new CatalogSearch();
      try
      {
         test.execute();
      }
      catch (Exception ex)
      {
    	  System.out.println("EXCEPTION: " + ex.getMessage());
      }

      return;
   }
   
   protected void execute() throws Exception
   //----------------------------------------------------------------
   {
      String tag = null;
      String category = null;
      StringBuilder buf = new StringBuilder();
      CatalogSearchCriteria csc1 = null;
      CatalogSearchCriteria csc2 = null;
      CatalogSearchCriteria csc = null;
      CatalogSearchResult searchResults = null;
      List<Catalog> catalogs = null;

      System.out.println("__BEGIN__");

      tag = "DCAppInstance2"; // display name of the catalog item 
      category = "ApplicationInstance"; // type of the catalog item

      csc1 = new CatalogSearchCriteria(CatalogSearchCriteria.Argument.TAG, tag, CatalogSearchCriteria.Operator.EQUAL);
      serviceObj=Platform.getService(CatalogService.class);
      searchResults = serviceObj.search(csc1, 1, 5000, "CATALOG_ID", CatalogSearchCriteria.SortCriteria.ASCENDING);

      catalogs = searchResults.getCatalogs();

      if (catalogs != null && !catalogs.isEmpty())
      {
         buf.append("search results, quantity=").append(catalogs.size());
         
         for (Catalog catalog : catalogs)
         {
            buf.append("ApproverRole='").append(catalog.getApproverRole()).append("', ");
            buf.append("ApproverRoleDisplayName='").append(catalog.getApproverRoleDisplayName()).append("', ");
            buf.append("ID='").append(catalog.getId()).append("', ");
            buf.append("FulfillmentRole='").append(catalog.getFulFillMentRoleDisplayName()).append("', ");
            buf.append("EntityKey='").append(catalog.getEntityKey()).append("'\n");
         }
      }
      else
      {
         buf.append("search result is empty");
      }

      System.out.println(buf.toString());

      System.out.println("__END__");

      return;
   }
}
