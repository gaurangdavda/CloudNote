package edu.neu.csye6225.spring19.cloudninja.service;

import edu.neu.csye6225.spring19.cloudninja.exception.UnAuthorizedLoginException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;

public interface AuthService {

	public void authenticateUser(String authHeader) throws ValidationException, UnAuthorizedLoginException;
}
