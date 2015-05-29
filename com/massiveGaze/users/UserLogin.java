package com.massiveGaze.users;

import java.util.HashMap;
import java.util.Map;

import oracle.iam.selfservice.exception.SetChallengeValueException;
import oracle.iam.selfservice.exception.ValidationFailedException;
import oracle.iam.selfservice.self.selfmgmt.api.AuthenticatedSelfService;

import com.massiveGaze.connection.Platform;

public class UserLogin {
	
	public static void main(String[] args) throws ValidationFailedException, SetChallengeValueException, Exception {
		login();

	}	
	public static void login() throws ValidationFailedException, SetChallengeValueException, Exception{
		AuthenticatedSelfService m_authselfservice = Platform.getService(AuthenticatedSelfService.class);
		m_authselfservice.setChallengeValues(setChallengeQuestiones());
	}

	 private static Map<String, Object> setChallengeQuestiones() throws Exception {
    	 System.out.println("Setting  Challenge Questions ....");
        Map<String, Object> mapQnA = new HashMap<String, Object>();			
		mapQnA.put("What is your mother's maiden name?", "4");
		mapQnA.put("What is your favorite color?", "5");
		//mapQnA.put("What is the city of your birth?", "6");
		mapQnA.put("What is the name of your pet?", "6");

        return mapQnA;
    }

}
