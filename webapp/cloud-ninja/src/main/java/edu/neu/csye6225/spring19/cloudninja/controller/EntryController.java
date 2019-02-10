package edu.neu.csye6225.spring19.cloudninja.controller;

import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.AUTHORIZATION;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.LOGIN;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.NOTE;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.NOTE_ID;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.NO_AUTH;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.REGISTER;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.neu.csye6225.spring19.cloudninja.exception.ResourceNotFoundException;
import edu.neu.csye6225.spring19.cloudninja.exception.UnAuthorizedLoginException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.Note;
import edu.neu.csye6225.spring19.cloudninja.model.ResponseBody;
import edu.neu.csye6225.spring19.cloudninja.model.TimeStampWrapper;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import edu.neu.csye6225.spring19.cloudninja.service.LoginService;
import edu.neu.csye6225.spring19.cloudninja.service.NoteTakingService;

@RestController
public class EntryController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private NoteTakingService noteTakingService;

	@RequestMapping(method = RequestMethod.GET, value = LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TimeStampWrapper> login(
			@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		return new ResponseEntity<TimeStampWrapper>(loginService.getTimestamp(auth), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = REGISTER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseBody> register(@RequestBody UserCredentials userCredentials)
			throws ValidationException {
		return new ResponseEntity<ResponseBody>(loginService.registerUser(userCredentials), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET, value = NOTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Note>> getAllNotes(
			@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		return new ResponseEntity<List<Note>>(noteTakingService.getAllNotes(auth), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = NOTE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void createNote(@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth,
			@RequestBody Note note) throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		noteTakingService.createNote(auth, note);
	}

	@RequestMapping(method = RequestMethod.GET, value = NOTE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Note> getNote(@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth,
			@RequestParam("id") UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		return new ResponseEntity<Note>(noteTakingService.getNote(auth, noteId), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = NOTE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateNote(@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth,
			@RequestParam("id") UUID noteId, @RequestBody Note note)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		noteTakingService.updateNote(auth, noteId, note);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = NOTE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteNote(@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth,
			@RequestParam UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		noteTakingService.deleteNote(auth, noteId);
	}
}
