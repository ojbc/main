/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
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