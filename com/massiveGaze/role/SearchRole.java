package com.massiveGaze.role;

import static oracle.iam.identity.rolemgmt.api.RoleManagerConstants.ROLE_CATEGORY_KEY;
import static oracle.iam.identity.rolemgmt.api.RoleManagerConstants.ROLE_DESCRIPTION;
import static oracle.iam.identity.rolemgmt.api.RoleManagerConstants.ROLE_DISPLAY_NAME;
import static oracle.iam.identity.rolemgmt.api.RoleManagerConstants.ROLE_KEY;
import static oracle.iam.identity.rolemgmt.api.RoleManagerConstants.ROLE_LDAP_DN;
import static oracle.iam.identity.rolemgmt.api.RoleManagerConstants.ROLE_NAME;
import static oracle.iam.identity.rolemgmt.api.RoleManagerConstants.ROLE_OWNER_KEY;
import static oracle.iam.identity.utils.Constants.SEARCH_ENDROW;
import static oracle.iam.identity.utils.Constants.SEARCH_SORTEDBY;
import static oracle.iam.identity.utils.Constants.SEARCH_SORTORDER;
import static oracle.iam.identity.utils.Constants.SEARCH_STARTROW;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.rolemgmt.vo.Role;
import oracle.iam.platform.entitymgr.spi.entity.Searchable;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import com.massiveGaze.connection.Platform;

public class SearchRole {
	  protected static final String ROLE_NAME_PREFIX = "Role Name";
	  protected RoleManager m_roleManager = Platform.getService(RoleManager.class);
	  
	  public static void main(String args[]) throws Exception{
		  new SearchRole().searchRoleConfigParams();
	  }
	public void searchRoleConfigParams() throws Exception {

	    SearchCriteria criteria =   new SearchCriteria(ROLE_NAME, ROLE_NAME_PREFIX + "*", SearchCriteria.Operator.EQUAL);

	    Set<String> searchAttrs = getSearchRoleAttributes();  

	    Map<String, Object> configParams = new HashMap<String, Object>();
	    int start = 0;
	    int end = 5;
	    configParams.put(SEARCH_STARTROW, start);
	    configParams.put(SEARCH_ENDROW, end);
	    configParams.put(SEARCH_SORTORDER, Searchable.SortOrder.ASCENDING);
	    configParams.put(SEARCH_SORTEDBY,ROLE_NAME);  

	    try {
	        List<Role> roles =  m_roleManager.search(criteria, searchAttrs, configParams);	     

	    } catch (Exception e) {
	      
	    }

	}	
    protected HashSet<String> getSearchRoleAttributes() {
        // NOTE for SRGs we can't use getEntityAttributes()
        HashSet<String> searchAttributes = new HashSet<String>();

        searchAttributes.add(ROLE_KEY);
        searchAttributes.add(ROLE_NAME);
        searchAttributes.add(ROLE_DISPLAY_NAME);
        searchAttributes.add(ROLE_DESCRIPTION);
        searchAttributes.add(ROLE_OWNER_KEY);
        searchAttributes.add(ROLE_CATEGORY_KEY);
        searchAttributes.add(ROLE_LDAP_DN);
        return searchAttributes;
    }

}
