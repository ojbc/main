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
package org.ojbc.intermediaries.sn.subscription;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.util.EmailAddressValidatorResponse;
import org.ojbc.util.validator.EmailValidator;

public class EmailAddressPatternValidator {

	private List<String> allowedEmailAddressPatterns;
	
	private static final Log log = LogFactory.getLog(SubscriptionRequest.class);
	
	//read in comma separated list of allowed email address regular expressions
	public EmailAddressPatternValidator(String allowedEmailAddressRegularExpressions) {
		
		if (StringUtils.isNotBlank(allowedEmailAddressRegularExpressions))
		{	
			allowedEmailAddressPatterns = Arrays.asList(allowedEmailAddressRegularExpressions.split(","));
		}
		
	}
	
	public EmailAddressPatternValidator() {
		super();
	}

	public EmailAddressValidatorResponse areEmailAddressesValid(List<String> emailAddresses)
	{
		EmailAddressValidatorResponse emailAddressValidatorResponse = new EmailAddressValidatorResponse();
		
		if (allowedEmailAddressPatterns != null)
		{	
			log.debug("Allowed email address pattern: " + allowedEmailAddressPatterns.toString());
		}
			
		//Start with a default of true
		emailAddressValidatorResponse.setAreAllEmailAddressValid(true);

		//If allowed email patterns not set, test for generic email pattern
		if (allowedEmailAddressPatterns == null)
		{
			for (String emailAddress : emailAddresses)
			{
	
				if (!EmailValidator.validateEmailAddress(emailAddress))
				{
					emailAddressValidatorResponse.setAreAllEmailAddressValid(false);
					emailAddressValidatorResponse.getInvalidEmailAddresses().add(emailAddress);
				}	 
	
			}	
			
			return emailAddressValidatorResponse;
		}	
			
		//Loop through email addresses and see if the valid per the email address regular expression  		
		for (String emailAddress : emailAddresses)
		{
			boolean isEmailAddressValid = false;
			
			for (String regularExpression : allowedEmailAddressPatterns)
			{
				
				if (EmailValidator.testEmailAddressAgainstRegularExpression(emailAddress, regularExpression))
				{
					isEmailAddressValid = true;
				}

			}	
			
			if (!isEmailAddressValid)
			{
				emailAddressValidatorResponse.setAreAllEmailAddressValid(false);
				emailAddressValidatorResponse.getInvalidEmailAddresses().add(emailAddress);
			}	
			
		}	
		
		return emailAddressValidatorResponse;
	}

}
