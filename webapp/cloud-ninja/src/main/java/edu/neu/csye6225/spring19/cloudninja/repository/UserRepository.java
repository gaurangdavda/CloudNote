package edu.neu.csye6225.spring19.cloudninja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;

@RepositoryRestResource
public interface UserRepository  extends CrudRepository<UserCredentials,Long> {

	@Query(value = "select u from UserCredentials u where u.emailId = :emailId")
    public List<UserCredentials> findByEmailId (String emailId);

}
