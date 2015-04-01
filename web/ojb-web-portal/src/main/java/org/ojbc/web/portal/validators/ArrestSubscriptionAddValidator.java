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
package org.ojbc.web.portal.validators;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.ojbc.web.model.subscription.add.SubscriptionAddRequest;
import org.ojbc.web.portal.controllers.dto.SubscriptionAddCommand;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class ArrestSubscriptionAddValidator {
	
	private Logger logger = Logger.getLogger(ArrestSubscriptionAddValidator.class.getName());
		
	public void validate(SubscriptionAddCommand subAddCmd, BindingResult errors){
						
		logger.info("* * * inside validate()");
		
		SubscriptionAddRequest subAddReq = subAddCmd.getSubscriptionAddRequest();
		
		if(subAddReq == null){
			return;
		}
				
		Map<String, String> fieldToErrorMap = getValidationErrorsList(subAddReq);
		
		if(fieldToErrorMap ==  null){
			return;
		}
		
		for(String iField : fieldToErrorMap.keySet()){
			
			String errorMessage = fieldToErrorMap.get(iField);
			
			errors.rejectValue(iField, errorMessage);			
		}				
					
	}
	
	
	Map<String, String> getValidationErrorsList(SubscriptionAddRequest subAddReq){
		
		if(subAddReq == null){
			return null;
		}
		
		Map<String, String> fieldToErrorMap = new HashMap<String, String>();		
						
		String topic = subAddReq.getSubscriptionType(); 		
		if(StringUtils.isBlank(topic)){			
			fieldToErrorMap.put("subscriptionAddRequest.subscriptionType", "Subscription type must be specified");			
		}
				
		String sid = subAddReq.getStateId();		
		if(StringUtils.isBlank(sid)){			
			fieldToErrorMap.put("subscriptionAddRequest.stateId", "SID must be specified");
		}
		
		String name = subAddReq.getFullName();		
		if(StringUtils.isBlank(name)){
			fieldToErrorMap.put("subscriptionAddRequest.fullName", "Name must be specified");
		}
		
		Date subStartDate = subAddReq.getSubscriptionStartDate();
		if(subStartDate == null){
			fieldToErrorMap.put("subscriptionAddRequest.subscriptionStartDate", "Start date must be specified");
		}
		
		Date subEndDate = subAddReq.getSubscriptionEndDate();
		if(subEndDate != null && subStartDate != null){			
			if(subEndDate.before(subStartDate)){
				fieldToErrorMap.put("subscriptionAddRequest.subscriptionEndDate", "End date may not occur before start date");
			}									
		}
				
		boolean hasEmail = false;
		
		for(String iEmail : subAddReq.getEmailList()){
			if(StringUtils.isNotBlank(iEmail)){
				hasEmail = true;
			}
		}
		
		if(!hasEmail){
			fieldToErrorMap.put("subscriptionAddRequest.emailList", "Email Address must be specified");
		}			
		
		return fieldToErrorMap;
	}

}


