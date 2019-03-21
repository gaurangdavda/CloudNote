/**
 * 
 */
package edu.neu.csye6225.spring19.cloudninja.constants;

/**
 * @author gaurang
 *
 */
public class ApplicationConstants {

	
	// Username and Password Validation exceptions
	// =========================================================================================================================
	public static final String NULL_PASSWORD = "Kindly enter the password.";
	public static final String WEAK_PASSWORD = "Password is weak. Kindly enter the password with the following specifications."
			+ " 1) Length should be >= 8 and <= 50." + " 2) Should contain atleast 1 lower case character."
			+ " 3) Should contain atleast 1 upper case character." + " 4) Should contain atleast 1 digit."
			+ " 5) Should contain atleast 1 alphabet" + " 6) Shoould not contain spaces."
			+ " 7) Should not be Password@123." + " 8) Should contain one of the special characters @$!%*?._#^&";
	public static final String NULL_EMAIL = "Kindly enter the emaild id.";
	public static final String INVALID_EMAIL = "Kindly enter valid emaild id.";
	public static final String PASSWORD_INCORRECT = "Incorrect password entered.";
	public static final String EMAILID_PASSWORD_MISSING = "EmailId/Username or Password not entered";
	public static final String EMAILID_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
	// =========================================================================================================================

	// Endpoints for this service
	// =========================================================================================================================
	public static final String LOGIN = "/";
	public static final String NOTE_ID = "/note/{noteId}";
	public static final String NOTE = "/note";
	public static final String REGISTER = "/user/register";
	public static final String HTTP_OK = "User Created Successfully";
	public static final String AUTHORIZATION = "Authorization";
	public static final String NO_AUTH = "No Auth";
	public static final String NOTE_ID_ATTACHMENT = "/note/{noteId}/attachments";
	public static final String NOTE_ID_ATTACHMENTS = "/note/{noteId}/multiattachments";
	public static final String NOTE_ID_ATTACHMENT_ID = "/note/{noteId}/attachments/{idAttachments}";
	public static final String FILE = "file";

	// =========================================================================================================================

	// Date format
	// =========================================================================================================================
	public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZ'Z'";

}
