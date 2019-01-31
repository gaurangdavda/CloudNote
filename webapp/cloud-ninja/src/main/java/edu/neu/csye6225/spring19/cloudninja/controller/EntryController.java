package edu.neu.csye6225.spring19.cloudninja.controller;

import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.AUTHORIZATION;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.GET_ENDPOINT;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.HTTP_OK;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.NO_AUTH;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.POST_ENDPOINT;

import edu.neu.csye6225.spring19.cloudninja.model.ResponseBody;
import edu.neu.csye6225.spring19.cloudninja.model.TimeStampWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.neu.csye6225.spring19.cloudninja.exception.UnAuthorizedLoginException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import edu.neu.csye6225.spring19.cloudninja.service.LoginService;

@RestController
public class EntryController {

	@Autowired
	LoginService loginService;

	@RequestMapping(method = RequestMethod.GET, value = GET_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<TimeStampWrapper> getTimestamp(@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth)
			throws ValidationException, UnAuthorizedLoginException {
		return new ResponseEntity<TimeStampWrapper>(loginService.getTimestamp(auth), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = POST_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ResponseBody> userDetails(@RequestBody UserCredentials userCredentials) throws ValidationException {
		return new ResponseEntity<ResponseBody>(loginService.registerUser(userCredentials), HttpStatus.OK);
	}
}
