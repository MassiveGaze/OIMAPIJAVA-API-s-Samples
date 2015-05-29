package com.massiveGaze.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.massiveGaze.properties.OIMProfileReader;

/**
 *
 * 
 */
public class DataSource {
	private static Connection connectionObject = null;
	private static OIMProfileReader properties=null;
	static String JDBC_DRIVER = null;
	static String DB_URL = null;

	static String USER = null;
	static String PASS = null;

	private static Connection makeConnection() {

		try {			
			properties = new OIMProfileReader();
			JDBC_DRIVER=properties.getString("operationsDB.directDBdriver");
			DB_URL=properties.getString("operationsDB.jdbcUrl");
			USER=properties.getString("operationsDB.user");
			PASS=properties.getString("operationsDB.password");
			
			Class.forName(JDBC_DRIVER);
			System.out.println("Connecting to database...");
                        System.out.println("DB User Name -> "+DB_URL);
                        System.out.println("DB URL  -> "+USER);
                        
			connectionObject = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException se) {
					se.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return connectionObject;
	}

	public static synchronized Connection getConnection() {
		if (connectionObject == null) {
			connectionObject = makeConnection();
		}
		return connectionObject;

	}

	public static void closeConnection() {
            	System.out.println("Closing  to database connection ...");
		try {
			properties=null;
			JDBC_DRIVER=null;
			DB_URL=null;
			USER=null;
			PASS=null;
			if (connectionObject != null)
				connectionObject.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
	public static void main(String args[]){
		getConnection();
                closeConnection();
	}
}
