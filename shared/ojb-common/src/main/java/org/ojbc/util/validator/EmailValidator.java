package org.ojbc.util.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Please see this link:
 * http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
 * 
 * @author yogeshchawla
 *
 */

public class EmailValidator {
 
	private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.?[A-Za-z]{2,})$";
 
	/**
	 * Validate emailAddress with regular expression
	 * 
	 * @param emailAddress
	 *            emailAddress for validation
	 * @return true valid emailAddress, false invalid emailAddress
	 */
	public static boolean validateEmailAddress(final String emailAddress) {
 
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(emailAddress);
		return matcher.matches();
 
	}
	
	public static boolean testEmailAddressAgainstRegularExpression(String emailAddress, String regularExpression) {

		Pattern pattern;
		Matcher matcher;

		pattern = Pattern.compile(regularExpression);
		
		matcher = pattern.matcher(emailAddress);
		return matcher.matches();

		
	}
}