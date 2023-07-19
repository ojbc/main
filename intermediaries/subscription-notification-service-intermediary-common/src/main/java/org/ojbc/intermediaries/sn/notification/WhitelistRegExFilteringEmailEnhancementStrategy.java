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
