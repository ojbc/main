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

import java.util.List;

import org.ojbc.web.portal.util.WebUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.w3c.dom.Element;

@Configuration
@Profile("standalone")
public class ArrestServiceStandalone implements ArrestService {

	@Override
	public String findArrests(ArrestSearchRequest arrestSearchRequest, Element SamlToken) throws Throwable {
		return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
				"/xmlInstances/Initial_Results_muni.xml"));
	}

	@Override
	public String getArrest(ArrestDetailSearchRequest arrestDetailSearchRequest, Element samlToken) throws Throwable {
		return WebUtils.returnStringFromFilePath(getClass().getResourceAsStream(
				"/xmlInstances/Municipal_DeferredDisposition_search_results_multiple.xml"));
	}

	@Override
	public String hideArrest(String id, List<String> chargeIds, Element samlToken) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteDisposition(Disposition disposition, Element samlAssertion) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String referArrestToDa(String id, Element samlAssertion) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String referArrestToMuni(String id, Element samlAssertion) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String saveDisposition(Disposition disposition, Element samlToken) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String expungeDisposition(Disposition disposition, Element samlToken) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String lookupOtn(String otn, Element samlAssertion) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String unhideArrest(String id, List<String> chargeIds, Element samlToken) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String finalizeArrest(String id, String[] chargeIds, Element samlAssertion) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String declineCharge(ArrestCharge arrestCharge, Element samlAssertion) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String referCharge(ChargeReferral chargeReferral, Element samlAssertion) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String referArrest(ArrestReferral arrestReferral, Element samlAssertion) throws Throwable {
		// TODO Auto-generated method stub
		return null;
	}

}
