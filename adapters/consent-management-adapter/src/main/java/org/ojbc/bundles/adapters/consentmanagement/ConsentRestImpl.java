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
package org.ojbc.bundles.adapters.consentmanagement;

import java.time.LocalDateTime;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.bundles.adapters.consentmanagement.dao.ConsentManagementDAOImpl;
import org.ojbc.bundles.adapters.consentmanagement.model.Consent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsentRestImpl implements ConsentInterface {
	
	private final Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private ConsentManagementDAOImpl consentManagementDAOImpl;
	
	@Override
	public Response search() {
		List<Consent> consentRecordsWithNoConsentDecisions = consentManagementDAOImpl.searchForConsentRecords();
		
		return Response.status(Status.OK).entity(consentRecordsWithNoConsentDecisions).build();
	}

	@Override
	public Response consent(Consent consent) throws Exception {

		Integer consentDecisionID = consent.getConsentId();
		
		Integer consentDecisionTypeID = consent.getConsentDecisionTypeID();
		
		String consenterUserID = consent.getConsenterUserID();
		
		String consentDocumentControlNumber = consent.getConsentDocumentControlNumber();
		
		if (consentDecisionID == null || consentDecisionTypeID==null || StringUtils.isBlank(consenterUserID) || StringUtils.isBlank(consentDocumentControlNumber))
		{
			throw new Exception("Required fields were not provided");
		}	
		
		LocalDateTime consentDecisionTimestamp = consent.getConsentDecisionTimestamp();
		
		if (consentDecisionTimestamp == null)
		{
			consentDecisionTimestamp = LocalDateTime.now();
		}	
		
		consentManagementDAOImpl.updateConsentDecision(consentDecisionID, consentDecisionTypeID, consenterUserID, consentDocumentControlNumber, consentDecisionTimestamp);
		
		return Response.status(Status.OK).build();
	}
	

}