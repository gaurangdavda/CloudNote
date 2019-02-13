package edu.neu.csye6225.spring19.cloudninja.service;

import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.Note;

public interface ValidationService {

//	Boolean isIdPresent(UUID noteId) throws ValidationException;

	void isNoteValid(Note note) throws ValidationException;

}
