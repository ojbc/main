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
package org.ojbc.processor.firearm.query;

import static org.junit.Assert.assertTrue;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.w3c.dom.Document;

public class FirearmQueryRequestTest {

	@Before
	public void setUp() throws Exception {
		//Tell XML Unit to ignore whitespace between elements and within elements
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setNormalizeWhitespace(true);
	}
	
	@Test
	public void testRequestForPerson(){
		
		DetailsRequest personQueryRequest = new DetailsRequest();
		
		personQueryRequest.setIdentificationSourceText(
				"{http://ojbc.org/Services/WSDL/FirearmRegistrationQueryRequestService/1.0}SubmitFirearmRegistrationQueryRequestByPerson");
		
		String sQueryRequest = RequestMessageBuilderUtilities.createFirearmQueryRequest(personQueryRequest);
				
		boolean containsRootNode = sQueryRequest.contains("PersonFirearmRegistrationRequest");
		
		assertTrue(containsRootNode);
		
		boolean containsIdNode = sQueryRequest.contains("PersonFirearmRegistrationIdentification");
		
		assertTrue(containsIdNode);
	}
	
	@Test
	public void testRequestForFirearm(){
		
		DetailsRequest firearmQueryRequest = new DetailsRequest();
		
		firearmQueryRequest.setIdentificationSourceText(
				"{http://ojbc.org/Services/WSDL/FirearmRegistrationQueryRequestService/1.0}SubmitFirearmRegistrationQueryRequestByFirearm");
		
		String sQueryRequest = RequestMessageBuilderUtilities.createFirearmQueryRequest(firearmQueryRequest);
		
		boolean containsRootNode = sQueryRequest.contains("FirearmRegistrationRequest");
		
		assertTrue(containsRootNode);
		
		boolean containsIdNode = sQueryRequest.contains("FirearmRegistrationIdentification");
		
		assertTrue(containsIdNode);
	}

	@Test
	public void testRequestForFirearmMaui() throws Exception{
		
		DetailsRequest firearmQueryRequest = new DetailsRequest();
		
		firearmQueryRequest.setIdentificationSourceText(
				"{http://ojbc.org/Services/WSDL/FirearmRegistrationQueryRequestService/1.0}SubmitFirearmRegistrationQueryRequestByFirearm-Maui");
		
		String sQueryRequest = RequestMessageBuilderUtilities.createFirearmQueryRequest(firearmQueryRequest);
		
		boolean containsRootNode = sQueryRequest.contains("FirearmRegistrationRequest");
		
		assertTrue(containsRootNode);
		
		boolean containsIdNode = sQueryRequest.contains("FirearmRegistrationIdentification");
		
		assertTrue(containsIdNode);

		Document resultingDoc = OJBUtils.loadXMLFromString(sQueryRequest);
		XmlUtils.validateInstance("ssp/Firearm_Registration_Query_Request/artifacts/service_model/information_model/IEPD/xsd", "Subset/niem", "exchange_schema.xsd",resultingDoc);
		
	}

	@Test
	public void testRequestForPersonMaui(){
		
		DetailsRequest personQueryRequest = new DetailsRequest();
		
		personQueryRequest.setIdentificationSourceText(
				"{http://ojbc.org/Services/WSDL/FirearmRegistrationQueryRequestService/1.0}SubmitFirearmRegistrationQueryRequestByPerson-Maui");
		
		String sQueryRequest = RequestMessageBuilderUtilities.createFirearmQueryRequest(personQueryRequest);
				
		boolean containsRootNode = sQueryRequest.contains("PersonFirearmRegistrationRequest");
		
		assertTrue(containsRootNode);
		
		boolean containsIdNode = sQueryRequest.contains("PersonFirearmRegistrationIdentification");
		
		assertTrue(containsIdNode);
	}
	
}


