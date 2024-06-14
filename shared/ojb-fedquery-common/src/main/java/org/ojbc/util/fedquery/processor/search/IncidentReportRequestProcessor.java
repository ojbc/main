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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.fedquery.model.IncidentReportRequest;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Node;

public class IncidentReportRequestProcessor {
	
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(PersonSearchRequestProcessor.class);
    /**
     * Receives an xml document with report request parameters coming from the portal. 
     * Pulls these parameters out and populates a model pojo with their values
     * 
     * @throws Exception
     */
    public IncidentReportRequest buildIncidentReportRequest(org.w3c.dom.Document document) throws Exception {
    	IncidentReportRequest incidentReportRequest = new IncidentReportRequest();
    	
    	Node incidentNode = XmlUtils.xPathNodeSearch(document, "/iqr-doc:IncidentIdentifierIncidentReportRequest");
    
    	if (incidentNode != null) {
        	Node incidentNameNode = XmlUtils.xPathNodeSearch(incidentNode, "iqr:Incident");
    		String s = XmlUtils.xPathStringSearch(incidentNameNode, "nc:ActivityIdentification/nc:IdentificationID");
    		if (StringUtils.isNotEmpty(s)) {
                incidentReportRequest.setId(s);
            }
    		s = XmlUtils.xPathStringSearch(incidentNameNode, "iqr:IncidentCategoryCode");
    		if (StringUtils.isNotEmpty(s)) {
                incidentReportRequest.setCategoryCode(s);
            }
    		s = XmlUtils.xPathStringSearch(incidentNode, "iqr:SourceSystemNameText");
    		if (StringUtils.isNotEmpty(s)) {
                incidentReportRequest.setSystemNameText(s);
            }
    	}
    	return incidentReportRequest;
    }
}
