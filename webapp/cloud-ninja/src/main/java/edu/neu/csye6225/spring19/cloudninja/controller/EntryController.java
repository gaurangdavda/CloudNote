package edu.neu.csye6225.spring19.cloudninja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import edu.neu.csye6225.spring19.cloudninja.service.LoginService;


@RestController
public class EntryController {

	@Autowired
	LoginService loginService;

	@RequestMapping(method = RequestMethod.GET, value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> getTimestamp(@RequestHeader(value = "Authorization", defaultValue = "No Auth") String auth) throws ValidationException {
		loginService.getTimestamp(auth);
		//return String.valueOf(System.currentTimeMillis());
		return new ResponseEntity<String>(String.valueOf(System.currentTimeMillis()), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/user/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> userDetails(@RequestBody UserCredentials userCredentials) throws ValidationException {
		loginService.registerUser(userCredentials);
		return new ResponseEntity<String>("User Created Successfully", HttpStatus.OK);
	}
}
