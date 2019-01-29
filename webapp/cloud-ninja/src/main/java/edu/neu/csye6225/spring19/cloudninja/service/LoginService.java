package edu.neu.csye6225.spring19.cloudninja.service;

import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;

public interface LoginService {
	public String getTimestamp(String authHeader) throws ValidationException;

	public String registerUser(UserCredentials userCredentials) throws ValidationException;
}
