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
package org.ojbc.audit.enhanced.processor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.IncidentSearchRequest;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractIncidentSearchRequestProcessor {

	private static final Log log = LogFactory.getLog(AbstractIncidentSearchRequestProcessor.class);
	
	public abstract void auditIncidentSearchRequest(Exchange exchange, @Body Document document);
	
	IncidentSearchRequest processIncidentSearchRequest(Document document) throws Exception
	{
		IncidentSearchRequest incidentSearchRequest = new IncidentSearchRequest();

        Node incidentSearchNode = XmlUtils.xPathNodeSearch(document, "/isr-doc:IncidentSearchRequest/isr:Incident");

        if (incidentSearchNode != null) {
        	
        	String incidentNumber = XmlUtils.xPathStringSearch(incidentSearchNode, "nc:ActivityIdentification/nc:IdentificationID");
        	
        	if (StringUtils.isNotBlank(incidentNumber))
        	{
        		incidentSearchRequest.setIncidentNumber(incidentNumber);
        	}	
        	
        	String startDateString = XmlUtils.xPathStringSearch(incidentSearchNode, "nc:ActivityDateRange/nc:StartDate/nc:DateTime");
        	
        	if (StringUtils.isNotBlank(startDateString))
        	{
        		LocalDate startDate = LocalDate.parse(StringUtils.substringBefore(startDateString, "T"));
        		
        		if (startDate != null)
        		{
        			incidentSearchRequest.setStartDate(startDate);
        		}	
        		
        	}	

        	String endDateString = XmlUtils.xPathStringSearch(incidentSearchNode, "nc:ActivityDateRange/nc:EndDate/nc:DateTime");
        	
        	if (StringUtils.isNotBlank(endDateString))
        	{
        		LocalDate endDate = LocalDate.parse(StringUtils.substringBefore(endDateString, "T"));
        		
        		if (endDate != null)
        		{
        			incidentSearchRequest.setEndDate(endDate);
        		}	
        		
        	}	
        	
        }
        
    	String cityTown = XmlUtils.xPathStringSearch(document, "/isr-doc:IncidentSearchRequest/nc:Location/nc:LocationAddress/isr:StructuredAddress/*[local-name()='LocationCityTownCode']");
    	
    	if (StringUtils.isNotBlank(cityTown))
    	{
    		incidentSearchRequest.setCityTown(cityTown);
    	}	
        
        NodeList sourceSystems = XmlUtils.xPathNodeListSearch(document, "/isr-doc:IncidentSearchRequest/isr:SourceSystemNameText");

        List<String> sourceSystemsList = new ArrayList<String>();
        
        if (sourceSystems != null) {
            int length = sourceSystems.getLength();
            for (int i = 0; i < length; i++) {
                if (sourceSystems.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element sourceSystem = (Element) sourceSystems.item(i);
                    
                    if (StringUtils.isNotBlank(sourceSystem.getTextContent()))
                    {
                    	sourceSystemsList.add(sourceSystem.getTextContent());
                    }		
                }
            }
        }
        
        String onBehalfOfText = XmlUtils.xPathStringSearch(document, "/isr-doc:IncidentSearchRequest/isr:SearchMetadata/isr:SearchRequestOnBehalfOfText");
        
        if (StringUtils.isNotEmpty(onBehalfOfText)) {
        	incidentSearchRequest.setOnBehalfOf(onBehalfOfText);
        }

        String purposeText = XmlUtils.xPathStringSearch(document, "/isr-doc:IncidentSearchRequest/isr:SearchMetadata/isr:SearchPurposeText");
        
        if (StringUtils.isNotEmpty(purposeText)) {
        	incidentSearchRequest.setPurpose(purposeText);
        }
        
        incidentSearchRequest.setSystemsToSearch(sourceSystemsList);

        log.debug("Incident Search Request: " + incidentSearchRequest.toString());

        return incidentSearchRequest;
	}

}
