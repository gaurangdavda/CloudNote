package edu.neu.csye6225.spring19.cloudninja.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import edu.neu.csye6225.spring19.cloudninja.util.CommonUtil;
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

	@Autowired
	private CommonUtil commonUtil;

	private static final Logger logger = LogManager.getLogger(NoteTakingServiceImpl.class);

	@Override
	public List<Note> getAllNotes(String auth) throws ValidationException, UnAuthorizedLoginException {
		try {
			UserCredentials credentials = authService.extractCredentialsFromHeader(auth);
			authService.authenticateUser(credentials);
			List<Note> noteList = noteTakingRepository.getAllNotesForUser(credentials);
			return noteList;
		} catch (Exception e) {
			logger.log(Level.ERROR, commonUtil.stackTraceString(e));
			throw e;
		}
	}

	@Override
	public Note createNote(String auth, Note note) throws ValidationException, UnAuthorizedLoginException {
		try {
			UserCredentials credentials = authService.extractCredentialsFromHeader(auth);
			authService.authenticateUser(credentials);
			validationService.isNoteValid(note);
			note.setUserCredentials(credentials);
			Note savedNote = noteTakingRepository.save(note);
			return savedNote;
		} catch (Exception e) {
			logger.error(commonUtil.stackTraceString(e));
			throw e;
		}
	}

	@Override
	public Note getNote(String auth, UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		try {
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
				throw new UnAuthorizedLoginException(
						"Note with ID: " + noteId.toString() + " is not one of your notes");
			}
		} catch (Exception e) {
			logger.error(commonUtil.stackTraceString(e));
			throw e;
		}
	}

	@Override
	public void updateNote(String auth, UUID noteId, Note note)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		try {
			validationService.isNoteValid(note);
			Note fetchedNote;
			fetchedNote = getNote(auth, noteId);
			fetchedNote.setContent(note.getContent());
			fetchedNote.setTitle(note.getTitle());
			noteTakingRepository.save(fetchedNote);
		} catch (Exception e) {
			logger.error(commonUtil.stackTraceString(e));
			throw e;
		}
	}

	@Override
	public void deleteNote(String auth, UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException {
		try {
			Note fetchedNote;
			fetchedNote = getNote(auth, noteId);
			List<Attachment> attachments = fetchedNote.getAttachments();
			if (attachments != null && attachments.size() > 0) {
				for (Attachment a : attachments) {
					fileStorageUtil.deleteFile(a.getUrl());
					attachmentReposiory.delete(a);
				}
			}
			noteTakingRepository.delete(fetchedNote);
		} catch (Exception e) {
			logger.error(commonUtil.stackTraceString(e));
			throw e;
		}
	}

	@Override
	public List<Attachment> getAttachments(String auth, UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException {
		try {
			Note fetchedNote;
			fetchedNote = getNote(auth, noteId);
			return fetchedNote.getAttachments();
		} catch (Exception e) {
			logger.error(commonUtil.stackTraceString(e));
			throw e;
		}
	}

	@Override
	public Attachment saveAttachment(String auth, UUID noteId, MultipartFile file)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException {
		try {
			Note fetchedNote;
			fetchedNote = getNote(auth, noteId);
			String fileLocation = fileStorageUtil.storeFile(file);
			Attachment attachment = new Attachment(fileLocation, fetchedNote);
			attachment.setFileName(commonUtil.getFileNameFromPath(fileLocation));
			return attachmentReposiory.save(attachment);
		} catch (Exception e) {
			logger.error(commonUtil.stackTraceString(e));
			throw e;
		}
	}

	@Override
	public List<Attachment> saveAttachments(String auth, UUID noteId, MultipartFile[] files)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException {
		try {
			Note fetchedNote;
			fetchedNote = getNote(auth, noteId);
			List<Attachment> savedAttachments = new ArrayList<Attachment>();
			for (MultipartFile file : files) {
				String fileLocation = fileStorageUtil.storeFile(file);
				Attachment attachment = new Attachment(fileLocation, fetchedNote);
				attachment.setFileName(commonUtil.getFileNameFromPath(fileLocation));
				savedAttachments.add(attachmentReposiory.save(attachment));
			}
			return savedAttachments;
		} catch (Exception e) {
			logger.error(commonUtil.stackTraceString(e));
			throw e;
		}
	}

	@Override
	public void updateAttachment(String auth, UUID noteId, UUID attachmentId, MultipartFile file)
			throws ValidationException, UnAuthorizedLoginException, FileStorageException, ResourceNotFoundException {
		try {
			Note fetchedNote;
			fetchedNote = getNote(auth, noteId);
			Attachment attachment = getAttachmentForNote(attachmentId, fetchedNote);
			String fileLocation = fileStorageUtil.replaceFile(attachment.getUrl(), file);
			attachment.setUrl(fileLocation);
			attachment.setFileName(commonUtil.getFileNameFromPath(fileLocation));
			attachmentReposiory.save(attachment);
		} catch (Exception e) {
			logger.error(commonUtil.stackTraceString(e));
			throw e;
		}
	}

	@Override
	public void deleteAttachment(String auth, UUID noteId, UUID attachmentId)
			throws ValidationException, UnAuthorizedLoginException, FileStorageException, ResourceNotFoundException {
		try {
			Note fetchedNote;
			fetchedNote = getNote(auth, noteId);
			Attachment attachment = getAttachmentForNote(attachmentId, fetchedNote);
			fileStorageUtil.deleteFile(attachment.getUrl());
			attachmentReposiory.delete(attachment);
		} catch (Exception e) {
			logger.error(commonUtil.stackTraceString(e));
			throw e;
		}
	}

	private Attachment getAttachmentForNote(UUID attachmentId, Note fetchedNote)
			throws ValidationException, UnAuthorizedLoginException {
		Optional<Attachment> attachmentWrapper = attachmentReposiory.findById(attachmentId);
		if (!attachmentWrapper.isPresent() || attachmentWrapper.get() == null) {
			throw new ValidationException("Attachment with ID: " + attachmentId.toString() + " does not exist");
		}
		Attachment attachment = attachmentWrapper.get();
		if (fetchedNote.getAttachments().contains(attachment)) {
			return attachment;
		}
		throw new UnAuthorizedLoginException(
				"Attachment with ID: " + attachmentId.toString() + " is not one of the attachments of your note");

	}

	@Override
	public void deleteAllNotesAndAttachments(String auth)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException {

		try {
			attachmentReposiory.deleteAll();
			noteTakingRepository.deleteAll();
		} catch (Exception e) {
			logger.log(Level.ERROR, commonUtil.stackTraceString(e));
			throw e;
		}
	}

}
