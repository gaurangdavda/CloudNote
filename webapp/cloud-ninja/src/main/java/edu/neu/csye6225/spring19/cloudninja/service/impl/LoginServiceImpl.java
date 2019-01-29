package edu.neu.csye6225.spring19.cloudninja.service.impl;

import edu.neu.csye6225.spring19.cloudninja.util.LoginServiceUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import edu.neu.csye6225.spring19.cloudninja.service.LoginService;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;

@Service
public class LoginServiceImpl implements LoginService{

	@Override
	public boolean checkCredentials(String authHeader) {
		//HttpHeaders headers = new HttpHeaders();
		byte[] bytes = Base64.decodeBase64(authHeader.split(" ")[1]);
		String userPassArr[] = new String(bytes).split(":");
		// checking remaining for time being
		return true;
	}

	public String registerUser(UserCredentials userCredential){
		if (LoginServiceUtil.checkPasswordStrength(userCredential.getPassword()) && LoginServiceUtil.isValidEmail(userCredential.getEmailId()))
		{
			String password = LoginServiceUtil.encryptPassword(userCredential.getPassword());
		}
		return "abc";
	}

}
