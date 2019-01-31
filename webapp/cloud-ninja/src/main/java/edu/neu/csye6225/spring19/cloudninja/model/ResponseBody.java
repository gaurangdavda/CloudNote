package edu.neu.csye6225.spring19.cloudninja.model;


import org.springframework.stereotype.Component;

@Component
public class ResponseBody {

    private String responseMessage;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
