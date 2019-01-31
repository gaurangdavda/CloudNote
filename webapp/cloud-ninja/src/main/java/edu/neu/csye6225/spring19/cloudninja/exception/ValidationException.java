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

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ValidationException extends Exception {

	private static final long serialVersionUID = 3253030044129825830L;

	public ValidationException() {
		super();
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidationException(String message) {
		super(message);
	}
}
