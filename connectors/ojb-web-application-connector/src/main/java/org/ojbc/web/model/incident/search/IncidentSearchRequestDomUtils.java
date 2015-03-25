package org.ojbc.web.model.incident.search;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.ojbc.util.helper.NIEMXMLUtils;
import org.ojbc.web.util.OJBCWebApplicationNamespaceContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class IncidentSearchRequestDomUtils {

	public static Element createIncidentSearchRequestElement(Document doc) {		
		Element element = doc.createElementNS(OJBCWebApplicationNamespaceContext.INCIDENT_SEARCH_REQUEST, "IncidentSearchRequest");
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
			Element categoryCode = doc.createElementNS(OJBCWebApplicationNamespaceContext.INCIDENT_SEARCH_REQUEST_EXT, "IncidentCategoryCode");
			categoryCode.setTextContent(isr.getIncidentType());
			incidentElement.appendChild(categoryCode);
		}
		
		//TODO: map element for Incident Nature field

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
		
		Element locationElement = NIEMXMLUtils.createLocationElement(doc, "L001");
		
		if (StringUtils.isNotBlank(isr.getIncidentCityTown())) {
			Element locationStructuredAddress = NIEMXMLUtils.createLocationStructuredAddressElement(doc, OJBCWebApplicationNamespaceContext.INCIDENT_SEARCH_REQUEST_EXT);
		
			Element locationCityTownCode = doc.createElementNS(cityTownCodelistNamespace, cityTownCodelistElementName);
			locationCityTownCode.setTextContent(isr.getIncidentCityTown());
			
			Node structuredAddress = locationStructuredAddress.getFirstChild();
			if (!structuredAddress.getLocalName().equals("StructuredAddress")) {
				throw new RuntimeException("LocationAddress contains unexpected child element");
			}
			
			structuredAddress.appendChild(locationCityTownCode);
			
			locationElement.appendChild(locationStructuredAddress);
		}
		
		//TODO: map additional address elements once they are added to POJO and Web app
		
		return locationElement;
	}

}
