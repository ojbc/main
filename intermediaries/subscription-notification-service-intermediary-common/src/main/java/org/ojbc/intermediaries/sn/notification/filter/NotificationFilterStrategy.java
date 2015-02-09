package org.ojbc.intermediaries.sn.notification.filter;

import org.ojbc.intermediaries.sn.notification.NotificationRequest;

/**
 * This interface for various ways of filtering notification requests.
 * 
 */
public interface NotificationFilterStrategy {

    /**
     * Determine whether this request should be filtered out, and therefore not processed for notifications.
     * @param request the notification request
     * @return true if the request should be filtered
     */
	public boolean shouldMessageBeFiltered(NotificationRequest request);
	
}
