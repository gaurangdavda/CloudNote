/**
 * 
 */
package edu.neu.csye6225.spring19.cloudninja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author gaurang
 *
 */

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnAuthorizedLoginException extends Exception {

	private static final long serialVersionUID = 3253030044129825830L;

	public UnAuthorizedLoginException() {
		super();
	}

	public UnAuthorizedLoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnAuthorizedLoginException(String message) {
		super(message);
	}
}
