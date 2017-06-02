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
package org.ojbc.web.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.ojbc.processor.person.query.JuvenileQueryRequestProcessor;
import org.ojbc.web.OJBCWebServiceURIs;
import org.ojbc.web.model.person.query.DetailsRequest;

/**
 * This call tests the Detail Query Dispatcher.  It requires that query type is 
 * specified for Juvenile Query calls. 
 * 
 *
 */
public class TestDetailQueryDispatcher {

	@InjectMocks
	private DetailQueryDispatcher unit = new DetailQueryDispatcher();
	
	@Spy
	private JuvenileQueryRequestProcessor juvenileCasePlanHistoryRequestProcessor;

	@Spy
	private JuvenileQueryRequestProcessor juvenileOffenseHistoryRequestProcessor;

	@Spy
	private JuvenileQueryRequestProcessor juvenilePlacementHistoryRequestProcessor;

	@Spy
	private JuvenileQueryRequestProcessor juvenileReferralHistoryRequestProcessor;

	@Spy
	private JuvenileQueryRequestProcessor juvenileHearingHistoryRequestProcessor;
	
	@Spy
	private JuvenileQueryRequestProcessor juvenileIntakeHistoryRequestProcessor;
	
	@Before
	public void setUp() throws Exception {
		Assert.assertNotNull(unit);
		
		MockitoAnnotations.initMocks(this);
	}

	@Test(expected=RuntimeException.class)
	public void testDetailQueryDispatcherJuvenileQueryNoQueryType() throws Exception
	{
		//No Query type specified, throw exception
		
		DetailsRequest detailRequest = new DetailsRequest();
		
		detailRequest.setIdentificationID("12345");
		detailRequest.setIdentificationSourceText(OJBCWebServiceURIs.JUVENILE_HISTORY);
		
		unit.invokeRequest(detailRequest, "123456", null);
	}

	@Test
	public void testDetailQueryDispatcherJuvenileQueryWithQueryType() throws Exception
	{
		//Create Detail Request with Case Plan Query Type
		DetailsRequest detailRequest = new DetailsRequest();
		
		detailRequest.setIdentificationID("12345");
		detailRequest.setIdentificationSourceText(OJBCWebServiceURIs.JUVENILE_HISTORY);
		detailRequest.setQueryType("CasePlan");
		
		//Mock objects so we don't actually call the web service beans
		Mockito.doReturn("Case Plan Called").when(juvenileCasePlanHistoryRequestProcessor).invokeRequest(detailRequest, "123456", null);
		Mockito.doReturn("Hearing Called").when(juvenileHearingHistoryRequestProcessor).invokeRequest(detailRequest, "123456", null);
		Mockito.doReturn("Intake Called").when(juvenileIntakeHistoryRequestProcessor).invokeRequest(detailRequest, "123456", null);
		Mockito.doReturn("Offense Called").when(juvenileOffenseHistoryRequestProcessor).invokeRequest(detailRequest, "123456", null);
		Mockito.doReturn("Placement Called").when(juvenilePlacementHistoryRequestProcessor).invokeRequest(detailRequest, "123456", null);
		Mockito.doReturn("Referral Called").when(juvenileReferralHistoryRequestProcessor).invokeRequest(detailRequest, "123456", null);
		
		//Assert proper beans are called for query types
		Assert.assertEquals("Case Plan Called",unit.invokeRequest(detailRequest, "123456", null));		
		
		detailRequest.setQueryType("Hearing");
		Assert.assertEquals("Hearing Called",unit.invokeRequest(detailRequest, "123456", null));

		detailRequest.setQueryType("Intake");
		Assert.assertEquals("Intake Called",unit.invokeRequest(detailRequest, "123456", null));

		detailRequest.setQueryType("Offense");
		Assert.assertEquals("Offense Called",unit.invokeRequest(detailRequest, "123456", null));
		
		detailRequest.setQueryType("Placement");
		Assert.assertEquals("Placement Called",unit.invokeRequest(detailRequest, "123456", null));
		
		detailRequest.setQueryType("Referral");
		Assert.assertEquals("Referral Called",unit.invokeRequest(detailRequest, "123456", null));
		
		//Verify each bean was called only once
		Mockito.verify(juvenileCasePlanHistoryRequestProcessor, Mockito.times(1)).invokeRequest(detailRequest, "123456", null);
		Mockito.verify(juvenileHearingHistoryRequestProcessor, Mockito.times(1)).invokeRequest(detailRequest, "123456", null);
		Mockito.verify(juvenileIntakeHistoryRequestProcessor, Mockito.times(1)).invokeRequest(detailRequest, "123456", null);
		Mockito.verify(juvenileOffenseHistoryRequestProcessor, Mockito.times(1)).invokeRequest(detailRequest, "123456", null);
		Mockito.verify(juvenilePlacementHistoryRequestProcessor, Mockito.times(1)).invokeRequest(detailRequest, "123456", null);
		Mockito.verify(juvenileReferralHistoryRequestProcessor, Mockito.times(1)).invokeRequest(detailRequest, "123456", null);

	}
}
