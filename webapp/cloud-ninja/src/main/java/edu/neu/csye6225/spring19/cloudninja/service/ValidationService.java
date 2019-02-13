package edu.neu.csye6225.spring19.cloudninja.service;

import edu.neu.csye6225.spring19.cloudninja.exception.ResourceNotFoundException;
import edu.neu.csye6225.spring19.cloudninja.exception.UnAuthorizedLoginException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.Note;
import edu.neu.csye6225.spring19.cloudninja.model.ResponseBody;
import edu.neu.csye6225.spring19.cloudninja.model.TimeStampWrapper;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;

import java.util.UUID;

public interface ValidationService {

	Boolean isIdPresent(UUID noteId)
		throws ValidationException;

	Boolean isNoteValid(Note note)
		throws ValidationException;

}
