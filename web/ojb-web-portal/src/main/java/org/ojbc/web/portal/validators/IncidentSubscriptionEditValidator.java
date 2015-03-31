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



