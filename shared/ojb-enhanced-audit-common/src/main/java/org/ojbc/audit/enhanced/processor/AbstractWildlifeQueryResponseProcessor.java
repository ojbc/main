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
import org.ojbc.audit.enhanced.dao.model.WildlifeQueryResponse;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public abstract class AbstractWildlifeQueryResponseProcessor {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(AbstractWildlifeQueryResponseProcessor.class);
	
	public abstract void auditWildlifeQueryResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID);
	
	WildlifeQueryResponse processWildlifeQueryResponse(Document document) throws Exception
	{
		WildlifeQueryResponse wildlifeQueryResponse = new WildlifeQueryResponse();

		//Check for error and access denied
		String accessDeniedIndicator = XmlUtils.xPathStringSearch(document, "/wlq-res-doc:WildlifeLicenseQueryResults/qrm:QueryResultsMetadata/iad:InformationAccessDenial/iad:InformationAccessDenialIndicator");
		
		if (StringUtils.isNotBlank(accessDeniedIndicator) && accessDeniedIndicator.equals("true"))
		{
			wildlifeQueryResponse.setQueryResultsAccessDeniedIndicator(true);
			
			String queryResultsErrorText = XmlUtils.xPathStringSearch(document, "/wlq-res-doc:WildlifeLicenseQueryResults/qrm:QueryResultsMetadata/iad:InformationAccessDenial/iad:InformationAccessDenialReasonText");
			wildlifeQueryResponse.setQueryResultsErrorText(queryResultsErrorText);
			
			String systemName = XmlUtils.xPathStringSearch(document, "/wlq-res-doc:WildlifeLicenseQueryResults/qrm:QueryResultsMetadata/iad:InformationAccessDenial/iad:InformationAccessDenyingSystemNameText");
			wildlifeQueryResponse.setSystemName(systemName);
		}	
		
		String errorText = XmlUtils.xPathStringSearch(document, "/wlq-res-doc:WildlifeLicenseQueryResults/qrm:QueryResultsMetadata/qrer:QueryRequestError/qrer:ErrorText");
	
		if (StringUtils.isNotBlank(errorText))
		{
			wildlifeQueryResponse.setQueryResultsErrorIndicator(true);
			wildlifeQueryResponse.setQueryResultsErrorText(errorText);
		}	
		
		String residenceCity = XmlUtils.xPathStringSearch(document, "/wlq-res-doc:WildlifeLicenseQueryResults/wlq-res-ext:WildlifeLicenseReport/nc30:Location/nc30:Address/nc30:LocationCityName");
		
		if (StringUtils.isNotBlank(residenceCity))
		{
			wildlifeQueryResponse.setResidenceCity(residenceCity);
		}

    	String systemName = XmlUtils.xPathStringSearch(document, "/wlq-res-doc:WildlifeLicenseQueryResults/wlq-res-ext:WildlifeLicenseReport/intel31:SystemIdentification/nc30:SystemName");
    			
		if (StringUtils.isNotBlank(systemName))
		{
			wildlifeQueryResponse.setSystemName(systemName);
		}    			
    	
        return wildlifeQueryResponse;
	}

}
