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
package org.ojbc.adapters.analyticaldatastore.processor;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticaldatastore.personid.IdentifierGenerationStrategy;
import org.ojbc.adapters.analyticaldatastore.util.AnalyticalDataStoreUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IncidentReportProcessor {

	private static final Log log = LogFactory.getLog( IncidentReportProcessor.class );
	
	private static final String PATH_TO_LEXS_DATA_ITEM_PACKAGE="/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage";
	
	private static final String PATH_TO_LEXS_DIGEST= PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:Digest";
	
	private IdentifierGenerationStrategy identifierGenerationStrategy;
		
	public void processIncidentReport(Document incidentReport) throws Exception
	{
		//XmlUtils.printNode(incidentReport);
		
		String reportingAgencyName = XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DIGEST + "/lexsdigest:EntityOrganization/nc:Organization[@s:id= " + PATH_TO_LEXS_DIGEST + " /lexsdigest:Associations/nc:ActivityReportingOrganizationAssociation[nc:ActivityReference/@s:ref=" + PATH_TO_LEXS_DIGEST + "/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/@s:id]/nc:OrganizationReference/@s:ref]/nc:OrganizationName");
		log.debug("Agency Name: " + reportingAgencyName);
		
		String incidentDateTimeAsString = XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DIGEST+ "/lexsdigest:EntityActivity/nc:Activity/nc:ActivityDateRange/nc:StartDate/nc:DateTime");
		log.debug("Incident Date: " + incidentDateTimeAsString);
		
		Calendar incidentDateTimeCal = DatatypeConverter.parseDateTime(incidentDateTimeAsString);
		Date incidentDateTime = incidentDateTimeCal.getTime();

		Time incidentTime =  new Time(incidentDateTime.getTime());
		log.debug("Incident Time: " + incidentTime.toString());
		
		String mapHorizontalCoordinateText =XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Location/nc:LocationMapLocation/nc:MapHorizontalCoordinateText");
		log.debug("Map horizontal coordinate text: " + mapHorizontalCoordinateText);
		
		String mapVerticalCoordinateText =XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Location/nc:LocationMapLocation/nc:MapVerticalCoordinateText");
		log.debug("Map vertical coordinate text: " + mapVerticalCoordinateText);
		
		String incidentCaseNumber=XmlUtils.xPathStringSearch(incidentReport,  PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:PackageMetadata/lexs:DataItemID");
		log.debug("Incident Case Number: " + incidentCaseNumber);
		
		String incidentLocationReference = XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DIGEST + "/lexsdigest:Associations/lexsdigest:IncidentLocationAssociation/nc:LocationReference/@s:ref");
		
		if (StringUtils.isNotEmpty(incidentLocationReference))
		{
			Node locationNode = XmlUtils.xPathNodeSearch(incidentReport, PATH_TO_LEXS_DIGEST + "/lexsdigest:EntityLocation/nc:Location[@s:id='" + incidentLocationReference + "']");
			
			String streetFullText = XmlUtils.xPathStringSearch(locationNode, "nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetFullText");
			log.debug("Street Full Text: " + streetFullText);
			
			//TODO: build address from components if no streetfull text
			
			String cityTown = XmlUtils.xPathStringSearch(locationNode, "nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName");
			log.debug("City/Town: " + cityTown);
		}	
		
		NodeList arrestNodes = XmlUtils.xPathNodeListSearch(incidentReport, PATH_TO_LEXS_DIGEST + "/lexsdigest:Associations/lexsdigest:ArrestSubjectAssociation");
		
	    if (arrestNodes != null && arrestNodes.getLength() > 0) 
	    {
			log.debug("Arrest nodes in document");

	        processArrests(incidentReport, arrestNodes);
	    }
			
	}

	protected void processArrests(Document incidentReport, NodeList arrestNodes)
			throws Exception {
		for (int i = 0; i < arrestNodes.getLength(); i++) 
		{
		    if (arrestNodes.item(i).getNodeType() == Node.ELEMENT_NODE)
		    {
		        String personId = XmlUtils.xPathStringSearch(arrestNodes.item(i), "nc:PersonReference/@s:ref");
		        log.debug("Arrestee Person ID: " + personId);
		        
		        Node personNode = XmlUtils.xPathNodeSearch(incidentReport, PATH_TO_LEXS_DIGEST + "/lexsdigest:EntityPerson/lexsdigest:Person[@s:id='" + personId + "']");
		        
		        Map<String, Object> arrestee = AnalyticalDataStoreUtils.retrieveMapOfPersonAttributes(personNode);
		        
		        String personIdentifierKey = identifierGenerationStrategy.generateIdentifier(arrestee);

		        log.debug("Arrestee person identifier keys: " + personIdentifierKey);
		        
		        String arrestActivityReference = XmlUtils.xPathStringSearch(arrestNodes.item(i), "nc:ActivityReference/@s:ref");
		        log.debug("Arrest Activity Reference: " + arrestActivityReference);
		        
		        Node arrestNode = XmlUtils.xPathNodeSearch(incidentReport, PATH_TO_LEXS_DIGEST + "/lexsdigest:EntityActivity/nc:Activity[@s:id='" + arrestActivityReference + "']");
		        
		        String arrestDateTimeAsString = XmlUtils.xPathStringSearch(arrestNode, "nc:ActivityDate/nc:DateTime");
		        
				Calendar arrestDateTimeCal = DatatypeConverter.parseDateTime(arrestDateTimeAsString);
				Date arrestDateTime = arrestDateTimeCal.getTime();

				Time arrestTime =  new Time(arrestDateTime.getTime());
				log.debug("Arrest Time: " + arrestTime.toString());

				String arrestingAgency = returnArrestingAgency(incidentReport);
				log.debug("Arresting Agency: " + arrestingAgency);
		        
		    }
		}
	}

	private String returnArrestingAgency(Node incidentReport) throws Exception
	{
		String arrestingOfficerRefernce = XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DIGEST + "/lexsdigest:Associations/lexsdigest:ArrestOfficerAssociation/nc:PersonReference/@s:ref");
		log.debug("Arresting Officer Reference: " + arrestingOfficerRefernce);
		
		String arrestAgencyReference=XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DIGEST + "/lexsdigest:Associations/nc:PersonAssignedUnitAssociation/nc:PersonReference[@s:ref='" + arrestingOfficerRefernce + "']/following-sibling::nc:OrganizationReference/@s:ref");
		log.debug("Arresting Agency Reference: " + arrestAgencyReference);
		
		String arrestingAgency = XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DIGEST + "/lexsdigest:EntityOrganization/nc:Organization[@s:id='" + arrestAgencyReference + "']/nc:OrganizationName");
		log.debug("Arresting Agency: " + arrestingAgency);
		
		return arrestingAgency;
	}
	
	public IdentifierGenerationStrategy getIdentifierGenerationStrategy() {
		return identifierGenerationStrategy;
	}

	public void setIdentifierGenerationStrategy(
			IdentifierGenerationStrategy identifierGenerationStrategy) {
		this.identifierGenerationStrategy = identifierGenerationStrategy;
	}
	
}
