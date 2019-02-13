package edu.neu.csye6225.spring19.cloudninja.service.impl;

import org.springframework.stereotype.Service;

import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.Note;
import edu.neu.csye6225.spring19.cloudninja.service.ValidationService;

@Service
public class ValidationServiceImpl implements ValidationService {

//    @Override
//    public Boolean isIdPresent(UUID noteId) throws ValidationException {
//        if(noteId!=null){
//            return true;
//        }
//        else{
//            throw new ValidationException();
//        }
//    }

	@Override
	public void isNoteValid(Note note) throws ValidationException {
		if (note.getContent() == null || note.getTitle() == null) {
			throw new ValidationException("Content or title is missing, Please verify");
		}

	}
}
