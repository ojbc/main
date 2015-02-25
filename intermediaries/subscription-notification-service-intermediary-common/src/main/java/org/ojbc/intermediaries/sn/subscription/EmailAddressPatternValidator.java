package org.ojbc.intermediaries.sn.subscription;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.ojbc.intermediaries.sn.util.EmailAddressValidatorResponse;
import org.ojbc.util.validator.EmailValidator;

public class EmailAddressPatternValidator {

	private List<String> allowedEmailAddressPatterns;
	
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
