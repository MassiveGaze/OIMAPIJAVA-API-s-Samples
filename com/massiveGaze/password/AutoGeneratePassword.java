package com.massiveGaze.password;

import java.util.logging.Logger;

import oracle.iam.identity.exception.NoSuchUserException;
import oracle.iam.identity.exception.UserManagerException;
import oracle.iam.identity.usermgmt.api.UserManager;
import oracle.iam.platform.authz.exception.AccessDeniedException;

import com.massiveGaze.connection.Platform;

public class AutoGeneratePassword {

	private static Logger logger = Logger.getLogger("IAM.DEMO");

	public AutoGeneratePassword() {
	}

	public static void main(String[] args) throws NoSuchUserException, UserManagerException, AccessDeniedException {

		autoResetMethod();
	}

	public static void autoResetMethod() throws NoSuchUserException,
			UserManagerException, AccessDeniedException {
		String userLogin = "TUSER";
		UserManager userManager = (UserManager) Platform.getService(UserManager.class);
		userManager.resetPassword(userLogin, true, true);
		System.out.println("Password for user " + userLogin
				+ " has been reset successfully!");
	}
}
