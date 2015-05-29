package com.massiveGaze.selfservice;

//import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.massiveGaze.connection.Platform;
import com.massiveGaze.connection.DataSource;

import oracle.iam.selfservice.self.selfmgmt.api.AuthenticatedSelfService;
import oracle.iam.selfservice.uself.uselfmgmt.api.UnauthenticatedSelfService;


public class UnAuthenticatedSelfServiceResetPassword {	
	    private UnauthenticatedSelfService m_unAuthselfservice =  Platform.getService(UnauthenticatedSelfService.class);	  
	    static private Map<String, String> cachedUserKeys = new HashMap<String, String>();
	    
	    public static void main(String[] args) {
		// TODO Auto-generated method stub
	    	try {
				new UnAuthenticatedSelfServiceResetPassword().setResetPassword("101", "TUSER V493");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	
	 private  void setResetPassword(String usrKey, String loggedInUser) throws Exception {
	        String password = "Welcome2";
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
	        Map<String, Object> quesAnsMap = setChallengeQuestiones(usrKey);
	        m_unAuthselfservice = Platform.getService(UnauthenticatedSelfService.class);
	        m_unAuthselfservice.resetPassword(loggedInUser, quesAnsMap, password.toCharArray());
	        resultSet = preparedStatement.executeQuery();
	        //assertTrue(resultSet.next());
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
	 
	    private  Map<String, Object> setChallengeQuestiones(String usrKey) throws Exception {
	    	 System.out.println("Setting  Challenge Questions ....");
	        Map<String, Object> mapQnA = new HashMap<String, Object>();			
			mapQnA.put("What is your mother's maiden name?", "4");
			mapQnA.put("What is your favorite color?", "5");
			//mapQnA.put("What is the city of your birth?", "6");
			mapQnA.put("What is the name of your pet?", "6");

	        return mapQnA;
	    }

}
