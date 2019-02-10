package edu.neu.csye6225.spring19.cloudninja.service;

import java.util.List;
import java.util.UUID;

import edu.neu.csye6225.spring19.cloudninja.model.Note;

public interface NoteTakingService {

	public List<Note> getAllNotes(String auth);

	public void createNote(String auth, Note note);

	public Note getNote(String auth, UUID noteId);

	public void updateNote(String auth, UUID noteId, Note note);

	public void deleteNote(String auth, UUID noteId);

}
