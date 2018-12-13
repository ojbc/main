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
package org.ojbc.util.camel.filter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TimeElapsedFileFilter<T> implements GenericFileFilter<T> {
	private static final Log log = LogFactory
			.getLog(TimeElapsedFileFilter.class);

	private Integer minutesToDelayFilePickup = Integer.valueOf(0);

	public TimeElapsedFileFilter() {
	}

	public boolean accept(GenericFile<T> file) {
		long lModified = file.getLastModified();

		log.debug("Last Modified timestamp: " + lModified);
		
		log.debug("minutesToDelayFilePickup: " + minutesToDelayFilePickup);

		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDateTime fileCreationDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(lModified), ZoneId.systemDefault());

		log.debug("Current Date: " + currentDateTime.toString());
		log.debug("File Creation Date: " + fileCreationDateTime.toString());

		long minutesBetweenNowAndLastModified = ChronoUnit.MINUTES.between(fileCreationDateTime, currentDateTime);
		
		log.debug("Time between now and last modifed file stamp: "
				+ minutesBetweenNowAndLastModified);
		
		if (minutesToDelayFilePickup == 0)
		{
			log.debug("Filter set to zero, return true.");
			return true;
		}	

		if (minutesBetweenNowAndLastModified > minutesToDelayFilePickup.longValue()) {
			return true;
		}

		return false;
	}

	public Integer getMinutesToDelayFilePickup() {
		return minutesToDelayFilePickup;
	}

	public void setMinutesToDelayFilePickup(Integer minutesToDelayFilePickup) {
		this.minutesToDelayFilePickup = minutesToDelayFilePickup;
	}
}