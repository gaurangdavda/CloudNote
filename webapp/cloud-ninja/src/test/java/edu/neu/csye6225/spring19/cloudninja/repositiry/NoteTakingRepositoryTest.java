/**
 * 
 */
package edu.neu.csye6225.spring19.cloudninja.repositiry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import edu.neu.csye6225.spring19.cloudninja.repository.UserRepository;

/**
 * @author gaurang
 *
 */
@ContextConfiguration(classes = {})
@ActiveProfiles("test")
@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
public class NoteTakingRepositoryTest {

//	@Autowired
//	private NoteTakingRepository noteTakingRepository;

	@Autowired
	private UserRepository userRepository;

	@Test
	public void testUserRepo() {

		System.out.println(userRepository.findAll());
	}
}