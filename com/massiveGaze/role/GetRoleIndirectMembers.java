package com.massiveGaze.role;

import static oracle.iam.identity.utils.Constants.FIRSTNAME;
import static oracle.iam.identity.utils.Constants.SEARCH_ENDROW;
import static oracle.iam.identity.utils.Constants.SEARCH_SORTEDBY;
import static oracle.iam.identity.utils.Constants.SEARCH_SORTORDER;
import static oracle.iam.identity.utils.Constants.SEARCH_STARTROW;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.iam.identity.exception.RoleMemberException;
import oracle.iam.identity.rolemgmt.api.RoleManager;
import oracle.iam.identity.usermgmt.vo.User;
import oracle.iam.platform.authz.exception.AccessDeniedException;
import oracle.iam.platform.entitymgr.spi.entity.Searchable.SortOrder;
import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import com.massiveGaze.connection.Platform;

public class GetRoleIndirectMembers {
	  protected RoleManager m_roleManager = Platform.getService(RoleManager.class);
	public static void main(String[] args) throws Exception {
		new GetRoleIndirectMembers().testGetRoleIndirectMembersWithPagenation();
	}

	

public void testGetRoleIndirectMembersWithPagenation() throws Exception {  
    String roleKey = "61";   
    //actual test case.
    SearchCriteria criteria = getSearchCriteriaAllUsers();
    //test getting all members by passing null to control and retrieve

    Set<String> retAttr = new HashSet<String>();
    Map<String, Object> control = new HashMap<String, Object>();
    control.put(SEARCH_SORTORDER, SortOrder.ASCENDING);
    control.put(SEARCH_SORTEDBY, FIRSTNAME);
    control.put(SEARCH_STARTROW, 0);
    control.put(SEARCH_ENDROW, 30);
    
    retAttr.add("rownum");
    retAttr.add("First Name");
    retAttr.add("User Login");
    
    List<User> listOfIndirectMembers = null;      

            try {
                listOfIndirectMembers =
                    m_roleManager.getRoleMembers(roleKey,criteria,retAttr,control, false);
            } catch (RoleMemberException e) {
                
            } catch (AccessDeniedException e) {
               
            }  
            System.out.println(listOfIndirectMembers);
}
protected SearchCriteria getSearchCriteriaAllUsers() {   
	SearchCriteria criteria1 = new SearchCriteria("Display Name", "veda", SearchCriteria.Operator.OR);
    return new SearchCriteria(FIRSTNAME, "veda", SearchCriteria.Operator.CONTAINS);
}

}
