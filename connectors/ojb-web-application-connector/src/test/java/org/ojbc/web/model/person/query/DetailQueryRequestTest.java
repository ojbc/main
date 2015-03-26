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
package org.ojbc.web.model.person.query;


import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.web.util.RequestMessageBuilderUtilities;

public class DetailQueryRequestTest {

	private static final Log log = LogFactory.getLog( DetailQueryRequestTest.class );
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCriminalHistory()
	{
		DetailsRequest chr = new DetailsRequest();
		
		chr.setIdentificationID("12345");
		chr.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/Person_Query_Service-Criminal_History/1.0}Person-Query-Service---Criminal-History");
		chr.setPurpose("Administrative File Maintenance (A)");
		chr.setOnBehalfOf("Criminal Justice");
		
		String criminalHistoryRequestPayload = RequestMessageBuilderUtilities.createPersonQueryRequest(chr);
		
		log.debug("Criminal History Request Payload: " + criminalHistoryRequestPayload);
	
		//TODO: also do assertion with XML Unit
		assertEquals("<pqr:PersonRecordRequest xmlns:pqr=\"http://ojbc.org/IEPD/Exchange/PersonQueryRequest/1.0\" xmlns:nc20=\"http://niem.gov/niem/niem-core/2.0\">    <pqr:PersonRecordRequestIdentification >        <nc20:IdentificationID>12345</nc20:IdentificationID>        <nc20:IdentificationSourceText>{http://ojbc.org/Services/WSDL/Person_Query_Service-Criminal_History/1.0}Person-Query-Service---Criminal-History</nc20:IdentificationSourceText>    </pqr:PersonRecordRequestIdentification></pqr:PersonRecordRequest>", criminalHistoryRequestPayload);
		
	}
	
	@Test
	public void testWarrant()
	{
		DetailsRequest warrant = new DetailsRequest();
		
		warrant.setIdentificationID("12345");
		warrant.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/Person_Query_Service-Warrants/1.0}Person-Query-Service---Warrants");
		warrant.setPurpose("Domestic Violence and Stalking (D)");
		warrant.setOnBehalfOf("Criminal Justice");

		
		String criminalHistoryRequestPayload = RequestMessageBuilderUtilities.createPersonQueryRequest(warrant);
		
		log.debug("Warrant Request Payload: " + criminalHistoryRequestPayload);
	
		//TODO: also do assertion with XML Unit
		assertEquals("<pqr:PersonRecordRequest xmlns:pqr=\"http://ojbc.org/IEPD/Exchange/PersonQueryRequest/1.0\" xmlns:nc20=\"http://niem.gov/niem/niem-core/2.0\">    <pqr:PersonRecordRequestIdentification >        <nc20:IdentificationID>12345</nc20:IdentificationID>        <nc20:IdentificationSourceText>{http://ojbc.org/Services/WSDL/Person_Query_Service-Warrants/1.0}Person-Query-Service---Warrants</nc20:IdentificationSourceText>    </pqr:PersonRecordRequestIdentification></pqr:PersonRecordRequest>", criminalHistoryRequestPayload);
		
	}
	
	@Test
	public void testPersonToIncident()
	{
		DetailsRequest personToIncident = new DetailsRequest();
		
		personToIncident.setIdentificationID("12345");
		personToIncident.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/IncidentSearchResultsService/1.0}SubmitIncidentPersonSearchResults-DPS");
		
		String personToIncidentRequestPayload = RequestMessageBuilderUtilities.createPersonToIncidentQueryRequest(personToIncident.getIdentificationID(), personToIncident.getIdentificationSourceText());
		
		log.debug("Person To Incident Request Payload: " + personToIncidentRequestPayload);
	
		//TODO: also do assertion with XML Unit
		assertEquals("<exchange:IncidentPersonSearchRequest	xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/IncidentSearchRequest/1.0\"	xmlns:ext=\"http://ojbc.org/IEPD/Extensions/IncidentSearchRequest/1.0\"	xmlns:nc=\"http://niem.gov/niem/niem-core/2.0\">	<nc:Person>		<nc:PersonOtherIdentification>			<nc:IdentificationID>12345</nc:IdentificationID>		</nc:PersonOtherIdentification>	</nc:Person>	<ext:SourceSystemNameText>{http://ojbc.org/Services/WSDL/IncidentSearchResultsService/1.0}SubmitIncidentPersonSearchResults-DPS</ext:SourceSystemNameText></exchange:IncidentPersonSearchRequest>", personToIncidentRequestPayload);
		
	}

	@Test
	public void testVehicleToIncident()
	{
		DetailsRequest vehicleToIncident = new DetailsRequest();
		
		vehicleToIncident.setIdentificationID("12345");
		vehicleToIncident.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/IncidentSearchResultsService/1.0}SubmitIncidentPersonSearchResults-DPS");
		
		String vehicleToIncidentRequestPayload = RequestMessageBuilderUtilities.createVehicleToIncidentQueryRequest(vehicleToIncident.getIdentificationID(), vehicleToIncident.getIdentificationSourceText());
		
		log.debug("Vehicle To Incident Request Payload: " + vehicleToIncidentRequestPayload);
	
		//TODO: also do assertion with XML Unit
		assertEquals("<exchange:IncidentVehicleSearchRequest	xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/IncidentSearchRequest/1.0\"	xmlns:nc=\"http://niem.gov/niem/niem-core/2.0\"	xmlns:extVehicle=\"http://ojbc.org/IEPD/Extensions/IncidentVehicleSearchRequest/1.0\"	xmlns:ext=\"http://ojbc.org/IEPD/Extensions/IncidentSearchRequest/1.0\">		<extVehicle:Vehicle>			<extVehicle:VehicleSystemIdentification>				<nc:IdentificationID>12345</nc:IdentificationID>			</extVehicle:VehicleSystemIdentification>		</extVehicle:Vehicle>		<ext:SourceSystemNameText>{http://ojbc.org/Services/WSDL/IncidentSearchResultsService/1.0}SubmitIncidentPersonSearchResults-DPS</ext:SourceSystemNameText></exchange:IncidentVehicleSearchRequest>", vehicleToIncidentRequestPayload);
		
	}
	
	@Test
	public void testIncidentReportRequest()
	{
		DetailsRequest incidentReportRequest = new DetailsRequest();
		
		incidentReportRequest.setIdentificationID("Law12345");
		incidentReportRequest.setIdentificationSourceText("{http://ojbc.org/Services/WSDL/IncidentReportRequestService/1.0}SubmitIncidentIdentiferIncidentReportRequest-DPS");
		
		String incidentCategoryCode = "";
		
		if (incidentReportRequest.getIdentificationSourceText().equals("{http://ojbc.org/Services/WSDL/IncidentReportRequestService/1.0}SubmitIncidentIdentiferIncidentReportRequest-DPS"))
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
		
		log.debug("Incident Report Request Payload: " + incidentRequestPayload);
	
		//TODO: also do assertion with XML Unit
		assertEquals("<exchange:IncidentIdentifierIncidentReportRequest	xmlns:exchange=\"http://ojbc.org/IEPD/Exchange/IncidentReportRequest/1.0\"	xmlns:extension=\"http://ojbc.org/IEPD/Extensions/IncidentReportRequest/1.0\"	xmlns:nc=\"http://niem.gov/niem/niem-core/2.0\">	<extension:Incident>	    <nc:ActivityIdentification>	    	<nc:IdentificationID>12345</nc:IdentificationID>	    </nc:ActivityIdentification>	    <extension:IncidentCategoryCode>Law</extension:IncidentCategoryCode>	</extension:Incident> <extension:SourceSystemNameText>{http://ojbc.org/Services/WSDL/IncidentReportRequestService/1.0}SubmitIncidentIdentiferIncidentReportRequest-DPS</extension:SourceSystemNameText></exchange:IncidentIdentifierIncidentReportRequest>", incidentRequestPayload);
		
	}

}
