package com.massiveGaze.export;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import oracle.iam.platform.OIMClient;
import Thor.API.Exceptions.tcAPIException;
import Thor.API.Operations.tcExportOperationsIntf;

import com.massiveGaze.connection.OIMConnection;
import com.thortech.xl.ddm.exception.DDMException;
import com.thortech.xl.vo.ddm.RootObject;

public class ExportCategoryUtils {

	public static void main(String str[]) throws tcAPIException, DDMException
	{
		try
		{
			java.util.Date date= new java.util.Date();
            String outputFileName = "Export.xml";
			Collection<RootObject> rdbmsRootObjects = null;
			Collection<RootObject> rdbmsRootObjectsChildren = null;
			Collection<RootObject> rdbmsRootObjectsDependency = null;
			Collection<RootObject> rdbmsRootObjectsDependencytree = null;
			Collection<RootObject> allObjects =  new ArrayList();

            FileWriter fstream = new FileWriter(outputFileName);
			BufferedWriter out = new BufferedWriter(fstream);
			
			OIMClient oIMConnection = OIMConnection.getConnection();
                       // c.login(username, password.toCharArray());
             tcExportOperationsIntf exportIntf =  (tcExportOperationsIntf) oIMConnection.getService(Thor.API.Operations.tcExportOperationsIntf.class);
             int count=0;           
           
             
            Collection<String> categories = exportIntf.retrieveCategories();

            Iterator<String> catIter = categories.iterator();
            while (catIter.hasNext()) {
                System.out.println("	Category	:	"+ ++count +"		"+catIter.next());
            }
            count=0;
            String exportCategory = "GenericConnector"; // Which Category user needs to Export
            String searchString = "GTC2*"; //matching String else * for All
            System.out.println("------------------------------------------------------------------------");
            System.out.println("	Start Time : " +new Timestamp(date.getTime()));
            System.out.println("------------------------------------------------------------------------");
            System.out.println("	Finding Objects For Category :	'"+exportCategory+"'");
            
			rdbmsRootObjects = exportIntf.findObjects(exportCategory, searchString);
			rdbmsRootObjects.addAll(exportIntf.findObjects("Job", "GTC2_GTC"));
			if (rdbmsRootObjects == null || rdbmsRootObjects.size() < 1) {
				throw new DDMException("No Rdbms Objects found");
			}
			Iterator<RootObject> rootObjects = rdbmsRootObjects.iterator();
            while (rootObjects.hasNext()) {
                System.out.println("	Search results	:	"+ ++count +"		"+rootObjects.next());
            } 
            count=0;
			//Get the child objects
			System.out.println("	Getting Child Objects For Parent Object...!");			
			rdbmsRootObjectsChildren = exportIntf.retrieveChildren(rdbmsRootObjects);
            Iterator<RootObject> rdbmsRootObjectsChld = rdbmsRootObjectsChildren.iterator();
            while (rdbmsRootObjectsChld.hasNext()) {
                System.out.println("	Child Object	:	"+ ++count + "		"+rdbmsRootObjectsChld.next());
            } 
            count=0;
			System.out.println("	Collecting All Child Objects Into List...!");
			allObjects.addAll(rdbmsRootObjectsChildren);
			
			//Get the  dependencies
			System.out.println("	Getting Dependency Object...!");
			rdbmsRootObjectsDependency = exportIntf.getDependencies(rdbmsRootObjectsChildren);
			System.out.println("	Processing Please Wait...!");
			for (Iterator iter = rdbmsRootObjectsDependency.iterator(); iter.hasNext();) {
				RootObject child = (RootObject) iter.next();				
				if (!allObjects.contains(child)) {
					 System.out.println("	Dependency Child Object	:	"+ ++count + "		"+child);
					allObjects.add(child);
				}
			}
			//Get the  dependencies tree
			System.out.println("	Creating Object Dependency Tree... !");
			rdbmsRootObjectsDependencytree =exportIntf.retrieveDependencyTree(allObjects);

			//store all the root objects in 'rdbmsRootObjectsDependencytree' object before export takes off..
			System.out.println("	Processing Root Objects Dependency Tree Please Wait...!");
			for (Iterator iter = allObjects.iterator(); iter.hasNext();) {
				RootObject child = (RootObject) iter.next();
				if (!rdbmsRootObjectsDependencytree.contains(child)) {
					rdbmsRootObjectsDependencytree.add(child);
				}
			}
			// Export the XML file
			System.out.println("	Exporting XML File. Please Wait...!");
			String s = exportIntf.getExportXML(rdbmsRootObjectsDependencytree, "*");

			//System.out.println(s);
			//Store it in XML file.
			out.write(s);
			System.out.println("	'"+exportCategory+"' Objects Successfully Exported : " + outputFileName);
			System.out.println("	DM Export Completed...");
			out.close();
			System.out.println("------------------------------------------------------------------------");
			System.out.println("	End Time : " +new Timestamp(new java.util.Date().getTime()));		
			System.out.println("------------------------------------------------------------------------");
		}
		catch(Exception ex)
		{
			System.out.println(ex);
			ex.printStackTrace();
			throw new DDMException(ex.getMessage());
		}

	}
}
