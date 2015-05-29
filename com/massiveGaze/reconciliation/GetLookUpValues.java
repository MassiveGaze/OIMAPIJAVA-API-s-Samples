package com.massiveGaze.reconciliation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.massiveGaze.connection.DataSource;

public class GetLookUpValues {
	
	public List getLookupCodes(String lkvKey){
		List list = new ArrayList();
		Connection con = DataSource.getConnection();
		try {
			Statement stmt = con.createStatement();
			String query ="select * from LKV where lku_key="+lkvKey;
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				list.add(rs.getString("lkv_encoded"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Size"+ list.size());
		return list;
	}
	public static void main(String[] args) throws Exception {
		System.out.println(new GetLookUpValues().getLookupCodes("1592"));
		
	}
}
