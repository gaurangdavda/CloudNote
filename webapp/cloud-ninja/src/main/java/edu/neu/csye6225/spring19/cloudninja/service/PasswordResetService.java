package edu.neu.csye6225.spring19.cloudninja.service;

import edu.neu.csye6225.spring19.cloudninja.exception.ServerException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.PasswordReset;

public interface PasswordResetService {

    public void resetPassword(PasswordReset passwordReset) throws ValidationException, ServerException;
}
