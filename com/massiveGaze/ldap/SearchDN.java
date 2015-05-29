package com.massiveGaze.ldap;


import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;


public class SearchDN {
	static DirContext ctx = null;
	
	public static void getDNDetails(){
		try {

			ctx = (DirContext) LDAPConnection.getLDAPConnection();
			String SearchCtrlString = "(&(objectclass=inetOrgPerson)(uid=TUSER3))";
			SearchControls controls = new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration results = ctx.search(LDAPConnection.getUserContainer(), SearchCtrlString, controls);
			 System.out.println(" Result Has More -> "+results.hasMore());
			 String obPasswordExpiryDate=null;
			 while (results.hasMore()) {
					SearchResult searchResult = (SearchResult) results.next();
					Attributes attributes = searchResult.getAttributes();
					Attribute attrObPWDExpiry = attributes.get("obpasswordexpirydate");
					if(attrObPWDExpiry==null){
							attrObPWDExpiry=attributes.get("orclpwdexpirationdate");
							System.out.println("******* -> ");
					}
					if(attrObPWDExpiry!=null)
		            obPasswordExpiryDate = (String) attrObPWDExpiry.get();  
					System.out.println("obPasswordExpiryDate -> "+obPasswordExpiryDate);
					
				}

		} catch (Exception e) {
			e.printStackTrace();
			//fail("UnLock user failed : " + e.getCause());
		}	
	}
	public static void main(String args[]){
		getDNDetails();
	}
}
