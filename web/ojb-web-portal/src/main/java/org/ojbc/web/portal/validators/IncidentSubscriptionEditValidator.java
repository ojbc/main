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
import org.ojbc.web.model.subscription.edit.SubscriptionEditRequest;
import org.ojbc.web.portal.controllers.dto.SubscriptionEditCommand;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class IncidentSubscriptionEditValidator {
	
	private Logger logger = Logger.getLogger(IncidentSubscriptionEditValidator.class.getName());
	
	public void validate(SubscriptionEditCommand subEditCmd,
			BindingResult errors){
				
		logger.info("* * * inside validate()");
		
		SubscriptionEditRequest subEditRequest = subEditCmd.getSubscriptionEditRequest();
				
		String topic = subEditRequest.getSubscriptionType(); 		
		if(StringUtils.isBlank(topic)){
			errors.rejectValue("subscriptionEditRequest.subscriptionType", "Subscription type must be specified");
		}
				
		String fName = subEditRequest.getFirstName();
		if(StringUtils.isBlank(fName)){
			errors.rejectValue("subscriptionEditRequest.firstName", "First name must be specified");
		}
		
		String lName = subEditRequest.getLastName();
		if(StringUtils.isBlank(lName)){
			errors.rejectValue("subscriptionEditRequest.lastName", "Last name must be specified");
		}
		
		Date dob = subEditRequest.getDateOfBirth();
		if(dob == null){
			errors.rejectValue("subscriptionEditRequest.dateOfBirth", "DOB must be specified");
		}
		
		
		Date subStartDate = subEditRequest.getSubscriptionStartDate();
		if(subStartDate == null){
			errors.rejectValue("subscriptionEditRequest.subscriptionStartDate", "Start date must be specified");
		}
		
		Date subEndDate = subEditRequest.getSubscriptionEndDate();
		if(subEndDate != null && subStartDate != null){			
			if(subEndDate.before(subStartDate)){
				errors.rejectValue("subscriptionEditRequest.subscriptionEndDate", "End date may not occur before start date");
			}									
		}
				
		boolean hasEmail = false;
		
		for(String iEmail : subEditRequest.getEmailList()){
			if(StringUtils.isNotBlank(iEmail)){
				hasEmail = true;
			}
		}
		
		if(!hasEmail){
			errors.rejectValue("subscriptionEditRequest.emailList", "Email Address must be specified");
		}		
	}

}



