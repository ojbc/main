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


