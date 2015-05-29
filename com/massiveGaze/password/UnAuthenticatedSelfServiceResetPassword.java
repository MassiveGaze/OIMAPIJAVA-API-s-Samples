package com.massiveGaze.password;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import oracle.iam.selfservice.uself.uselfmgmt.api.UnauthenticatedSelfService;

import com.massiveGaze.connection.DataSource;
import com.massiveGaze.connection.Platform;


public class UnAuthenticatedSelfServiceResetPassword {	
	    private UnauthenticatedSelfService m_unAuthselfservice =  Platform.getService(UnauthenticatedSelfService.class);
	    static private Map<String, String> cachedUserKeys = new HashMap<String, String>();
	    
	    public static void main(String[] args) {
		// TODO Auto-generated method stub
	    	try {
				new UnAuthenticatedSelfServiceResetPassword().setResetPassword("5", "TUSER0");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	
	 private  void setResetPassword(String usrKey, String loggedInUser) throws Exception {
	        String password = "Welcome1";
	        System.out.println("password : " + password);
	        Connection con = DataSource.getConnection();
	        String query = "SELECT USR_LOGIN,"
	            + " USR_PASSWORD FROM USR WHERE USR_KEY=?";
	        System.out.println(query);
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setLong(1, Long.parseLong(usrKey));
	        ResultSet resultSet = preparedStatement.executeQuery();
	        resultSet.next();
	        String oldPass = resultSet.getString("USR_PASSWORD");           
	        System.out.println("-----------oldPass oldPass--------------------- " + oldPass);
	       // removeChallengeQuestiones(usrKey);
	        Map<String, Object> quesAnsMap = setChallengeQuestiones();

	        m_unAuthselfservice = Platform.getService(UnauthenticatedSelfService.class);
	        m_unAuthselfservice.resetPassword(loggedInUser, quesAnsMap, password.toCharArray());
	        resultSet = preparedStatement.executeQuery();
	        resultSet.next();
	        String newPass = resultSet.getString("USR_PASSWORD");   
	        System.out.println("-------------newPass newPass------------------- " + newPass);        

	        System.out.println("Change password successful!!!");
	        DataSource.closeConnection();
	    }
	 	 
	 private void removeChallengeQuestiones(String usrKey) throws Exception {
		 System.out.println("Removing Challenge Questions ....");
	        Connection con = DataSource.getConnection();
	        String query = "DELETE FROM PCQ WHERE USR_KEY=?";
	        System.out.println(query);
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setLong(1, Long.parseLong(usrKey));
	        preparedStatement.executeUpdate();	       
	        System.out.println("Removed Challenge Questions ....");
	    }
	 
	    private  Map<String, Object> setChallengeQuestiones() throws Exception {
	    	 System.out.println("Setting  Challenge Questions ....");
	        Map<String, Object> mapQnA = new HashMap<String, Object>();			
			mapQnA.put("What is your mother's maiden name?", "1");
			//mapQnA.put("What is your favorite color?", "5");
			mapQnA.put("What is the city of your birth?", "3");
			mapQnA.put("What is the name of your pet?", "2");

	        return mapQnA;
	    }

}
