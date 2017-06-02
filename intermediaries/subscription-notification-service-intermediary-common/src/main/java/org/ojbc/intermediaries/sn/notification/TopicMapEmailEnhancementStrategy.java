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
package org.ojbc.intermediaries.sn.notification;

import java.util.HashMap;
import java.util.Map;

/**
 * A composite strategy that selects the specific strategy to use based on topic.
 *
 */
public class TopicMapEmailEnhancementStrategy implements EmailEnhancementStrategy {
	
	private EmailEnhancementStrategy defaultStrategy = new DefaultEmailEnhancementStrategy();
	
	private Map<String, EmailEnhancementStrategy> strategyTopicMap = new HashMap<String, EmailEnhancementStrategy>();

	public EmailEnhancementStrategy getDefaultStrategy() {
		return defaultStrategy;
	}

	public void setDefaultStrategy(EmailEnhancementStrategy defaultStrategy) {
		this.defaultStrategy = defaultStrategy;
	}

	public Map<String, EmailEnhancementStrategy> getStrategyTopicMap() {
		return strategyTopicMap;
	}

	public void setStrategyTopicMap(Map<String, EmailEnhancementStrategy> strategyTopicMap) {
		this.strategyTopicMap = strategyTopicMap;
	}

	@Override
	public EmailNotification enhanceEmail(EmailNotification emailNotification) {
		EmailEnhancementStrategy strategy = strategyTopicMap.get(emailNotification.getNotificationRequest().getTopic());
		if (strategy == null) {
			strategy = defaultStrategy;
		}
		return strategy.enhanceEmail(emailNotification);
	}

}