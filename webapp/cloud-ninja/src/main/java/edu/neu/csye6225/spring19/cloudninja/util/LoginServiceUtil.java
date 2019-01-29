/**
 * 
 */
package edu.neu.csye6225.spring19.cloudninja.util;

import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author gaurang
 *
 */
public class LoginServiceUtil {

	public static boolean checkPasswordStrength(String password) {
		Predicate<String> rule;
		Predicate<String> rule1 = s -> s.length() >= 8 && s.length() <= 50;
		Predicate<String> rule2a = s -> !s.equals(s.toLowerCase());
		Predicate<String> rule2b = s -> !s.equals(s.toUpperCase());
		Predicate<String> rule2c = s -> s.codePoints().anyMatch(Character::isDigit);
		Predicate<String> rule2d = s -> s.codePoints().anyMatch(i -> !Character.isAlphabetic(i));
//        Predicate<String> rule2e = s -> s.codePoints().anyMatch(i -> refString.contains(String.valueOf(s.charAt(i))));
		Predicate<String> rule2 = s -> Stream.of(rule2a, rule2b, rule2c, rule2d).filter(p -> p.test(s)).count() == 4;
		rule = rule1.and(rule2);
		return rule.test(password);
	}

	public static boolean isValidEmail(String email) {

		return true;
	}

	public static String encryptPassword(String password) {

		return "";
	}
}