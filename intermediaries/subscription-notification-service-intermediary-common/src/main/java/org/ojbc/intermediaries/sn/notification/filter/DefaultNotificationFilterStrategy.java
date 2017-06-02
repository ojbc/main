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
