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