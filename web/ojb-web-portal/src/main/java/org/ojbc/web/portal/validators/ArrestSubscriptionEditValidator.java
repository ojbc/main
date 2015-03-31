package org.ojbc.web.portal.validators;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.ojbc.web.model.subscription.edit.SubscriptionEditRequest;
import org.ojbc.web.portal.controllers.dto.SubscriptionEditCommand;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class ArrestSubscriptionEditValidator {
	
	private Logger logger = Logger.getLogger(ArrestSubscriptionEditValidator.class.getName());
	
	
	public void validate(SubscriptionEditCommand subEditCmd, BindingResult errors){
		
		logger.info("* * * inside validate()");
				
		SubscriptionEditRequest subEditRequest = subEditCmd.getSubscriptionEditRequest();
		
		if(subEditRequest == null){
			return;
		}
				
		Map<String, String> fieldToErrorMap = getValidationErrorsList(subEditRequest);
		
		if(fieldToErrorMap == null){			
			return;
		}
		
		for(String iField : fieldToErrorMap.keySet()){
			
			String errorMessage = fieldToErrorMap.get(iField);
			
			errors.rejectValue(iField, errorMessage);			
		}				
					
	}
	
			
	Map<String, String> getValidationErrorsList(SubscriptionEditRequest subEditRequest){
						
		if(subEditRequest == null){
			return null;
		}
		
		Map<String, String> fieldToErrorMap = new HashMap<String, String>();
				
		String topic = subEditRequest.getSubscriptionType(); 		
		if(StringUtils.isBlank(topic)){			
			fieldToErrorMap.put("subscriptionEditRequest.subscriptionType", "Subscription type must be specified");
		}
				
		String sid = subEditRequest.getStateId();		
		if(StringUtils.isBlank(sid)){
			fieldToErrorMap.put("subscriptionEditRequest.stateId", "SID must be specified");
		}
		
		String name = subEditRequest.getFullName();		
		if(StringUtils.isBlank(name)){
			fieldToErrorMap.put("subscriptionEditRequest.fullName", "Name must be specified");
		}
		
		Date subStartDate = subEditRequest.getSubscriptionStartDate();
		if(subStartDate == null){
			fieldToErrorMap.put("subscriptionEditRequest.subscriptionStartDate", "Start date must be specified");
		}
		
		Date subEndDate = subEditRequest.getSubscriptionEndDate();
		if(subEndDate != null && subStartDate != null){			
			if(subEndDate.before(subStartDate)){
				fieldToErrorMap.put("subscriptionEditRequest.subscriptionEndDate", "End date may not occur before start date");
			}									
		}
				
		boolean hasEmail = false;
		
		for(String iEmail : subEditRequest.getEmailList()){
			if(StringUtils.isNotBlank(iEmail)){
				hasEmail = true;
			}
		}
		
		if(!hasEmail){
			fieldToErrorMap.put("subscriptionEditRequest.emailList", "Email Address must be specified");
		}
		
		return fieldToErrorMap;						
	}
	

}

