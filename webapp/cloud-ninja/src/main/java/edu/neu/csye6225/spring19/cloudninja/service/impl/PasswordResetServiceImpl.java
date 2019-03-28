package edu.neu.csye6225.spring19.cloudninja.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.neu.csye6225.spring19.cloudninja.aws.AWSSNSClient;
import edu.neu.csye6225.spring19.cloudninja.exception.ServerException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import edu.neu.csye6225.spring19.cloudninja.model.PasswordReset;
import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import edu.neu.csye6225.spring19.cloudninja.repository.UserRepository;
import edu.neu.csye6225.spring19.cloudninja.service.PasswordResetService;
import edu.neu.csye6225.spring19.cloudninja.util.AuthServiceUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private AuthServiceUtil authServiceUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AWSSNSClient awssnsClient;

    @Autowired
    private ObjectMapper mapper;

    private static final Logger logger = LogManager.getLogger(PasswordResetServiceImpl.class);

    @Override
    public void resetPassword(PasswordReset passwordReset) throws ValidationException, ServerException {
        //Validate the request received
        authServiceUtil.isValidEmail(passwordReset.getEmailId());

        //Check if email exists in database
        List<UserCredentials> credentialList = userRepository.findByEmailId(passwordReset.getEmailId());
        if (credentialList == null && credentialList.size() != 1)
            throw new ValidationException("Email ID does not exist in the system");


        //Call the SNS Topic to trigger the password reset request
        String messageId = "";
        try{
            messageId =  awssnsClient.publishToTopic(mapper.writeValueAsString(passwordReset));
        } catch (JsonProcessingException jsonParsingException) {
            logger.error("Unable to parse the PasswordReset request json", jsonParsingException);
            throw new ServerException("Unable to parse the PasswordReset request json", jsonParsingException);
        }

        logger.info("Message ID : " + messageId);
    }


}
