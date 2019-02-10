package edu.neu.csye6225.spring19.cloudninja.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.csye6225.spring19.cloudninja.exception.ResourceNotFoundException;
import edu.neu.csye6225.spring19.cloudninja.exception.UnAuthorizedLoginException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.Note;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import edu.neu.csye6225.spring19.cloudninja.repository.NoteTakingRepository;
import edu.neu.csye6225.spring19.cloudninja.repository.UserRepository;
import edu.neu.csye6225.spring19.cloudninja.service.AuthService;
import edu.neu.csye6225.spring19.cloudninja.service.NoteTakingService;

@Service
public class NoteTakingServiceImpl implements NoteTakingService {

	@Autowired
	private AuthService authService;

	@Autowired
	private NoteTakingRepository noteTakingRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public List<Note> getAllNotes(String authHeader)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(authHeader);
		authService.authenticateUser(credentials);
		List<Note> noteList = noteTakingRepository.getAllNotesForUser(credentials);
		return noteList;
	}

	@Override
	public void createNote(String authHeader, Note note)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(authHeader);
		authService.authenticateUser(credentials);
		note.setUserCredentials(credentials);
		noteTakingRepository.save(note);
	}

	@Override
	public Note getNote(String authHeader, UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(authHeader);
		authService.authenticateUser(credentials);
		return null;
	}

	@Override
	public void updateNote(String authHeader, UUID noteId, Note note)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(authHeader);
		authService.authenticateUser(credentials);

	}

	@Override
	public void deleteNote(String authHeader, UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(authHeader);
		authService.authenticateUser(credentials);

	}

}
