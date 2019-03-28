package edu.neu.csye6225.spring19.cloudninja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerException extends Exception{

    /**
     *
     */
    private static final long serialVersionUID = -4292041661110777973L;

    public ServerException() {
        super();
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerException(String message) {
        super(message);
    }
}
