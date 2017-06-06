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
import org.joda.time.DateTime;
import org.joda.time.Days;

public class EventDateBasedNotificationFilterStrategy implements NotificationFilterStrategy{

	private static final Log log = LogFactory.getLog( EventDateBasedNotificationFilterStrategy.class );
	
	private Integer filterNotificationsWithEventDatesOlderThanThisManyDays;
	
	@Override
	public boolean shouldMessageBeFiltered(NotificationRequest notificationRequest) {
		
		try
		{
			DateTime currentDate = new DateTime();
	
			
			DateTime notificationDate = notificationRequest.getNotificationEventDate();
	
			if (notificationDate == null)
			{
				log.info("unable to determine notification date.  Will not filter");
				return false;
			}	
			
			if (Days.daysBetween(notificationDate, currentDate).getDays() > filterNotificationsWithEventDatesOlderThanThisManyDays )
			{
				log.info("Notification date of " + notificationDate.toString() + " is older than " + filterNotificationsWithEventDatesOlderThanThisManyDays + ". Notification IS filtered.");
				return true;
			}
		} 
		catch (Exception ex)
		{
			log.error("EventDateBasedNotificationFilterStrategy threw an error.  Will not filter message");
		}
		
		return false;
	}

	public Integer getFilterNotificationsWithEventDatesOlderThanThisManyDays() {
		return filterNotificationsWithEventDatesOlderThanThisManyDays;
	}

	public void setFilterNotificationsWithEventDatesOlderThanThisManyDays(
			Integer filterNotificationsWithEventDatesOlderThanThisManyDays) {
		this.filterNotificationsWithEventDatesOlderThanThisManyDays = filterNotificationsWithEventDatesOlderThanThisManyDays;
	}


}
