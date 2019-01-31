package edu.neu.csye6225.spring19.cloudninja.service.impl;

import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginServiceImplTest {

    @Test
    public void registerUser() {

        LoginServiceImpl loginService = new LoginServiceImpl();

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setEmailId("mansi@gmail.com");
        userCredentials.setPassword("xyz");

       // loginService.registerUser(userCredentials);


     //  assertEquals();
    }
}