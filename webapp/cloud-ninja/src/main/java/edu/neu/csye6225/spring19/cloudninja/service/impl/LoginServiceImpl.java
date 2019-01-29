package edu.neu.csye6225.spring19.cloudninja.service.impl;

import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.EMAILID_PASSWORD_MISSING;

import edu.neu.csye6225.spring19.cloudninja.exception.ResourceNotFoundException;
import edu.neu.csye6225.spring19.cloudninja.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import edu.neu.csye6225.spring19.cloudninja.service.LoginService;
import edu.neu.csye6225.spring19.cloudninja.util.LoginServiceUtil;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private LoginServiceUtil loginServiceUtil;

	@Autowired
	private UserRepository userRepository;

	@Override
	public String getTimestamp(String authHeader) throws ValidationException {

		// HttpHeaders headers = new HttpHeaders();
		byte[] bytes = loginServiceUtil.getDecodedString(authHeader.split(" ")[1]);

		String userPassArr[] = new String(bytes).split(":");
		if (userPassArr.length != 2) {
			throw new ValidationException(EMAILID_PASSWORD_MISSING);
		}

		String emailId = userPassArr[0];
		String password = userPassArr[1];

		loginServiceUtil.isValidEmail(emailId);

		System.out.println("Valid email" + emailId);

		// Fetch details from DB
		String passwordFromDB = "password";

		// loginServiceUtil.verifyPassword(enteredPassword, passwordFromDb);

		return String.valueOf(System.currentTimeMillis());

	}

	public String registerUser(UserCredentials userCredential) throws ValidationException {
		loginServiceUtil.checkPasswordStrength(userCredential.getPassword());

		loginServiceUtil.isValidEmail(userCredential.getEmailId());


		String password = loginServiceUtil.encryptPassword(userCredential.getPassword());
		userCredential.setPassword(password);
		userRepository.save(userCredential);

		return "abc";
	}


	public  void deleteUser(UserCredentials userCredential) throws ValidationException{

		loginServiceUtil.isValidEmail(userCredential.getEmailId());

		if(!userRepository.existsById(userCredential.getId())){
			throw new ResourceNotFoundException("User not found with id " + userCredential.getId());
		}

		userRepository.delete(userCredential);
	}

	public List<UserCredentials> getUser(UserCredentials userCredentials){
		return userRepository.findAll();
	}

}
