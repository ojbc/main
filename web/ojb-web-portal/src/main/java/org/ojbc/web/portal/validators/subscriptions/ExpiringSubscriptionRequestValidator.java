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

import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.model.rapback.ExpiringSubscriptionRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class ExpiringSubscriptionRequestValidator implements Validator{
	
	private final Log log = LogFactory.getLog(this.getClass());	
	
	@Override
	public boolean supports(Class<?> clazz) {
		return ExpiringSubscriptionRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ExpiringSubscriptionRequest expiringSubscriptionRequest = (ExpiringSubscriptionRequest) target; 
		log.info("Validating expiringSubscriptionRequest/expiredSubscriptionRequest " + expiringSubscriptionRequest);
		
		if (Objects.isNull(expiringSubscriptionRequest.getOris()) 
				|| expiringSubscriptionRequest.getOris().isEmpty()) {
			errors.reject(null, "Please select one or more agencies");
		}
		
	}

}


