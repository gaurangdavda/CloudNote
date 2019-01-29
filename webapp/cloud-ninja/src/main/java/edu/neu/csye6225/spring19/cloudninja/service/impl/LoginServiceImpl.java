package edu.neu.csye6225.spring19.cloudninja.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import edu.neu.csye6225.spring19.cloudninja.service.LoginService;
import edu.neu.csye6225.spring19.cloudninja.util.LoginServiceUtil;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginServiceUtil loginServiceUtil;

	@Override
	public String getTimestamp(String authHeader) throws ValidationException {

		// HttpHeaders headers = new HttpHeaders();
		byte[] bytes = loginServiceUtil.getDecodedString(authHeader.split(" ")[1]);

		String userPassArr[] = new String(bytes).split(":");
		if(userPassArr.length != 2) {
			throw new ValidationException("Username or Password not entered");
		}
		
		String emailId = userPassArr[0];
		String password = userPassArr[1];
		
		
		
		loginServiceUtil.isValidEmail(emailId);
		
		System.out.println("Valid email" + emailId);
		
		
		//Fetch details from DB
		String passwordFromDB = "password";
		
		//loginServiceUtil.verifyPassword(enteredPassword, passwordFromDb);
		
		


		return String.valueOf(System.currentTimeMillis());

	}

	public String registerUser(UserCredentials userCredential) throws ValidationException {
		loginServiceUtil.checkPasswordStrength(userCredential.getPassword());

		loginServiceUtil.isValidEmail(userCredential.getEmailId());

		String password = loginServiceUtil.encryptPassword(userCredential.getPassword());
		
		return "abc";
	}

}
