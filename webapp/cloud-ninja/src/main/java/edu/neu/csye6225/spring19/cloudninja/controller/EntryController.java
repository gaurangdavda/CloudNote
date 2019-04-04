package edu.neu.csye6225.spring19.cloudninja.controller;

import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.AUTHORIZATION;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.DELETE_ALL_RECORDS;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.FILE;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.HEALTH_CHECK;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.LOGIN;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.NOTE;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.NOTE_ID;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.NOTE_ID_ATTACHMENT;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.NOTE_ID_ATTACHMENTS;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.NOTE_ID_ATTACHMENT_ID;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.NO_AUTH;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.REGISTER;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.RESET_PASSWORD;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.neu.csye6225.spring19.cloudninja.exception.FileStorageException;
import edu.neu.csye6225.spring19.cloudninja.exception.ResourceNotFoundException;
import edu.neu.csye6225.spring19.cloudninja.exception.ServerException;
import edu.neu.csye6225.spring19.cloudninja.exception.UnAuthorizedLoginException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.Attachment;
import edu.neu.csye6225.spring19.cloudninja.model.Note;
import edu.neu.csye6225.spring19.cloudninja.model.PasswordReset;
import edu.neu.csye6225.spring19.cloudninja.model.ResponseBody;
import edu.neu.csye6225.spring19.cloudninja.model.TimeStampWrapper;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import edu.neu.csye6225.spring19.cloudninja.property.FileStorageProperties;
import edu.neu.csye6225.spring19.cloudninja.service.LoginService;
import edu.neu.csye6225.spring19.cloudninja.service.NoteTakingService;
import edu.neu.csye6225.spring19.cloudninja.service.PasswordResetService;

@RestController
public class EntryController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private NoteTakingService noteTakingService;

	@Autowired
	private PasswordResetService passwordResetService;

	@Autowired
	FileStorageProperties fileStorageProperties;

	@RequestMapping(method = RequestMethod.GET, value = LOGIN, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TimeStampWrapper> getTimeStamp(
			@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth)
			throws ValidationException, UnAuthorizedLoginException {
		return new ResponseEntity<TimeStampWrapper>(loginService.getTimestamp(auth), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = REGISTER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseBody> register(@RequestBody UserCredentials userCredentials)
			throws ValidationException {
		return new ResponseEntity<ResponseBody>(loginService.registerUser(userCredentials), HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.GET, value = NOTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Note>> getAllNotes(
			@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		return new ResponseEntity<List<Note>>(noteTakingService.getAllNotes(auth), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = NOTE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Note> createNote(@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth,
			@RequestBody Note note) throws ValidationException, UnAuthorizedLoginException {
		return new ResponseEntity<Note>(noteTakingService.createNote(auth, note), HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.GET, value = NOTE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Note> getNote(@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth,
			@PathVariable(value = "noteId") UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		return new ResponseEntity<Note>(noteTakingService.getNote(auth, noteId), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, value = NOTE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateNote(@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth,
			@PathVariable(value = "noteId") UUID noteId, @RequestBody Note note)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		noteTakingService.updateNote(auth, noteId, note);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = NOTE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteNote(@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth,
			@PathVariable(value = "noteId") UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException {
		noteTakingService.deleteNote(auth, noteId);
	}

	@RequestMapping(value = NOTE_ID_ATTACHMENT, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public Attachment attachFile(@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth,
			@PathVariable(value = "noteId") UUID noteId, @RequestParam(FILE) MultipartFile file)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException {
		return noteTakingService.saveAttachment(auth, noteId, file);
	}

	@RequestMapping(value = NOTE_ID_ATTACHMENTS, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public List<Attachment> attachMultipleFiles(
			@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth,
			@PathVariable(value = "noteId") UUID noteId, @RequestParam(FILE) MultipartFile[] files)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException {
		return noteTakingService.saveAttachments(auth, noteId, files);
	}

	@RequestMapping(value = NOTE_ID_ATTACHMENT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<Attachment> getAllAttachments(@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth,
			@PathVariable(value = "noteId") UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		return noteTakingService.getAttachments(auth, noteId);
	}

	@RequestMapping(value = NOTE_ID_ATTACHMENT_ID, method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateAttachment(@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth,
			@PathVariable(value = "noteId") UUID noteId, @PathVariable(value = "idAttachments") UUID attachmentId,
			@RequestParam(FILE) MultipartFile file)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException {
		noteTakingService.updateAttachment(auth, noteId, attachmentId, file);
	}

	@RequestMapping(value = NOTE_ID_ATTACHMENT_ID, method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAttachment(@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth,
			@PathVariable(value = "noteId") UUID noteId, @PathVariable(value = "idAttachments") UUID attachmentId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException {
		noteTakingService.deleteAttachment(auth, noteId, attachmentId);
	}

	@RequestMapping(value = RESET_PASSWORD, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public void resestPassword(@RequestBody @NotNull PasswordReset passwordReset)
			throws ServerException, ValidationException {
		passwordResetService.resetPassword(passwordReset);
	}

	@RequestMapping(value = HEALTH_CHECK, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public void healthCheck() {

	}

	@RequestMapping(method = RequestMethod.DELETE, value = DELETE_ALL_RECORDS)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAllRecords(@RequestHeader(value = AUTHORIZATION, defaultValue = NO_AUTH) String auth)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException {

		noteTakingService.deleteAllNotesAndAttachments(auth);
		loginService.deleteAllUsers(auth);
	}
}
