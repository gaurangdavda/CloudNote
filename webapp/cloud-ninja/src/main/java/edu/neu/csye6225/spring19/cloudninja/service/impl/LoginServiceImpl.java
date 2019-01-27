package edu.neu.csye6225.spring19.cloudninja.service.impl;

import org.springframework.stereotype.Service;

import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import edu.neu.csye6225.spring19.cloudninja.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{

	@Override
	public boolean checkCredentials(UserCredentials userCredentials) {
		
		return true;
	}

}
