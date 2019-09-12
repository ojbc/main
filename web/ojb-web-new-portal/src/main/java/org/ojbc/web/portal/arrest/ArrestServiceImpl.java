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
package org.ojbc.web.portal.arrest;

import org.ojbc.processor.arrest.modify.ArrestFinalizeRequestProcessor;
import org.ojbc.processor.arrest.modify.ArrestHideRequestProcessor;
import org.ojbc.processor.arrest.modify.ArrestModifyRequestProcessor;
import org.ojbc.processor.arrest.modify.ArrestUnhideRequestProcessor;
import org.ojbc.processor.arrest.modify.DaArrestReferRequestProcessor;
import org.ojbc.processor.arrest.modify.DeleteDispositionRequestProcessor;
import org.ojbc.processor.arrest.modify.ExpungeDispositionRequestProcessor;
import org.ojbc.processor.arrest.modify.MuniArrestReferRequestProcessor;
import org.ojbc.processor.arrest.search.ArrestDetailSearchRequestProcessor;
import org.ojbc.processor.arrest.search.ArrestSearchRequestProcessor;
import org.ojbc.processor.recordreplication.RecordReplicationRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.w3c.dom.Element;

@Configuration
@Profile("arrest-search")
public class ArrestServiceImpl implements ArrestService {

	@Autowired
	private ArrestSearchRequestProcessor arrestSearchRequestProcessor;
	@Autowired
	private ArrestDetailSearchRequestProcessor arrestDetailSearchRequestProcessor;
	@Autowired
	private ArrestModifyRequestProcessor arrestModifyRequestProcessor;
	@Autowired
	private ArrestFinalizeRequestProcessor arrestFinalizeRequestProcessor;
	@Autowired
	private ArrestHideRequestProcessor arrestHideRequestProcessor;
	@Autowired
	private ArrestUnhideRequestProcessor arrestUnhideRequestProcessor;
	@Autowired
	private DeleteDispositionRequestProcessor deleteDispositionRequestProcessor;
	@Autowired
	private ExpungeDispositionRequestProcessor expungeDispositionRequestProcessor;
	@Autowired
	private DaArrestReferRequestProcessor daArrestReferRequestProcessor;
	@Autowired
	private MuniArrestReferRequestProcessor muniArrestReferRequestProcessor;
	@Autowired
	private RecordReplicationRequestProcessor recordReplicationRequestProcessor;
	
	@Override
	public String findArrests(ArrestSearchRequest arrestSearchRequest, Element samlToken) throws Throwable {
		return arrestSearchRequestProcessor.invokeRequest(arrestSearchRequest, samlToken);
	}

	@Override
	public String getArrest(String id, Element samlToken) throws Throwable {
		return arrestDetailSearchRequestProcessor.invokeRequest(id, samlToken);
	}

	@Override
	public String saveDisposition(Disposition disposition, Element samlToken) throws Throwable {
		return arrestModifyRequestProcessor.invokeRequest(disposition, samlToken);
	}
	@Override
	public String hideArrest(String id, Element samlToken) throws Throwable {
		return arrestHideRequestProcessor.invokeRequest(id, samlToken);
	}

	@Override
	public String deleteDisposition(Disposition disposition, Element samlToken) throws Throwable {
		return deleteDispositionRequestProcessor.invokeRequest(disposition, samlToken);
	}

	@Override
	public String referArrestToDa(String id, Element samlAssertion) throws Throwable {
		return daArrestReferRequestProcessor.invokeRequest(id, samlAssertion);
	}

	@Override
	public String referArrestToMuni(String id, Element samlAssertion) throws Throwable {
		return muniArrestReferRequestProcessor.invokeRequest(id, samlAssertion);
	}

	@Override
	public String expungeDisposition(Disposition disposition, Element samlToken) throws Throwable {
		return expungeDispositionRequestProcessor.invokeRequest(disposition, samlToken);
	}

	@Override
	public String lookupOtn(String otn, Element samlAssertion) throws Throwable {
		return recordReplicationRequestProcessor.invokeRequest(otn, samlAssertion);
	}

	@Override
	public String unhideArrest(String id, Element samlToken) throws Throwable {
		return arrestUnhideRequestProcessor.invokeRequest(id, samlToken);
	}

	@Override
	public String finalizeArrest(String id, Element samlAssertion) throws Throwable {
		return arrestFinalizeRequestProcessor.invokeRequest(id, samlAssertion);
	}

	@Override
	public String declineCharge(ArrestCharge arrestCharge, Element samlAssertion) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}
	
}
