package edu.neu.csye6225.spring19.cloudninja.util;

import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class LoginServiceUtilTest {
//    @Autowired
    private LoginServiceUtil loginServiceUtil = new LoginServiceUtil();

    @Test
    public void checkPasswordStrength() {
        boolean flag = false;
        try {
            loginServiceUtil.checkPasswordStrength("MansOi@123");
            flag = true;
        } catch (ValidationException e) {
            e.printStackTrace();
        }finally {
            assertTrue(flag);
        }

    }



    @Test
    public void isValidEmail() {

        boolean flag = false;
        try {
            loginServiceUtil.isValidEmail("mansi@gmail.com");
            flag = true;
        } catch (ValidationException e) {
            e.printStackTrace();
        }finally {
            assertTrue(flag);
        }

    }
}