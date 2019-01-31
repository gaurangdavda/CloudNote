package edu.neu.csye6225.spring19.cloudninja.model;

import org.springframework.stereotype.Component;

@Component
public class TimeStampWrapper {

    private String timeStamp;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
