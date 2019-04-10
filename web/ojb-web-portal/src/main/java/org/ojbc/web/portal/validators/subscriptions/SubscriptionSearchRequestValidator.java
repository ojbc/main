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
package org.ojbc.web.portal.validators.subscriptions;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.OjbcWebConstants;
import org.ojbc.web.model.subscription.search.SubscriptionSearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class SubscriptionSearchRequestValidator implements Validator{
	
	private final Log log = LogFactory.getLog(this.getClass());	
	
    @Value("${sidRegexForSubscriptionAdminSearch:([ASXLasxl][a-zA-Z0-9]+)?}")
    String sidRegexForSubscriptionAdminSearch;
    
    @Value("${sidRegexAdminSearchValidationErrorMessage: SID must start with A, L, S or X }")
    String sidRegexAdminSearchValidationErrorMessage;
    
	@Override
	public boolean supports(Class<?> clazz) {
		return SubscriptionSearchRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		SubscriptionSearchRequest subscriptionSearchRequest = (SubscriptionSearchRequest) target; 
		log.info("Validating subscription " + subscriptionSearchRequest);
		
		if (StringUtils.isNotBlank(subscriptionSearchRequest.getSid()) 
				&& !subscriptionSearchRequest.getSid().matches(sidRegexForSubscriptionAdminSearch)){
			errors.rejectValue("sid", null, sidRegexAdminSearchValidationErrorMessage);
		}
		
	}

}


