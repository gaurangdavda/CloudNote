package edu.neu.csye6225.spring19.cloudninja.controller;


import edu.neu.csye6225.spring19.cloudninja.model.UserCredentials;
import edu.neu.csye6225.spring19.cloudninja.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
public class EntryController {

    @Autowired
    LoginService loginService;

    @RequestMapping(method = RequestMethod.GET, value="/",produces = MediaType.APPLICATION_JSON_VALUE)
    String getTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }

    @RequestMapping(method=RequestMethod.POST, value="/user/register", consumes = MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
    String userDetails(UserCredentials userCredentials){
        loginService.checkCredentials(userCredentials);
        return "";
    }
}
