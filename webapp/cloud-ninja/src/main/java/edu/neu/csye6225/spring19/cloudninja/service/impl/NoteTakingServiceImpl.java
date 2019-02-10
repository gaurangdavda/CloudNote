package edu.neu.csye6225.spring19.cloudninja.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import edu.neu.csye6225.spring19.cloudninja.model.Note;
import edu.neu.csye6225.spring19.cloudninja.service.NoteTakingService;

@Service
public class NoteTakingServiceImpl implements NoteTakingService {

	@Override
	public List<Note> getAllNotes(String auth) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createNote(String auth, Note note) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Note getNote(String auth, UUID noteId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateNote(String auth, UUID noteId, Note note) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteNote(String auth, UUID noteId) {
		// TODO Auto-generated method stub
		
	}

}
