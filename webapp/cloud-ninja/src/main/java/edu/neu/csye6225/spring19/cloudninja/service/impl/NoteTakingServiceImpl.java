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
	public void updateNote(String auth, UUID noteId, Note note)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		validationService.isNoteValid(note);
		Note fetchedNote;
		fetchedNote = getNote(auth, noteId);
		fetchedNote.setContent(note.getContent());
		fetchedNote.setTitle(note.getTitle());
		noteTakingRepository.save(fetchedNote);
	}

	@Override
	public void deleteNote(String auth, UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {

		Note fetchedNote;
		fetchedNote = getNote(auth, noteId);
		noteTakingRepository.delete(fetchedNote);
	}

	@Override
	public List<Attachment> getAttachments(String auth, UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		Note fetchedNote;
		fetchedNote = getNote(auth, noteId);
		return fetchedNote.getAttachments();
	}

	@Override
	public Attachment saveAttachment(String auth, UUID noteId, MultipartFile file)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException {

		Note fetchedNote;
		fetchedNote = getNote(auth, noteId);
		String fileLocation = fileStorageUtil.storeFile(file);
		Attachment attachment = new Attachment(fileLocation, fetchedNote);
		return attachmentReposiory.save(attachment);
	}

	@Override
	public List<Attachment> saveAttachments(String auth, UUID noteId, MultipartFile[] files)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException {

		Note fetchedNote;
		fetchedNote = getNote(auth, noteId);
		List<Attachment> savedAttachments = new ArrayList<Attachment>();
		for (MultipartFile file : files) {
			String fileLocation = fileStorageUtil.storeFile(file);
			Attachment attachment = new Attachment(fileLocation, fetchedNote);
			savedAttachments.add(attachmentReposiory.save(attachment));
		}
		return savedAttachments;
	}

	@Override
	public void updateAttachment(String auth, UUID noteId, UUID attachmentId, MultipartFile file)
			throws ValidationException, UnAuthorizedLoginException, FileStorageException, ResourceNotFoundException {
		Note fetchedNote;
		fetchedNote = getNote(auth, noteId);
		Attachment attachment = getAttachmentForNote(attachmentId, fetchedNote);
		String fileLocation = fileStorageUtil.replaceFile(attachment.getUrl(), file);
		attachment.setUrl(fileLocation);
		attachmentReposiory.save(attachment);
	}

	@Override
	public void deleteAttachment(String auth, UUID noteId, UUID attachmentId)
			throws ValidationException, UnAuthorizedLoginException, FileStorageException, ResourceNotFoundException {
		Note fetchedNote;
		fetchedNote = getNote(auth, noteId);
		Attachment attachment = getAttachmentForNote(attachmentId, fetchedNote);
		fileStorageUtil.deleteFile(attachment.getUrl());
		attachmentReposiory.delete(attachment);
	}

	private Attachment getAttachmentForNote(UUID attachmentId, Note fetchedNote)
			throws ValidationException, UnAuthorizedLoginException {
		Optional<Attachment> attachmentWrapper = attachmentReposiory.findById(attachmentId);
		if (!attachmentWrapper.isPresent() || attachmentWrapper.get() == null) {
			throw new ValidationException("Attachment with ID: " + attachmentId.toString() + " does not exist");
		}
		Attachment attachment = attachmentWrapper.get();
//		for (Attachment a : fetchedNote.getAttachments()) {
//			if (a.getId().equals(attachment.getId())) {
//				return attachment;
//			}
//		}
		if (fetchedNote.getAttachments().contains(attachment)) {
			return attachment;
		}
		throw new UnAuthorizedLoginException(
				"Attachment with ID: " + attachmentId.toString() + " is not one of the attachments of your note");

	}

}
