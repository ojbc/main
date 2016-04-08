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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.adapters.analyticsstaging.custody.processor;

import java.time.LocalDateTime;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Component
public class CustodyReleaseReportProcessor extends AbstractReportRepositoryProcessor {

	private static final Log log = LogFactory.getLog( CustodyReleaseReportProcessor.class );
	
	@Transactional
	public void processReport(Document report, String personUniqueIdentifier) throws Exception
	{
		log.info("Processing Custody Release report." );
		
		Node bookingNode = XmlUtils.xPathNodeSearch(report, "/crr-exc:CustodyReleaseReport/crr-ext:Custody/jxdm51:Booking");
		String bookingNumber = XmlUtils.xPathStringSearch(bookingNode, "jxdm51:BookingSubject/jxdm51:SubjectIdentification/nc30:IdentificationID");
		
		if (StringUtils.isBlank(bookingNumber)){
			throw new IllegalArgumentException("The booking number is empty in the request."); 
		}
		
		LocalDateTime releaseDate = null; 
		String releaseDateString = XmlUtils.xPathStringSearch(bookingNode, "jxdm51:BookingRelease/nc30:ActivityDate/nc30:DateTime");
		if (StringUtils.isNotBlank(releaseDateString)){
			releaseDate = LocalDateTime.parse(releaseDateString);
		}
		
		analyticalDatastoreDAO.updateCustodyReleaseDate(bookingNumber, releaseDate);
		
		log.info("Processed Custody Release report successfully.");
		
	}

}
