/**
 * 
 */
package edu.neu.csye6225.spring19.cloudninja.constants;

/**
 * @author gaurang
 *
 */
public class ApplicationConstants {

	public static final String NULL_PASSWORD = "Kindly enter the password.";
	public static final String WEAK_PASSWORD = "Password is weak. Kindly enter the password with the following specifications."
			+ "\n1) Length should be >= 8 and <= 50." + "\n2) Should contain atleast 1 lower case character."
			+ "\n3) Should contain atleast 1 upper case character." + "\n4) Should contain atleast 1 digit.";
	public static final String NULL_EMAIL = "Kindly enter the emaild id.";
	public static final String INVALID_EMAIL = "Kindly enter valid emaild id.";
	public static final String PASSWORD_INCORRECT = "Incorrect password entered.";
	public static final String GET_ENDPOINT = "/";
	public static final String POST_ENDPOINT = "/user/register";
	public static final String HTTP_OK = "User Created Successfully";
	public static final String AUTHORIZATION = "Authorization";
	public static final String NO_AUTH = "No Auth";
	public static final String EMAILID_PASSWORD_MISSING = "EmailId/Username or Password not entered";
	public static final String EMAILID_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
}
