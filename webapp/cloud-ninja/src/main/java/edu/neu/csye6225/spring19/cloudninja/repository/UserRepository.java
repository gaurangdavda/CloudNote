package edu.neu.csye6225.spring19.cloudninja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;

@RepositoryRestResource
public interface UserRepository  extends CrudRepository<UserCredentials,Integer> {

	@Query("select u from USR_DTLS u where u.USR_EMAIL = ?")
    public List<UserCredentials> findByEmailId (String emailId);

}
