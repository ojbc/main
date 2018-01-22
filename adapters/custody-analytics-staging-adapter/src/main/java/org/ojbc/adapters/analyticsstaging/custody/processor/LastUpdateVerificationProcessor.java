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
package org.ojbc.adapters.analyticsstaging.custody.processor;

import java.time.LocalDateTime;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticsstaging.custody.dao.AnalyticalDatastoreDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LastUpdateVerificationProcessor {

	@Autowired
	protected AnalyticalDatastoreDAO analyticalDatastoreDAO;

	private static final Log log = LogFactory.getLog( LastUpdateVerificationProcessor.class );
	
	public void camelHasThereBeenABookingInLast24hours(Exchange ex)
	{
		if (hasThereBeenABookingInLast24hours())
		{
			ex.getIn().setHeader("bookingError", "false");
		}	
		else
		{
			ex.getIn().setHeader("bookingError", "true");
		}	
	}
	
	public void camelHasThereBeenACustodyReleaseInLast24hours(Exchange ex)
	{
		if (hasThereBeenACustodyReleaseInLast24hours())
		{
			ex.getIn().setHeader("custodyReleaseError", "false");
		}	
		else
		{
			ex.getIn().setHeader("custodyReleaseError", "true");
		}	
	}
	
	public boolean hasThereBeenABookingInLast24hours()
	{
		boolean activity = false;
	
		LocalDateTime lastBookingDate = analyticalDatastoreDAO.getLastBookingDate();

		if (lastBookingDate == null)
		{
			return false;
		}	
		
		log.info("Last booking date: " + lastBookingDate);
		
		LocalDateTime now = LocalDateTime.now();
		
		if (now.minusDays(1).isBefore(lastBookingDate))
		{
			activity = true;
			log.info("Booking activity has been confirmed in the last 24 hours.");
		}	
		
		return activity;
	}
	
	public boolean hasThereBeenACustodyReleaseInLast24hours()
	{
		boolean activity = false;
		
		LocalDateTime lastCustodyReleaseDate = analyticalDatastoreDAO.getLastCustodyReleaseDate();

		if (lastCustodyReleaseDate == null)
		{
			return false;
		}	
		
		log.info("Last Custody Release date: " + lastCustodyReleaseDate);
		
		LocalDateTime now = LocalDateTime.now();
		
		if (now.minusDays(1).isBefore(lastCustodyReleaseDate))
		{
			activity = true;
			log.info("Custody Release activity has been confirmed in the last 24 hours.");
		}	
		
		return activity;
	}

}
