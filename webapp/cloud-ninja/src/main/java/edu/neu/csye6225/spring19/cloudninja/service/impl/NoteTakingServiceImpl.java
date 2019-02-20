package edu.neu.csye6225.spring19.cloudninja.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.neu.csye6225.spring19.cloudninja.exception.FileStorageException;
import edu.neu.csye6225.spring19.cloudninja.exception.ResourceNotFoundException;
import edu.neu.csye6225.spring19.cloudninja.exception.UnAuthorizedLoginException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.Attachment;
import edu.neu.csye6225.spring19.cloudninja.model.Note;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import edu.neu.csye6225.spring19.cloudninja.repository.AttachmentReposiory;
import edu.neu.csye6225.spring19.cloudninja.repository.NoteTakingRepository;
import edu.neu.csye6225.spring19.cloudninja.service.AuthService;
import edu.neu.csye6225.spring19.cloudninja.service.NoteTakingService;
import edu.neu.csye6225.spring19.cloudninja.service.ValidationService;
import edu.neu.csye6225.spring19.cloudninja.util.file.storage.FileStorageUtil;

@Service
public class NoteTakingServiceImpl implements NoteTakingService {

	@Autowired
	private AuthService authService;

	@Autowired
	private NoteTakingRepository noteTakingRepository;

	@Autowired
	private AttachmentReposiory attachmentReposiory;

	@Autowired
	private ValidationService validationService;

	@Autowired
	private FileStorageUtil fileStorageUtil;

	@Override
	public List<Note> getAllNotes(String auth) throws ValidationException, UnAuthorizedLoginException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(auth);
		authService.authenticateUser(credentials);
		List<Note> noteList = noteTakingRepository.getAllNotesForUser(credentials);
		return noteList;
	}

	@Override
	public Note createNote(String auth, Note note) throws ValidationException, UnAuthorizedLoginException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(auth);
		authService.authenticateUser(credentials);
		validationService.isNoteValid(note);
		note.setUserCredentials(credentials);
		Note savedNote = noteTakingRepository.save(note);
		return savedNote;
	}

	@Override
	public Note getNote(String auth, UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(auth);
		authService.authenticateUser(credentials);
		// Note note = fetchNoteFromId(noteId);
		Optional<Note> noteWrapper = noteTakingRepository.findById(noteId);
		if (!noteWrapper.isPresent() || noteWrapper.get() == null) {
			throw new ResourceNotFoundException("Note with ID: " + noteId.toString() + " does not exist");
		}
		Note note = noteWrapper.get();
		if (authService.isUserAuthorized(credentials, note)) {
			return note;
		} else {
			throw new UnAuthorizedLoginException("Note with ID: " + noteId.toString() + " is not one of your notes");
		}
	}

	@Override
	public void updateNote(String auth, UUID noteId, Note note) throws ValidationException, UnAuthorizedLoginException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(auth);
		authService.authenticateUser(credentials);
		validationService.isNoteValid(note);
		// Note nt = fetchNoteFromId(noteId);
		Optional<Note> noteWrapper = noteTakingRepository.findById(noteId);
		if (!noteWrapper.isPresent() || noteWrapper.get() == null) {
			throw new ValidationException("Note with ID: " + noteId.toString() + " does not exist");
		}
		Note nt = noteWrapper.get();

		if (authService.isUserAuthorized(credentials, nt)) {
			nt.setContent(note.getContent());
			nt.setTitle(note.getTitle());
			noteTakingRepository.save(nt);
		} else {
			throw new UnAuthorizedLoginException("Note with ID: " + noteId.toString() + " is not one of your notes");
		}
	}

	@Override
	public void deleteNote(String auth, UUID noteId) throws ValidationException, UnAuthorizedLoginException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(auth);
		authService.authenticateUser(credentials);

		// Note note = fetchNoteFromId(noteId);

		Optional<Note> noteWrapper = noteTakingRepository.findById(noteId);
		if (!noteWrapper.isPresent() || noteWrapper.get() == null) {
			throw new ValidationException("Note with ID: " + noteId.toString() + " does not exist");
		}
		Note note = noteWrapper.get();
		if (authService.isUserAuthorized(credentials, note)) {
			noteTakingRepository.delete(note);
		} else {
			throw new UnAuthorizedLoginException("Note with ID: " + noteId.toString() + " is not one of your notes");
		}

	}

	@Override
	public List<Attachment> getAttachments(String auth, UUID noteId)
			throws ValidationException, UnAuthorizedLoginException {
		return getNoteForAttachment(auth, noteId).getAttachments();
	}

	@Override
	public Attachment saveAttachment(String auth, UUID noteId, MultipartFile file)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException {

		Note note = getNoteForAttachment(auth, noteId);

		String fileLocation = fileStorageUtil.storeFile(file);
		Attachment attachment = new Attachment(fileLocation, note);
		return attachmentReposiory.save(attachment);

	}

	@Override
	public List<Attachment> saveAttachments(String auth, UUID noteId, MultipartFile[] files)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException {

		Note note = getNoteForAttachment(auth, noteId);
		List<Attachment> savedAttachments = new ArrayList<Attachment>();
		for (MultipartFile file : files) {
			String fileLocation = fileStorageUtil.storeFile(file);
			Attachment attachment = new Attachment(fileLocation, note);
			savedAttachments.add(attachmentReposiory.save(attachment));
		}

		return savedAttachments;

	}

	private Note getNoteForAttachment(String auth, UUID noteId) throws ValidationException, UnAuthorizedLoginException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(auth);
		authService.authenticateUser(credentials);
		Optional<Note> noteWrapper = noteTakingRepository.findById(noteId);
		if (!noteWrapper.isPresent() || noteWrapper.get() == null) {
			throw new ValidationException("Note with ID: " + noteId.toString() + " does not exist");
		}
		return noteWrapper.get();
	}

	@Override
	public void updateAttachment(String auth, UUID noteId, UUID attachmentId)
			throws ValidationException, UnAuthorizedLoginException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(auth);
		authService.authenticateUser(credentials);
		Note note = getNoteForAttachment(auth, noteId);

		// Update the attachment

		// String fileLocation = fileStorageUtil.replaceFile(oldFileUrl, file);
	}

	@Override
	public void deleteAttachment(String auth, UUID noteId, UUID attachmentId)
			throws ValidationException, UnAuthorizedLoginException, FileStorageException {
		UserCredentials credentials = authService.extractCredentialsFromHeader(auth);
		authService.authenticateUser(credentials);
		Note note = getNoteForAttachment(auth, noteId);

		Optional<Attachment> attachmentWrapper = attachmentReposiory.findById(attachmentId);
		if (!attachmentWrapper.isPresent() || attachmentWrapper.get() == null) {
			throw new ValidationException("Attachment with ID: " + attachmentId.toString()
					+ " does not exist in Note with ID: " + noteId.toString());
		}
		Attachment attachment = attachmentWrapper.get();
		// Deleting from the storage

		if (authService.isUserAuthorized(credentials, note)) {
			fileStorageUtil.deleteFile(attachment.getUrl());
			attachmentReposiory.delete(attachment);
		} else {
			throw new UnAuthorizedLoginException("Note with ID: " + noteId.toString() + " is not one of your notes");
		}

		// deleting the attachment

	}

}
