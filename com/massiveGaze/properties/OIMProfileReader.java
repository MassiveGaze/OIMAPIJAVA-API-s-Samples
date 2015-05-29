
package com.massiveGaze.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class OIMProfileReader {
	Properties props = null;
	public OIMProfileReader() {
		StringBuffer testXLHome = new StringBuffer();
		if(System.getenv("ADE_VIEW_ROOT")!=null){
                   System.out.println(" ADE_VIEW_ROOT From View...");  
		testXLHome.append(System.getenv("ADE_VIEW_ROOT")).append(File.separator).append("iam").append(File.separator).append("iam-build").append(File.separator).append("oim.profile");
		}else{
                     System.out.println(" ADE_VIEW_ROOT NOT FOUND...");
			testXLHome.append("iam-build").append(File.separator).append("oim.profile");
		}

		  this.props = new Properties();
		    try {
                        System.out.println(" OIM Profile Location -> "+testXLHome);
		       this.props.load(new FileInputStream(testXLHome.toString()));
		    } catch (FileNotFoundException e) {
		       e.printStackTrace();
		     } catch (IOException e) {
		      e.printStackTrace();
		     }
		   }
	public String getString(String key) {
		   return this.props.getProperty(key);
	  }
public static void main(String args[]){
    new OIMProfileReader();
}
}
