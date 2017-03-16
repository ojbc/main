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
package org.ojbc.web.portal.validators.subscriptions;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.helper.OJBCDateUtils;
import org.ojbc.util.xml.subscription.Subscription;
import org.ojbc.web.model.subscription.add.SubscriptionEndDateStrategy;
import org.ojbc.web.portal.controllers.SubscriptionsController;
import org.ojbc.web.portal.validators.ChCycleSubscriptionValidator;
import org.ojbc.web.portal.validators.IncidentSubscriptionAddValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service
public class SubscriptionValidator implements Validator{
	
	private final Log log = LogFactory.getLog(this.getClass());	
	
	@Value("${showSubscriptionPurposeDropDown:false}")
	protected Boolean showSubscriptionPurposeDropDown;
	
	@Value("${showCaseIdInput:false}")
	protected Boolean showCaseIdInput;
	
	@Value("${fbiIdWarning:false}")
	protected Boolean fbiIdWarning;
	
	@Value("#{getObject('arrestSubscriptionAddValidator')}")
	ArrestSubscriptionValidatorInterface arrestSubscriptionAddValidator;
	
	@Resource
	IncidentSubscriptionAddValidator incidentSubscriptionAddValidator;
	
	@Resource
	ChCycleSubscriptionValidator chCycleSubscriptionValidator;
	
	@Value("#{getObject('subscriptionEndDateStrategyMap')}")
	Map<String, SubscriptionEndDateStrategy> subscriptionEndDateStrategyMap;

	@Override
	public boolean supports(Class<?> clazz) {
		return Subscription.class.equals(clazz);
	}


	@Override
	public void validate(Object target, Errors errors) {
		Subscription subscription = (Subscription) target; 
		log.info("Validating subscription " + subscription);

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "topic", "Subscription type must be specified");
		
		switch (subscription.getTopic()){
		case SubscriptionsController.ARREST_TOPIC_SUB_TYPE:
			arrestSubscriptionAddValidator.validate(subscription, errors);
			break;
		case SubscriptionsController.RAPBACK_TOPIC_SUB_TYPE:
			validateRapbackSubscription(subscription, errors);
			break; 
		case SubscriptionsController.INCIDENT_TOPIC_SUB_TYPE:
			incidentSubscriptionAddValidator.validate(subscription, errors);
			break; 
		case SubscriptionsController.CHCYCLE_TOPIC_SUB_TYPE: 
			chCycleSubscriptionValidator.validate(subscription, errors);
			break; 
			
		}

	}

	private void validateRapbackSubscription(Subscription subscription, Errors errors){
	
		if (showSubscriptionPurposeDropDown){
			ValidationUtils.rejectIfEmpty(errors, "subscriptionPurpose", "Purpose must be specified");
		}
		
		if ("CS".equals(subscription.getSubscriptionPurpose()) && !subscription.getFederalRapSheetDisclosureIndicator()){
			errors.rejectValue("federalRapSheetDisclosureIndicator", 
					"Disclosure Indicator must be selected");
		}

		if (subscription.getFederalRapSheetDisclosureIndicator()){
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "federalRapSheetDisclosureAttentionDesignationText", 
					"Disclosure contact info must be specified");
		}
		
		ValidationUtils.rejectIfEmpty(errors, "stateId", "SID must be specified");
		ValidationUtils.rejectIfEmpty(errors, "fullName", "Name must be specified");
		ValidationUtils.rejectIfEmpty(errors, "dateOfBirth", "Date of Birth must be specified");
		
		ValidationUtils.rejectIfEmpty(errors, "subscriptionStartDate", "Start date must be specified");
		ValidationUtils.rejectIfEmpty(errors, "subscriptionEndDate", "End date must be specified");
		
		validateRapbackSubscriptionEndDate(subscription, errors);
		
		boolean hasNoEmail = subscription.getEmailList().stream()
				.filter(StringUtils::isNotBlank)
				.count() == 0;
		if (hasNoEmail){
			errors.rejectValue("emailList", null, "Email Address must be specified");
		}
		
		if (showCaseIdInput){
			ValidationUtils.rejectIfEmpty(errors, "caseId", "Case Id must be specified");
		}
		
	}

	public void validateRapbackSubscriptionEndDate(Subscription subscription,
			Errors errors) {
		
		Date subEndDate = subscription.getSubscriptionEndDate();
		Date subStartDate = subscription.getSubscriptionStartDate();
		
		if(subEndDate != null && subStartDate != null){			
			if(subEndDate.before(subStartDate)){
				errors.reject("End date may not occur before start date");
			}
			else {
				SubscriptionEndDateStrategy endDateStrategy = null;
				switch(StringUtils.trimToEmpty(subscription.getSubscriptionPurpose())){
				case "CS": 
					endDateStrategy = subscriptionEndDateStrategyMap.get(SubscriptionsController.RAPBACK_TOPIC_SUB_TYPE_CS);
					break; 
				case "CI": 
					endDateStrategy = subscriptionEndDateStrategyMap.get(SubscriptionsController.RAPBACK_TOPIC_SUB_TYPE_CI);
					break;
				default: 
					endDateStrategy = subscriptionEndDateStrategyMap.get(SubscriptionsController.ARREST_TOPIC_SUB_TYPE);
				}
				
				Date defaultEndDate = OJBCDateUtils.getEndDate(subStartDate,
						endDateStrategy.getPeriod());

				if(defaultEndDate != null && subEndDate.after(endDateStrategy.getDefaultValue())){
					errors.rejectValue("subscriptionEndDate", "End date may not be more than " + endDateStrategy.getPeriod() + " year after the start date");
				}
			}
		}
	}

}


