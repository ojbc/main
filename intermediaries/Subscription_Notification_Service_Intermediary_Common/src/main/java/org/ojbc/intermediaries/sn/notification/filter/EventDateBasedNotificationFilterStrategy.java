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
