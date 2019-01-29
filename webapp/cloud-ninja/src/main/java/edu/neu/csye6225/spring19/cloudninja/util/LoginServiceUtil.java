/**
 * 
 */
package edu.neu.csye6225.spring19.cloudninja.util;

import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.EMAILID_REGEX;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.INVALID_EMAIL;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.NULL_EMAIL;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.NULL_PASSWORD;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.PASSWORD_INCORRECT;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.WEAK_PASSWORD;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import edu.neu.csye6225.spring19.cloudninja.exception.UnAuthorizedLoginException;
import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;

/**
 * @author gaurang
 *
 */
@Component
@Scope(value = "singleton")
public class LoginServiceUtil {

	public void checkPasswordStrength(String password) throws ValidationException {
		Predicate<String> rule;
		Predicate<String> rule1 = s -> s.length() >= 8 && s.length() <= 50;
		Predicate<String> rule2a = s -> !s.equals(s.toLowerCase());
		Predicate<String> rule2b = s -> !s.equals(s.toUpperCase());
		Predicate<String> rule2c = s -> s.codePoints().anyMatch(Character::isDigit);
		Predicate<String> rule2d = s -> s.codePoints().anyMatch(i -> !Character.isAlphabetic(i));
		Predicate<String> rule2 = s -> Stream.of(rule2a, rule2b, rule2c, rule2d).filter(p -> p.test(s)).count() >= 3;
		rule = rule1.and(rule2);
		if (password != null && !password.isEmpty()) {
			boolean isValidPassword = rule.test(password);
			if (!isValidPassword) {
				throw new ValidationException(WEAK_PASSWORD);
			}
		} else {
			throw new ValidationException(NULL_PASSWORD);
		}

	}

	public void isValidEmail(String email) throws ValidationException {
		String emailRegex = EMAILID_REGEX;
		Pattern emailPattern = Pattern.compile(emailRegex);
		if (email != null && !email.isEmpty()) {
			Matcher emailMatcher = emailPattern.matcher(email);
			boolean isValidEmail = emailMatcher.find();
			if (!isValidEmail) {
				throw new ValidationException(INVALID_EMAIL);
			}
		} else {
			throw new ValidationException(NULL_EMAIL);
		}
	}

	public String encryptPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt(10));
	}

	public void verifyPassword(String enteredPassword, String passwordFromDb) throws UnAuthorizedLoginException {

		if (!BCrypt.checkpw(enteredPassword, passwordFromDb)) {
			throw new UnAuthorizedLoginException(PASSWORD_INCORRECT);
		}
	}

	public byte[] getDecodedString(String authDetails) {
		return Base64.decodeBase64(authDetails);
	}

	public static void main(String[] args) {
		System.out.println(WEAK_PASSWORD);
	}
}