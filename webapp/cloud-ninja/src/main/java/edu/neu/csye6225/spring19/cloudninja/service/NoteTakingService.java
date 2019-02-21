package edu.neu.csye6225.spring19.cloudninja.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import edu.neu.csye6225.spring19.cloudninja.exception.FileStorageException;
import edu.neu.csye6225.spring19.cloudninja.exception.ResourceNotFoundException;
import edu.neu.csye6225.spring19.cloudninja.exception.UnAuthorizedLoginException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.Attachment;
import edu.neu.csye6225.spring19.cloudninja.model.Note;

public interface NoteTakingService {

	public List<Note> getAllNotes(String auth) throws ValidationException, UnAuthorizedLoginException;

	public Note createNote(String auth, Note note) throws ValidationException, UnAuthorizedLoginException;

	public Note getNote(String auth, UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException;

	public void updateNote(String auth, UUID noteId, Note note)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException;

	public void deleteNote(String auth, UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException;

	public List<Attachment> getAttachments(String auth, UUID noteId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException;

	public void updateAttachment(String auth, UUID noteId, UUID attachmentId, MultipartFile file)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException;

	public void deleteAttachment(String auth, UUID noteId, UUID attachmentId)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException;

	public Attachment saveAttachment(String auth, UUID noteId, MultipartFile file)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException;

	public List<Attachment> saveAttachments(String auth, UUID noteId, MultipartFile[] files)
			throws ValidationException, UnAuthorizedLoginException, ResourceNotFoundException, FileStorageException;

}