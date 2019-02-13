package edu.neu.csye6225.spring19.cloudninja.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import edu.neu.csye6225.spring19.cloudninja.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.neu.csye6225.spring19.cloudninja.exception.ResourceNotFoundException;
import edu.neu.csye6225.spring19.cloudninja.exception.UnAuthorizedLoginException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.Note;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import edu.neu.csye6225.spring19.cloudninja.repository.NoteTakingRepository;
import edu.neu.csye6225.spring19.cloudninja.service.AuthService;
import edu.neu.csye6225.spring19.cloudninja.service.NoteTakingService;

@Service
public class NoteTakingServiceImpl implements NoteTakingService {

	@Autowired
	private AuthService authService;

	@Autowired
	private NoteTakingRepository noteTakingRepository;

	@Autowired
	private ValidationService validationService;


	@Override
	public List<Note> getAllNotes(String authHeader)
			throws ValidationException, UnAuthorizedLoginException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(authHeader);
		authService.authenticateUser(credentials);
		List<Note> noteList = noteTakingRepository.getAllNotesForUser(credentials);
		return noteList;
	}

	@Override
	public Note createNote(String authHeader, Note note)
			throws ValidationException, UnAuthorizedLoginException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(authHeader);
		authService.authenticateUser(credentials);
		validationService.isNoteValid(note);
		note.setUserCredentials(credentials);
		Note savedNote = noteTakingRepository.save(note);
		return savedNote;
	}

	@Override
	public Note getNote(String authHeader, UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(authHeader);
		authService.authenticateUser(credentials);
		//Note note = fetchNoteFromId(noteId);
		Optional<Note> noteWrapper = noteTakingRepository.findById(noteId);
		if(!noteWrapper.isPresent() || noteWrapper.get() == null) {
			throw new ResourceNotFoundException("Note with ID: " + noteId.toString() + " does not exist");
		}
		Note note = noteWrapper.get();
		if (authService.isUserAuthorized(credentials, note)) {
			return note;
		} else {
			throw new UnAuthorizedLoginException();
		}
	}

	@Override
	public void updateNote(String authHeader, UUID noteId, Note note)
			throws ValidationException, UnAuthorizedLoginException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(authHeader);
		authService.authenticateUser(credentials);
		validationService.isNoteValid(note);
		//Note nt = fetchNoteFromId(noteId);
		Optional<Note> noteWrapper = noteTakingRepository.findById(noteId);
		if(!noteWrapper.isPresent() || noteWrapper.get() == null) {
			throw new ValidationException("Note with ID: " + noteId.toString() + " does not exist");
		}
		Note nt = noteWrapper.get();
		
		if(authService.isUserAuthorized(credentials, nt)) {
			nt.setContent(note.getContent());
			nt.setTitle(note.getTitle());
			noteTakingRepository.save(nt);
		} else {
			throw new UnAuthorizedLoginException();
		}
	}

	@Override
	public void deleteNote(String authHeader, UUID noteId)
			throws ValidationException, UnAuthorizedLoginException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(authHeader);
		authService.authenticateUser(credentials);
		
		//Note note = fetchNoteFromId(noteId);
		
		Optional<Note> noteWrapper = noteTakingRepository.findById(noteId);
		if(!noteWrapper.isPresent() || noteWrapper.get() == null) {
			throw new ValidationException("Note with ID: " + noteId.toString() + " does not exist");
		}
		Note note = noteWrapper.get();
		if (authService.isUserAuthorized(credentials, note)) {
			noteTakingRepository.delete(note);
		} else {
			throw new UnAuthorizedLoginException();
		}

	}

	//Removing since we need seperate HTTP response codes for GET,PUT, DELETE
//	private Note fetchNoteFromId(UUID noteId) throws ResourceNotFoundException {
//		Optional<Note> noteWrapper = noteTakingRepository.findById(noteId);
//		if(!noteWrapper.isPresent() || noteWrapper.get() == null) {
//			throw new ResourceNotFoundException("Note with ID: " + noteId.toString() + " does not exist");
//		}
//		
//		//Returns the note which was found
//		return noteWrapper.get();
//	}

}
