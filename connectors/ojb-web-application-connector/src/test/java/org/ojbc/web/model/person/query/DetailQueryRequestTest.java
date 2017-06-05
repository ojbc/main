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
package org.ojbc.web.model.person.query;


import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.w3c.dom.Document;

public class DetailQueryRequestTest {

	private static final Log log = LogFactory.getLog( DetailQueryRequestTest.class );
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCriminalHistory() throws Exception
	{
		DetailsRequest chr = new DetailsRequest();
		
		chr.setIdentificationID("12345");
		chr.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/Person_Query_Service-Criminal_History/1.0}Person-Query-Service---Criminal-History");
		chr.setPurpose("Administrative File Maintenance (A)");
		chr.setOnBehalfOf("Criminal Justice");
		
		String criminalHistoryRequestPayload = RequestMessageBuilderUtilities.createPersonQueryRequest(chr);
		
		log.debug("Criminal History Request Payload: " + criminalHistoryRequestPayload);
			
		Document criminalHistoryRequestPayloadDoc = OJBUtils.loadXMLFromString(criminalHistoryRequestPayload);
		
		assertEquals("12345",XmlUtils.xPathStringSearch(criminalHistoryRequestPayloadDoc, "/pqr:PersonRecordRequest/pqr:PersonRecordRequestIdentification/nc:IdentificationID"));
		assertEquals("{http://ojbc.org/Services/WSDL/Person_Query_Service-Criminal_History/1.0}Person-Query-Service---Criminal-History",XmlUtils.xPathStringSearch(criminalHistoryRequestPayloadDoc, "/pqr:PersonRecordRequest/pqr:PersonRecordRequestIdentification/nc:IdentificationSourceText"));
	}
	
	@Test
	public void testWarrant() throws Exception
	{
		DetailsRequest warrant = new DetailsRequest();
		
		warrant.setIdentificationID("12345");
		warrant.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/Person_Query_Service-Warrants/1.0}Person-Query-Service---Warrants");
		warrant.setPurpose("Domestic Violence and Stalking (D)");
		warrant.setOnBehalfOf("Criminal Justice");

		
		String warrantRequestPayload = RequestMessageBuilderUtilities.createPersonQueryRequest(warrant);
		
		log.debug("Warrant Request Payload: " + warrantRequestPayload);
			
		Document warrantRequestPayloadDoc = OJBUtils.loadXMLFromString(warrantRequestPayload);
		
		assertEquals("12345",XmlUtils.xPathStringSearch(warrantRequestPayloadDoc, "/pqr:PersonRecordRequest/pqr:PersonRecordRequestIdentification/nc:IdentificationID"));
		assertEquals("{http://ojbc.org/Services/WSDL/Person_Query_Service-Warrants/1.0}Person-Query-Service---Warrants",XmlUtils.xPathStringSearch(warrantRequestPayloadDoc, "/pqr:PersonRecordRequest/pqr:PersonRecordRequestIdentification/nc:IdentificationSourceText"));
		
	}
	
	@Test
	public void testPersonToIncident() throws Exception
	{
		DetailsRequest personToIncident = new DetailsRequest();
		
		personToIncident.setIdentificationID("12345");
		personToIncident.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/IncidentSearchResultsService/1.0}SubmitIncidentPersonSearchResults-RMS");
		
		String personToIncidentRequestPayload = RequestMessageBuilderUtilities.createPersonToIncidentQueryRequest(personToIncident.getIdentificationID(), personToIncident.getIdentificationSourceText());
		
		log.debug("Person To Incident Request Payload: " + personToIncidentRequestPayload);
	
		Document personToIncidentRequestPayloadDoc = OJBUtils.loadXMLFromString(personToIncidentRequestPayload);
		
		assertEquals("12345",XmlUtils.xPathStringSearch(personToIncidentRequestPayloadDoc, "/isr-doc:IncidentPersonSearchRequest/nc:Person/nc:PersonOtherIdentification/nc:IdentificationID"));
		assertEquals("{http://ojbc.org/Services/WSDL/IncidentSearchResultsService/1.0}SubmitIncidentPersonSearchResults-RMS",XmlUtils.xPathStringSearch(personToIncidentRequestPayloadDoc, "/isr-doc:IncidentPersonSearchRequest/isr:SourceSystemNameText"));
	}

	@Test
	public void testVehicleToIncident() throws Exception
	{
		DetailsRequest vehicleToIncident = new DetailsRequest();
		
		vehicleToIncident.setIdentificationID("12345");
		vehicleToIncident.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/IncidentSearchResultsService/1.0}SubmitIncidentPersonSearchResults-RMS");
		
		String vehicleToIncidentRequestPayload = RequestMessageBuilderUtilities.createVehicleToIncidentQueryRequest(vehicleToIncident.getIdentificationID(), vehicleToIncident.getIdentificationSourceText());
		
		log.debug("Vehicle To Incident Request Payload: " + vehicleToIncidentRequestPayload);
			
		Document vehicleToIncidentRequestDoc = OJBUtils.loadXMLFromString(vehicleToIncidentRequestPayload);
		
		assertEquals("12345",XmlUtils.xPathStringSearch(vehicleToIncidentRequestDoc, "/isr-doc:IncidentVehicleSearchRequest/ivsr:Vehicle/ivsr:VehicleSystemIdentification/nc:IdentificationID"));
		assertEquals("{http://ojbc.org/Services/WSDL/IncidentSearchResultsService/1.0}SubmitIncidentPersonSearchResults-RMS",XmlUtils.xPathStringSearch(vehicleToIncidentRequestDoc, "/isr-doc:IncidentVehicleSearchRequest/isr:SourceSystemNameText"));

	}
	
	@Test
	public void testIncidentReportRequest() throws Exception
	{
		DetailsRequest incidentReportRequest = new DetailsRequest();
		
		incidentReportRequest.setIdentificationID("Law12345");
		incidentReportRequest.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/IncidentReportRequestService/1.0}SubmitIncidentIdentiferIncidentReportRequest-RMS");
		
		String incidentCategoryCode = "";
		
		if (incidentReportRequest.getIdentificationSourceText().equals("{http://ojbc.org/Services/WSDL/IncidentReportRequestService/1.0}SubmitIncidentIdentiferIncidentReportRequest-RMS"))
		{
			if(incidentReportRequest.getIdentificationID().startsWith("Citation"))
			{
				incidentCategoryCode = "Citation";
			}
			else if (incidentReportRequest.getIdentificationID().startsWith("Law")) 
			{	
				incidentCategoryCode = "Law";
			} 
			else if(incidentReportRequest.getIdentificationID().startsWith("Traffic"))
			{
				incidentCategoryCode = "Traffic";
			}	
			else
			{
				throw new IllegalStateException("Incident Category Code not specified.");
			}
		}	
		
		//POJO to XML Request
		String incidentRequestPayload = RequestMessageBuilderUtilities.createIncidentReportRequest(StringUtils.substringAfter(incidentReportRequest.getIdentificationID(), incidentCategoryCode), incidentReportRequest.getIdentificationSourceText(), incidentCategoryCode);
		
		Document incidentRequestPayloadDoc = OJBUtils.loadXMLFromString(incidentRequestPayload);
		
		assertEquals("12345",XmlUtils.xPathStringSearch(incidentRequestPayloadDoc, "/iqr-doc:IncidentIdentifierIncidentReportRequest/iqr:Incident/nc:ActivityIdentification/nc:IdentificationID"));
		assertEquals("{http://ojbc.org/Services/WSDL/IncidentReportRequestService/1.0}SubmitIncidentIdentiferIncidentReportRequest-RMS",XmlUtils.xPathStringSearch(incidentRequestPayloadDoc, "/iqr-doc:IncidentIdentifierIncidentReportRequest/iqr:SourceSystemNameText"));
	}

}
