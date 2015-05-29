package com.massiveGaze.orchestration;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.massiveGaze.connection.DataSource;

public class DumpOrchEventException {
	public static void main(String[] args) throws Exception {
		long orchEventKey = 10;
		// Connect to the database
		
		Connection con = DataSource.getConnection();				
		Statement stmt = con.createStatement();
		String query = "select id, name, status, result from orchevents where id="
				+ orchEventKey;
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			System.out.println("id = " + rs.getString("id"));
			System.out.println("name = " + rs.getString("name"));
			System.out.println("status = " + rs.getString("status"));
			ByteArrayInputStream bais = new ByteArrayInputStream(
					rs.getBytes("result"));
			ObjectInputStream oip = new ObjectInputStream(bais);
			Object o = oip.readObject();
			Exception ex = (Exception) o;
			ex.printStackTrace();
			System.out.println("****************"+ex.getMessage());
		}
		stmt.close();
		rs.close();
		con.close();
		System.exit(0);
	}
}