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
package org.ojbc.web.model.incident.search;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.ojbc.util.helper.NIEMXMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.ojbc.util.xml.OjbcNamespaceContext;

public class IncidentSearchRequestDomUtils {

	public static Element createIncidentSearchRequestElement(Document doc) {		
		Element element = doc.createElementNS(OjbcNamespaceContext.NS_INCIDENT_SEARCH_REQUEST_DOC, "IncidentSearchRequest");
		return element;
	}

	//	<ext:Incident s:id="I001">
	//		<nc:ActivityIdentification>
	//			<nc:IdentificationID>12345</nc:IdentificationID>
	//		</nc:ActivityIdentification>
	//		<nc:ActivityDateRange>
	//			<nc:StartDate>
	//				<nc:DateTime>2012-04-01T12:00:00</nc:DateTime>
	//			</nc:StartDate>
	//			<nc:EndDate>
	//				<nc:DateTime>2012-04-01T12:00:00</nc:DateTime>
	//			</nc:EndDate>
	//		</nc:ActivityDateRange>
	//		<ext:IncidentCategoryCode>Law</ext:IncidentCategoryCode>
	//  </ext:Incident>
	public static Element createIncidentElement(Document doc,
			IncidentSearchRequest isr) {
		
		Element incidentElement = NIEMXMLUtils.createIncidentElement(doc, "I001");
		
		if (StringUtils.isNotBlank(isr.getIncidentNumber())) {
			Element incidentNumber = NIEMXMLUtils.createIdentificationElementWithParent(doc, "ActivityIdentification", isr.getIncidentNumber());
			incidentElement.appendChild(incidentNumber);
		}
		
		//Check to see if both incident start and end are populated
		if (isr.getIncidentDateRangeStart() != null && isr.getIncidentDateRangeEnd() != null) {
			
			DateTime incidentDateRangeEnd = isr.getIncidentDateRangeEnd();
			
			//If incident start and end date are the same day, we set the end date to following date to get a date range because the queries also take the time into account
			if (isr.getIncidentDateRangeStart().equals(isr.getIncidentDateRangeEnd()))
			{
				incidentDateRangeEnd = incidentDateRangeEnd.plusDays(1);
			}	
			
			Element dateRange = NIEMXMLUtils.createNC20DateRangeElementWithParent(doc, "ActivityDateRange", isr.getIncidentDateRangeStart(), incidentDateRangeEnd);
			incidentElement.appendChild(dateRange);
		}
		
		//If only incident start date is populated query on a single day
		if (isr.getIncidentDateRangeStart() != null && isr.getIncidentDateRangeEnd() == null) {
			
			DateTime incidentDateRangeEnd = isr.getIncidentDateRangeStart().plusDays(1);
			
			Element dateRange = NIEMXMLUtils.createNC20DateRangeElementWithParent(doc, "ActivityDateRange", isr.getIncidentDateRangeStart(), incidentDateRangeEnd);
			incidentElement.appendChild(dateRange);
		}
		
		
		if (StringUtils.isNotBlank(isr.getIncidentType()))  {
			Element categoryCode = doc.createElementNS(OjbcNamespaceContext.NS_INCIDENT_SEARCH_REQUEST_EXT, "IncidentCategoryCode");
			categoryCode.setTextContent(isr.getIncidentType());
			incidentElement.appendChild(categoryCode);
		}
		
		return incidentElement;
	}

	//	<nc:Location s:id="L001">
	//		<nc:LocationAddress>
	//			<ext:StructuredAddress>
	//<!-- 			<nc:LocationStreet>
	//					<nc:StreetNumberText>101</nc:StreetNumberText>
	//					<nc:StreetName>Main Street</nc:StreetName>
	//				</nc:LocationStreet>
	//				<nc:AddressSecondaryUnitText>4B</nc:AddressSecondaryUnitText>
	//				<nc:LocationStateUSPostalServiceCode>VT
	//					</nc:LocationStateUSPostalServiceCode>
	//				<nc:LocationPostalCode>00000</nc:LocationPostalCode> -->
	//				<ext:LocationCityTownCode>Burlington</ext:LocationCityTownCode>
	//			</ext:StructuredAddress>
	//		</nc:LocationAddress>
	//	</nc:Location>
	public static Element createLocationElement(Document doc,
			IncidentSearchRequest isr, String cityTownCodelistNamespace, String cityTownCodelistElementName) {
		
		//UI only provide for 'city/town' searches so that is the only mapping provided
		
		Element locationElement = NIEMXMLUtils.createLocationElement(doc, "L001");
		
		if (StringUtils.isNotBlank(isr.getIncidentCityTown())) {
			Element locationStructuredAddress = NIEMXMLUtils.createLocationStructuredAddressElement(doc, OjbcNamespaceContext.NS_INCIDENT_SEARCH_REQUEST_EXT);
		
			Element locationCityTownCode = doc.createElementNS(cityTownCodelistNamespace, cityTownCodelistElementName);
			locationCityTownCode.setTextContent(isr.getIncidentCityTown());
			
			Node structuredAddress = locationStructuredAddress.getFirstChild();
			if (!structuredAddress.getLocalName().equals("StructuredAddress")) {
				throw new RuntimeException("LocationAddress contains unexpected child element");
			}
			
			structuredAddress.appendChild(locationCityTownCode);
			
			locationElement.appendChild(locationStructuredAddress);
		}
		
		return locationElement;
	}

}
