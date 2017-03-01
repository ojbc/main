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
import org.ojbc.util.xml.subscription.Subscription;
import org.ojbc.web.model.subscription.add.SubscriptionEndDateStrategy;
import org.ojbc.web.portal.controllers.SubscriptionsController;
import org.springframework.beans.factory.annotation.Value;

public class AbstractArrestSubscriptionValidator{
	
	@Value("${showSubscriptionPurposeDropDown:false}")
	protected Boolean showSubscriptionPurposeDropDown;
	
	@Value("${showCaseIdInput:false}")
	protected Boolean showCaseIdInput;
	
	@Value("${fbiIdWarning:false}")
	protected Boolean fbiIdWarning;
	
	@Resource
	Map<String, SubscriptionEndDateStrategy> subscriptionEndDateStrategyMap;

	public void validateArrestSubEndDate(Subscription subscription,
			Map<String, String> fieldToErrorMap, Date subEndDate) {
		SubscriptionEndDateStrategy endDateStrategy = null;
		switch(StringUtils.trimToEmpty(subscription.getSubscriptionPurpose())){
		case "CS": 
			endDateStrategy = subscriptionEndDateStrategyMap.get(SubscriptionsController.ARREST_TOPIC_SUB_TYPE_CS);
			break; 
		default: 
			endDateStrategy = subscriptionEndDateStrategyMap.get(SubscriptionsController.ARREST_TOPIC_SUB_TYPE_CI);
		}
		
		if(endDateStrategy.getDefaultValue() != null && subEndDate.after(endDateStrategy.getDefaultValue())){
			fieldToErrorMap.put("subscriptionEndDate", "End date may not be more than " + endDateStrategy.getPeriod() + " year after the start date");
		}
	}

}


