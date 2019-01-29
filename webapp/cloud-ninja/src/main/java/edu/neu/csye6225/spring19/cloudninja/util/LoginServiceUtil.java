/**
 * 
 */
package edu.neu.csye6225.spring19.cloudninja.util;

import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.INVALID_EMAIL;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.NULL_EMAIL;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.NULL_PASSWORD;
import static edu.neu.csye6225.spring19.cloudninja.constants.ApplicationConstants.WEAK_PASSWORD;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import edu.neu.csye6225.spring19.cloudninja.exception.ValidationException;

/**
 * @author gaurang
 *
 */
public class LoginServiceUtil {

	public static boolean checkPasswordStrength(String password) throws ValidationException {
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
			if (!isValidPassword)
				throw new ValidationException(WEAK_PASSWORD);
			return isValidPassword;
		} else {
			throw new ValidationException(NULL_PASSWORD);
		}

	}

	public static boolean isValidEmail(String email) throws ValidationException {
		String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
		Pattern emailPattern = Pattern.compile(emailRegex);
		if (email != null && !email.isEmpty()) {
			Matcher emailMatcher = emailPattern.matcher(email);
			boolean isValidEmail = emailMatcher.find();
			if (!isValidEmail)
				throw new ValidationException(INVALID_EMAIL);
			return isValidEmail;
		} else {
			throw new ValidationException(NULL_EMAIL);
		}
	}

	public static String encryptPassword(String password) {

		return "";
	}
}