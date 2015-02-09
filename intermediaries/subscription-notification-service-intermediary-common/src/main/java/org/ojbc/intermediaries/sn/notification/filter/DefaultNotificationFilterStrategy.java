package org.ojbc.intermediaries.sn.notification.filter;


import org.ojbc.intermediaries.sn.notification.NotificationRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This implementation will never filter a message.  It is a default for 
 * members who don't have a notification filter strategy.
 * 
 */
public class DefaultNotificationFilterStrategy implements NotificationFilterStrategy {

	private static final Log log = LogFactory.getLog(DefaultNotificationFilterStrategy.class);
	
	@Override
	public boolean shouldMessageBeFiltered(NotificationRequest request) {
		
		log.info("Entering No-Op Notification Filter Strategy.  Will always return false.");
		
		return false;
	}

	
}
