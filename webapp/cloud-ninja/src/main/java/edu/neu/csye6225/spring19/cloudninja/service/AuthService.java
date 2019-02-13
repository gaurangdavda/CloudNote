package edu.neu.csye6225.spring19.cloudninja.service;

import edu.neu.csye6225.spring19.cloudninja.exception.UnAuthorizedLoginException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.Note;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;

public interface AuthService {

	public void authenticateUser(UserCredentials credentials)
			throws ValidationException, UnAuthorizedLoginException;

	public UserCredentials extractCredentialsFromHeader(String authHeader)
			throws ValidationException, UnAuthorizedLoginException;

	public boolean isUserAuthorized(UserCredentials credentials, Note note);
}
