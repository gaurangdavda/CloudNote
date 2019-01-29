package edu.neu.csye6225.spring19.cloudninja.service.impl;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import edu.neu.csye6225.spring19.cloudninja.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{

	@Override
	public boolean checkCredentials(String authHeader) {
		//HttpHeaders headers = new HttpHeaders();
		byte[] bytes = Base64.decodeBase64(authHeader.split(" ")[1]);
		String userPassArr[] = new String(bytes).split(":");
		
		System.out.println("Username:"  + userPassArr[0]);
		System.out.println("Password:"  + userPassArr[1]);
		
		return true;
	}

}
