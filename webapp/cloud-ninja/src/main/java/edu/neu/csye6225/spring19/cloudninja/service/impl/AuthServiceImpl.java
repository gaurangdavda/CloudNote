package edu.neu.csye6225.spring19.cloudninja.service.impl;

import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.EMAILID_PASSWORD_MISSING;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants;
import edu.neu.csye6225.spring19.cloudninja.exception.ResourceNotFoundException;
import edu.neu.csye6225.spring19.cloudninja.exception.UnAuthorizedLoginException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import edu.neu.csye6225.spring19.cloudninja.repository.UserRepository;
import edu.neu.csye6225.spring19.cloudninja.service.AuthService;
import edu.neu.csye6225.spring19.cloudninja.util.AuthServiceUtil;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AuthServiceUtil authServiceUtil;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void authenticateUser(String authHeader) throws ValidationException, UnAuthorizedLoginException {
		if (authHeader != ApplicationConstants.NO_AUTH) {
			byte[] bytes = authServiceUtil.getDecodedString(authHeader.split(" ")[1]);

			String userPassArr[] = new String(bytes).split(":");
			if (userPassArr.length != 2) {
				throw new ValidationException(EMAILID_PASSWORD_MISSING);
			}

			// Storing email id in lowercase
			String emailId = userPassArr[0];
			String password = userPassArr[1];
			String actualPassword = "";
			authServiceUtil.isValidEmail(emailId);
			List<UserCredentials> credentialList = userRepository.findByEmailId(emailId.toLowerCase());
			if (credentialList != null && credentialList.size() == 1) {
				actualPassword = credentialList.get(0).getPassword();
				authServiceUtil.verifyPassword(password, actualPassword);
			} else {
				throw new ResourceNotFoundException("Invalid user ID.");
			}
		} else {
			throw new UnAuthorizedLoginException();
		}
	}

}
