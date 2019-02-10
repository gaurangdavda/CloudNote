/**
 * 
 */
package edu.neu.csye6225.spring19.cloudninja.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import edu.neu.csye6225.spring19.cloudninja.model.Note;

/**
 * @author gaurang
 *
 */
public interface NoteTakingRepository extends CrudRepository<Note, UUID> {

	@Query(value = "select n from Note n where n.emailId = :emailId")
	public List<Note> getAllNotesForUser(String emailId);

}
