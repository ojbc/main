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
package org.ojbc.adapters.analyticsstaging.corrections.processor;

import org.apache.camel.Body;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticsstaging.corrections.dao.AnalyticalDatastoreDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;

public abstract class AbstractReportRepositoryProcessor {

	private static final Log log = LogFactory.getLog( AbstractReportRepositoryProcessor.class );

	protected static final String FATAL_ERROR_PERSON_UNIQUE_ID_MISSING = "Fatal error: The person unique identifier is missing";
	protected static final String BOOKING_NUMBER_IS_MISSING_IN_THE_REPORT = "Booking Number is missing in the report";

	@Autowired
	protected AnalyticalDatastoreDAO analyticalDatastoreDAO;
	
    @Transactional
	public abstract void processReport(@Body Document report) throws Exception;

}
