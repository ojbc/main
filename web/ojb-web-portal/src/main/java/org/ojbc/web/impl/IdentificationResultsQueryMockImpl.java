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
package org.ojbc.web.impl;

import java.util.Arrays;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.IdentificationResultsQueryInterface;
import org.ojbc.web.model.identificationresult.search.IdentificationResultsQueryResponse;
import org.ojbc.web.portal.services.SearchResultConverter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
@Profile({"initial-results-query","standalone"})
public class IdentificationResultsQueryMockImpl implements IdentificationResultsQueryInterface{
		
	@SuppressWarnings("unused")
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Resource
	SearchResultConverter searchResultConverter;		
	
	@Override
	public IdentificationResultsQueryResponse invokeIdentificationResultsQueryRequest(
			String transactionNumber,  boolean initialResultsQuery, Element samlToken) throws Exception {
		
		IdentificationResultsQueryResponse identificationResultsQueryResponse = 
				new IdentificationResultsQueryResponse();

		if (initialResultsQuery){
			identificationResultsQueryResponse.setFbiSearchResultFile("Match");
			identificationResultsQueryResponse.setStateSearchResultFile("Match");
			identificationResultsQueryResponse.setStateCriminalHistoryRecordDocuments(Arrays.asList("State Rap Sheet"));
			identificationResultsQueryResponse.setFbiIdentityHistorySummaryDocuments(Arrays.asList("FBI Identity History Summary"));
		}
		else {
			identificationResultsQueryResponse.setStateCriminalHistoryRecordDocuments(Arrays.asList("State Subsequent Results"));
			identificationResultsQueryResponse.setFbiIdentityHistorySummaryDocuments(Arrays.asList("FBI Subsequent Results"));
		}
		return identificationResultsQueryResponse;
	}

	
}
