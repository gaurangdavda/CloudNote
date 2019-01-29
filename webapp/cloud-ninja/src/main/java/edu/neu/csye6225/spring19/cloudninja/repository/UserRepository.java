package edu.neu.csye6225.spring19.cloudninja.repository;

import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<UserCredentials,Integer> {



}
