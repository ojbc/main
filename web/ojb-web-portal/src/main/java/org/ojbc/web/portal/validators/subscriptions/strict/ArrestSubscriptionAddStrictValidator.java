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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.web.portal.validators.subscriptions.strict;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.xml.subscription.Subscription;
import org.ojbc.web.portal.validators.subscriptions.AbstractArrestSubscriptionValidator;
import org.ojbc.web.portal.validators.subscriptions.ArrestSubscriptionValidatorInterface;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service("arrestSubscriptionAddStrictValidator")
public class ArrestSubscriptionAddStrictValidator extends AbstractArrestSubscriptionValidator  
	implements ArrestSubscriptionValidatorInterface{
	
	private final Log logger = LogFactory.getLog(this.getClass());	
	
	public void validate(Subscription subscription, Errors errors){
						
		logger.info("* * * inside validate()");		
		
		if(subscription == null){
			return;
		}
				
		Map<String, String> fieldToErrorMap = getValidationErrorsList(subscription);
		
		if(fieldToErrorMap ==  null){
			return;
		}
		
		for(String iField : fieldToErrorMap.keySet()){
			
			String errorMessage = fieldToErrorMap.get(iField);
			
			errors.rejectValue(iField, errorMessage);			
		}				
					
	}
	
	
	public Map<String, String> getValidationErrorsList(Subscription subscription){
		
		if(subscription == null){
			return null;
		}
		
		Map<String, String> fieldToErrorMap = new HashMap<String, String>();		
						
		String topic = subscription.getTopic(); 		
		if(StringUtils.isBlank(topic)){			
			fieldToErrorMap.put("subscriptionType", "Subscription type must be specified");			
		}
				
		String sid = subscription.getStateId();		
		if(StringUtils.isBlank(sid)){			
			fieldToErrorMap.put("stateId", "SID must be specified");
		}
		
		String name = subscription.getFullName();		
		if(StringUtils.isBlank(name)){
			fieldToErrorMap.put("fullName", "Name must be specified");
		}
		
		Date subStartDate = subscription.getSubscriptionStartDate();
		if(subStartDate == null){
			fieldToErrorMap.put("subscriptionStartDate", "Start date must be specified");
		}
		
		validateArrestSubEndDate(subscription, fieldToErrorMap);
	
		boolean hasEmail = false;
		
		for(String iEmail : subscription.getEmailList()){
			if(StringUtils.isNotBlank(iEmail)){
				hasEmail = true;
			}
		}
		
		if(!hasEmail){
			fieldToErrorMap.put("emailList", "Email Address must be specified");
		}			
		
		return fieldToErrorMap;
	}

}


