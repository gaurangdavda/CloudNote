package edu.neu.csye6225.spring19.cloudninja.service;

import edu.neu.csye6225.spring19.cloudninja.exception.ResourceNotFoundException;
import edu.neu.csye6225.spring19.cloudninja.exception.UnAuthorizedLoginException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.ResponseBody;
import edu.neu.csye6225.spring19.cloudninja.model.TimeStampWrapper;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;

public interface LoginService {

	public TimeStampWrapper getTimestamp(String authHeader) throws ValidationException, UnAuthorizedLoginException;

	public ResponseBody registerUser(UserCredentials userCredentials) throws ValidationException;

	public void deleteAllUsers(String auth)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException;

}
