package edu.neu.csye6225.spring19.cloudninja.service.impl;

import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.EMAILID_PASSWORD_MISSING;

import java.util.List;

import edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.csye6225.spring19.cloudninja.exception.ResourceNotFoundException;
import edu.neu.csye6225.spring19.cloudninja.exception.UnAuthorizedLoginException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import edu.neu.csye6225.spring19.cloudninja.repository.UserRepository;
import edu.neu.csye6225.spring19.cloudninja.service.LoginService;
import edu.neu.csye6225.spring19.cloudninja.util.LoginServiceUtil;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginServiceUtil loginServiceUtil;

	@Autowired
	private UserRepository userRepository;

	@Override
	public String getTimestamp(String authHeader) throws ValidationException, UnAuthorizedLoginException {
		//Authenticating User before proceeding
		authenticateUser(authHeader);
		return String.valueOf(System.currentTimeMillis());
		
	}

	public String registerUser(UserCredentials userCredential) throws ValidationException {
		
		//Converting email id to lowercase
		userCredential.setEmailId(userCredential.getEmailId().toLowerCase());
		loginServiceUtil.isValidEmail(userCredential.getEmailId());
		loginServiceUtil.checkPasswordStrength(userCredential.getPassword());
		List<UserCredentials> credentialList = userRepository.findByEmailId(userCredential.getEmailId());
		if (credentialList == null || credentialList.size() == 0) {
			String password = loginServiceUtil.encryptPassword(userCredential.getPassword());
			userCredential.setPassword(password);
			userRepository.save(userCredential);
		} else {
			throw new ValidationException("User already exists. Kindly login.");
		}
		return "User created successfully.";
	}
	
	public void authenticateUser(String authHeader) throws ValidationException, UnAuthorizedLoginException {
		if(authHeader!= ApplicationConstants.NO_AUTH) {
			byte[] bytes = loginServiceUtil.getDecodedString(authHeader.split(" ")[1]);

			String userPassArr[] = new String(bytes).split(":");
			if (userPassArr.length != 2) {
				throw new ValidationException(EMAILID_PASSWORD_MISSING);
			}

			//Storing email id in lowercase
			String emailId = userPassArr[0].toLowerCase(); 
			String password = userPassArr[1];
			String actualPassword = "";
			loginServiceUtil.isValidEmail(emailId);
			List<UserCredentials> credentialList = userRepository.findByEmailId(emailId);
			if (credentialList != null && credentialList.size() == 1) {
				actualPassword = credentialList.get(0).getPassword();
				loginServiceUtil.verifyPassword(password, actualPassword);
				//return String.valueOf(System.currentTimeMillis());
			} else {
				throw new ResourceNotFoundException("Invalid user ID.");
			}
		}
		else {
			throw new UnAuthorizedLoginException();
		}
	}

}
