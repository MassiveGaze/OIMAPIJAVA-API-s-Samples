package com.massiveGaze.password;

import Thor.API.Security.XLClientSecurityAssociation;

import com.massiveGaze.connection.OIMConnection;
import com.thortech.xl.dataaccess.tcDataBaseClient;
import com.thortech.xl.dataaccess.tcDataProvider;
import com.thortech.xl.dataaccess.tcDataSet;
import com.thortech.xl.dataaccess.tcDataSetException;

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

 
/**
 * This class gets the OIM Client and uses that to establish a
 * connection to the OIM Schema. You can query the USR table and
 * get the password in plain text. 
 * NOTE: The administrator credential must be used for the OIM Client. 
 */
public class DecryptedOIMPassword 
{
    public static void main(String[] args)
    {
        tcDataProvider dbProvider = null;
        try
        {
 
 
            XLClientSecurityAssociation.setClientHandle(OIMConnection.getConnection());//Needed for database client
            dbProvider = new tcDataBaseClient(); //Connection to OIM Schema
            tcDataSet dataSet = new tcDataSet(); //Stores the result set of an executed query
            String query = "SELECT * FROM USR"; //Query Users table
            //String query = "SELECT * FROM PCQ"; //Query Users Challenge Question
            dataSet.setQuery(dbProvider, query); //Set query and database provider
            dataSet.executeQuery(); //execute query and store results into dataSet object
            int records = dataSet.getTotalRowCount(); //Get total records from result set
 
            for(int i = 0; i < records; i++)
            {
                dataSet.goToRow(i); //move pointer to next record
                String plainTextPassword = dataSet.getString("USR_PASSWORD");
                String userLogin = dataSet.getString("USR_LOGIN");
                String userStatus = dataSet.getString("USR_STATUS");
                System.out.printf("User Login: %s\nStatus: %s\nPassword: %s\n\n", userLogin, userStatus, plainTextPassword);  
 
                //Getting user challenge questions and answers
                //String usrKey = dataSet.getString("USR_KEY");
                //String question = dataSet.getString("PCQ_QUESTION");
                //String answer = dataSet.getString("PCQ_ANSWER");
                //System.out.printf("USR_KEY: %s\nQuestion: %s\nAnswer: %s\n", usrKey, question, answer);
            }
        } 
 
        catch (tcDataSetException ex) 
        { 
            Logger.getLogger(DecryptedOIMPassword.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            //close connections
            try{dbProvider.close();} catch(Exception e){}
            try{XLClientSecurityAssociation.clearThreadLoginSession();} catch(Exception e){}
           
        }     
    }//end main method   
}//end class