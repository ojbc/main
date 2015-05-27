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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class IncidentReportProcessor {

	private static final Log log = LogFactory.getLog( IncidentReportProcessor.class );
	
	public void processIncidentReport(Document incidentReport) throws Exception
	{
		//XmlUtils.printNode(incidentReport);
		
		String reportingAgencyName = XmlUtils.xPathStringSearch(incidentReport, "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityOrganization/nc:Organization[@s:id= /IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/nc:ActivityReportingOrganizationAssociation[nc:ActivityReference/@s:ref=/IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/@s:id]/nc:OrganizationReference/@s:ref]/nc:OrganizationName");
		log.debug("Agency Name: " + reportingAgencyName);
		
		String incidentDateAsString = XmlUtils.xPathStringSearch(incidentReport, "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityActivity/nc:Activity/nc:ActivityDateRange/nc:StartDate/nc:DateTime");
		log.debug("Incident Date: " + incidentDateAsString);
		
		String mapHorizontalCoordinateText =XmlUtils.xPathStringSearch(incidentReport, "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Location/nc:LocationMapLocation/nc:MapHorizontalCoordinateText");
		log.debug("Map horizontal coordinate text: " + mapHorizontalCoordinateText);
		
		String mapVerticalCoordinateText =XmlUtils.xPathStringSearch(incidentReport, "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Location/nc:LocationMapLocation/nc:MapVerticalCoordinateText");
		log.debug("Map vertical coordinate text: " + mapVerticalCoordinateText);
		
		NodeList arrestNodes = XmlUtils.xPathNodeListSearch(incidentReport, "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexs:Associations/lexs:ArrestSubjectAssociation");
		
		if (arrestNodes.getLength() > 0)
		{
			log.debug("Arrest nodes in document");
			
		}	

	}
	
}
