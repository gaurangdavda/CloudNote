package edu.neu.csye6225.spring19.cloudninja;

import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.ADMIN_EMAIL;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.ADMIN_PASSWD;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import edu.neu.csye6225.spring19.cloudninja.repository.UserRepository;
import edu.neu.csye6225.spring19.cloudninja.util.AuthServiceUtil;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AuthServiceUtil authServiceUtil;

	/**
	 * This event is executed as late as conceivably possible to indicate that the
	 * application is ready to service requests.
	 */
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {

		// here your code ...
		List<UserCredentials> credentialList = userRepository.findByEmailId(ADMIN_EMAIL);
		if (credentialList == null || credentialList.size() == 0) {
			UserCredentials credentials = new UserCredentials();
			String password = authServiceUtil.encryptPassword(ADMIN_PASSWD);
			credentials.setEmailId(ADMIN_EMAIL);
			credentials.setPassword(password);
			userRepository.save(credentials);
		}
		return;
	}

}