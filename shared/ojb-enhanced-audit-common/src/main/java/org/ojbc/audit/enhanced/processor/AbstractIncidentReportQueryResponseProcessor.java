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

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.IncidentReportQueryResponse;
import org.ojbc.audit.enhanced.dao.model.PersonQueryCriminalHistoryResponse;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public abstract class AbstractIncidentReportQueryResponseProcessor {

	private static final Log log = LogFactory.getLog(AbstractIncidentReportQueryResponseProcessor.class);
	
	public abstract void auditIncidentReportQueryResponseResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID);
	
	IncidentReportQueryResponse processIncidentReportQueryResponse(Document document) throws Exception
	{
		IncidentReportQueryResponse incidentReportQueryResponse = new IncidentReportQueryResponse();

		//Check for error and access denied
		String accessDeniedIndicator = XmlUtils.xPathStringSearch(document, "/ir:IncidentReport/srm:SearchResultsMetadata/iad:InformationAccessDenial/iad:InformationAccessDenialIndicator");
		
		if (StringUtils.isNotBlank(accessDeniedIndicator) && accessDeniedIndicator.equals("true"))
		{
			incidentReportQueryResponse.setQueryResultsAccessDeniedIndicator(true);
			
			String queryResultsErrorText = XmlUtils.xPathStringSearch(document, "/ir:IncidentReport/srm:SearchResultsMetadata/iad:InformationAccessDenial/iad:InformationAccessDenialReasonText");
			incidentReportQueryResponse.setQueryResultsErrorText(queryResultsErrorText);
			
			String systemName = XmlUtils.xPathStringSearch(document, "/ir:IncidentReport/srm:SearchResultsMetadata/iad:InformationAccessDenial/iad:InformationAccessDenyingSystemNameText");
			incidentReportQueryResponse.setSystemName(systemName);
		}	
		
		String errorText = XmlUtils.xPathStringSearch(document, "/ir:IncidentReport/srm:SearchResultsMetadata/srer:SearchRequestError/srer:ErrorText");
	
		if (StringUtils.isNotBlank(errorText))
		{
			incidentReportQueryResponse.setQueryResultsErrorIndicator(true);
			incidentReportQueryResponse.setQueryResultsErrorText(errorText);
		}	
		
		String incidentNumber = XmlUtils.xPathStringSearch(document, "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:PackageMetadata/lexs:DataItemReferenceID");
		
		if (StringUtils.isNotBlank(incidentNumber))
		{
			incidentReportQueryResponse.setIncidentNumber(incidentNumber);
		}	

		String systemName = XmlUtils.xPathStringSearch(document, "/ir:IncidentReport/lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:PackageMetadata/lexs:DataOwnerMetadata/lexs:DataOwnerIdentifier/nc:OrganizationName");
		
		if (StringUtils.isNotBlank(systemName))
		{
			incidentReportQueryResponse.setSystemName(systemName);
		}	

		log.debug("Incident Report Query Response: " + incidentReportQueryResponse.toString());
		
        return incidentReportQueryResponse;
	}

}
