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


import java.util.List;

import org.ojbc.intermediaries.sn.notification.NotificationRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class OriBasedNotificationFilterStrategy implements NotificationFilterStrategy{

	private static final Log log = LogFactory.getLog( OriBasedNotificationFilterStrategy.class );
	
	private List<String> filteredOris;
	
	@Override
	public boolean shouldMessageBeFiltered(NotificationRequest notificationRequest) {
		
		try
		{
			
			String notifyingAgencyOri = notificationRequest.getNotifyingAgencyOri();
	
			if (StringUtils.isBlank(notifyingAgencyOri))
			{
				log.info("unable to determine notifying Agency ORI.  Will not filter");
				return false;
			}	
			
			if (filteredOris!= null && filteredOris.contains(notifyingAgencyOri))
			{
				log.info("Notifying agency ORI " + notifyingAgencyOri + " is among the oris to be filtered " + filteredOris.toString() + ". Notification IS filtered.");
				return true;
			}
		} 
		catch (Exception ex)
		{
			log.error("OriBasedNotificationFilterStrategy threw an error.  Will not filter message");
		}
		
		return false;
	}

	public List<String> getFilteredOris() {
		return filteredOris;
	}

	public void setFilteredOris(List<String> filteredOris) {
		this.filteredOris = filteredOris;
	}


}
