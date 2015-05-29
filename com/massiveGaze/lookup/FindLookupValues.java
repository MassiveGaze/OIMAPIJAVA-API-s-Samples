package com.massiveGaze.lookup;
import Thor.API.tcResultSet;
import Thor.API.tcUtilityFactory;
import Thor.API.Operations.tcLookupOperationsIntf;

import com.massiveGaze.connection.OIMConnection;
import com.massiveGaze.util.OIMUtils;

public class FindLookupValues {

	public static void main(String[] args) throws Exception {
		String lookupCode = "Lookup.Foo.Bar";

		tcUtilityFactory factory = new tcUtilityFactory(OIMConnection.getEnvironment(), "xelsysadm","Welcome1");
		tcLookupOperationsIntf lookupIntf = (tcLookupOperationsIntf) factory.getUtility("Thor.API.Operations.tcLookupOperationsIntf");

		tcResultSet rs = lookupIntf.getLookupValues(lookupCode);

		OIMUtils.printResultSet(rs);
		
		factory.close();
		System.exit(0);
	}
}