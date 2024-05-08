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
package org.ojbc.util.fedquery.processor.search;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.model.incidentsearch.IncidentSearchRequest;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Node;

public class IncidentSearchRequestProcessor {
    @SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(PersonSearchRequestProcessor.class);
    
    /**
     * Receives an xml document with Search request parameters coming from the portal. 
     * Pulls these parameters out and populates a model pojo with their values
     * 
     * @throws Exception
     */
    public IncidentSearchRequest buildIncidentPersonSearchRequest(org.w3c.dom.Document document) throws Exception {
    	IncidentSearchRequest incidentSearchRequest = new IncidentSearchRequest();
    	
    	Node incidentNode = XmlUtils.xPathNodeSearch(document, "/isr-doc:IncidentPersonSearchRequest");
    
    	if (incidentNode != null) {
        	Node incidentPersonNode = XmlUtils.xPathNodeSearch(incidentNode, "nc: Person");
    		String s = XmlUtils.xPathStringSearch(incidentPersonNode, "nc:PersonOtherIdentification/nc:IdentificationID");
    		if (StringUtils.isNotEmpty(s)) {
                incidentSearchRequest.setIncidentSearchRequestId(Integer.parseInt(s));
            }
    		s = XmlUtils.xPathStringSearch(incidentNode, "isr:SourceSystemNameText");
    		if (StringUtils.isNotEmpty(s)) {
    			List<String> tempLst = new ArrayList<String>();
    			tempLst.add(s);
                incidentSearchRequest.setSystemsToSearch(tempLst);
            }
    		Node searchMetaDataNode = XmlUtils.xPathNodeSearch(incidentNode, "isr:SearchMetadata");
    		if(searchMetaDataNode != null) {
    			s = XmlUtils.xPathStringSearch(searchMetaDataNode, "isr:SearchRequestOnBehalfOfText");
        		if (StringUtils.isNotEmpty(s)) {
                    incidentSearchRequest.setOnBehalfOf(s);
                }
        		s = XmlUtils.xPathStringSearch(searchMetaDataNode, "isr:SearchPurposeText");
        		if (StringUtils.isNotEmpty(s)) {
                    incidentSearchRequest.setPurpose(s);
        		} 
    		}
    	}
    	return incidentSearchRequest;                                                                                       
    }
    
    public IncidentSearchRequest buildIncidentSearchRequest(org.w3c.dom.Document document) throws Exception {
    	IncidentSearchRequest incidentSearchRequest = new IncidentSearchRequest();
    	
    	Node incidentNode = XmlUtils.xPathNodeSearch(document, "/isr-doc:IncidentSearchRequest");
    
    	if (incidentNode != null) {
        	Node incidentNameNode = XmlUtils.xPathNodeSearch(incidentNode, "isr:Incident");
    		String s = XmlUtils.xPathStringSearch(incidentNameNode, "nc:ActivityIdentification/nc:IdentificationID");
    		if (StringUtils.isNotEmpty(s)) {
                incidentSearchRequest.setIncidentNumber(s);
            }
    		
    		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss");
    		
    		s = XmlUtils.xPathStringSearch(incidentNameNode, "nc:ActivityDateRange/nc:StartDate/nc:DateTime");
    		if (StringUtils.isNotEmpty(s)) {
    			LocalDate startDate = LocalDate.parse(s, dtf);
                incidentSearchRequest.setStartDate(startDate);
            }
    		
    		s = XmlUtils.xPathStringSearch(incidentNameNode, "nc:ActivityDateRange/nc:EndDate/nc:DateTime");
    		if (StringUtils.isNotEmpty(s)) {
    			LocalDate endDate = LocalDate.parse(s, dtf);
                incidentSearchRequest.setEndDate(endDate);
            }
    		
    		s = XmlUtils.xPathStringSearch(incidentNameNode, "isr:IncidentCategoryCode");
    		if (StringUtils.isNotEmpty(s)) {
    			incidentSearchRequest.setCategoryType(s);
            }
    		
    		Node locationNode = XmlUtils.xPathNodeSearch(incidentNode, "nc:Location");
    		
    		s = XmlUtils.xPathStringSearch(locationNode, "nc:LocationAddress/isr:StructuredAddress/incident-location-codes-demostate:LocationCityTownCode");
    		if (StringUtils.isNotEmpty(s)) {
                incidentSearchRequest.setCityTown(s);
            }
    		
    		Node searchMetaDataNode = XmlUtils.xPathNodeSearch(incidentNode, "isr:SearchMetadata");
    		if(searchMetaDataNode != null) {
    			s = XmlUtils.xPathStringSearch(searchMetaDataNode, "isr:SearchRequestOnBehalfOfText");
        		if (StringUtils.isNotEmpty(s)) {
                    incidentSearchRequest.setOnBehalfOf(s);
                }
        		
        		s = XmlUtils.xPathStringSearch(searchMetaDataNode, "isr:SearchPurposeText");
        		if (StringUtils.isNotEmpty(s)) {
                    incidentSearchRequest.setPurpose(s);
        		}
    		}
    		
    		
    	}
    	return incidentSearchRequest;                                                                                       
    }
}
