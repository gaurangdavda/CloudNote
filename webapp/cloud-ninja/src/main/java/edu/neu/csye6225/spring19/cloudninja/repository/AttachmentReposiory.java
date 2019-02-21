package edu.neu.csye6225.spring19.cloudninja.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import edu.neu.csye6225.spring19.cloudninja.model.Attachment;
import edu.neu.csye6225.spring19.cloudninja.model.Note;

public interface AttachmentReposiory extends CrudRepository<Attachment, UUID> {

	@Query(value = "select a from Attachment a where a.note = :note")
	public List<Attachment> getAllAttachmentsForUser(Note note);
}
