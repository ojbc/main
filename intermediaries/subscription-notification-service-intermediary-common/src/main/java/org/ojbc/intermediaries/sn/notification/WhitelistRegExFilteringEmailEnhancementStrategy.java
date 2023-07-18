package org.ojbc.intermediaries.sn.notification;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.subscription.EmailAddressPatternValidator;
import org.ojbc.intermediaries.sn.util.EmailAddressValidatorResponse;

public class WhitelistRegExFilteringEmailEnhancementStrategy implements EmailEnhancementStrategy {
    private static final Log log = LogFactory.getLog(WhitelistRegExFilteringEmailEnhancementStrategy.class);
    
	private String allowedEmailAddressPatterns;
	
	public String getAllowedEmailAddressPatterns() {
		return allowedEmailAddressPatterns;
	}

	public void setAllowedEmailAddressPatterns(String allowedEmailAddressPatterns) {
		this.allowedEmailAddressPatterns = allowedEmailAddressPatterns;
	}

	@Override
	public EmailNotification enhanceEmail(EmailNotification emailNotification) {
		
		EmailNotification ret = emailNotification;
		
		EmailAddressPatternValidator emailAddressPatternValidator = new EmailAddressPatternValidator(allowedEmailAddressPatterns);
		
		String[] toAddressSplit = ret.getToAddressees().split(",");
		
		//Check email addresses here
		EmailAddressValidatorResponse emailAddressValidatorResponse = emailAddressPatternValidator.areEmailAddressesValid(new ArrayList<String>(Arrays.asList(toAddressSplit)));
		
		if (!emailAddressValidatorResponse.isAreAllEmailAddressValid()) {
			for(String email : emailAddressValidatorResponse.getInvalidEmailAddresses()) {
				if(StringUtils.isNotBlank(email)) {
					log.info("Following email address is invalid: " + email);
					ret.addBlockedEmailAddress(email);
				}
			}
		}
		
		return ret;
		
	}
}
