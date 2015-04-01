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
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.ojbc.web.model.subscription.add.SubscriptionAddRequest;
import org.ojbc.web.portal.controllers.dto.SubscriptionAddCommand;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class IncidentSubscriptionAddValidator {
	
	private Logger logger = Logger.getLogger(IncidentSubscriptionAddValidator.class.getName());
	
	public void validate(SubscriptionAddCommand subAddCmd,
			BindingResult errors){
				
		logger.info("* * * inside validate()");
		
		SubscriptionAddRequest subAddReq = subAddCmd.getSubscriptionAddRequest();
				
		String topic = subAddReq.getSubscriptionType(); 		
		if(StringUtils.isBlank(topic)){
			errors.rejectValue("subscriptionAddRequest.subscriptionType", "Subscription type must be specified");
		}
				
		String fName = subAddReq.getFirstName();
		if(StringUtils.isBlank(fName)){
			errors.rejectValue("subscriptionAddRequest.firstName", "First name must be specified");
		}
		
		String lName = subAddReq.getLastName();
		if(StringUtils.isBlank(lName)){
			errors.rejectValue("subscriptionAddRequest.lastName", "Last name must be specified");
		}
		
		Date dob = subAddReq.getDateOfBirth();
		if(dob == null){
			errors.rejectValue("subscriptionAddRequest.dateOfBirth", "DOB must be specified");
		}
		
		
		Date subStartDate = subAddReq.getSubscriptionStartDate();
		if(subStartDate == null){
			errors.rejectValue("subscriptionAddRequest.subscriptionStartDate", "Start date must be specified");
		}
		
		Date subEndDate = subAddReq.getSubscriptionEndDate();
		if(subEndDate != null && subStartDate != null){			
			if(subEndDate.before(subStartDate)){
				errors.rejectValue("subscriptionAddRequest.subscriptionEndDate", "End date may not occur before start date");
			}									
		}
				
		boolean hasEmail = false;
		
		for(String iEmail : subAddReq.getEmailList()){
			if(StringUtils.isNotBlank(iEmail)){
				hasEmail = true;
			}
		}
		
		if(!hasEmail){
			errors.rejectValue("subscriptionAddRequest.emailList", "Email Address must be specified");
		}		
	}

}



