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
import org.ojbc.audit.enhanced.dao.model.FirearmsQueryResponse;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public abstract class AbstractFirearmsQueryResponseProcessor {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(AbstractFirearmsQueryResponseProcessor.class);
	
	public abstract void auditFirearmsQueryResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID);
	
	FirearmsQueryResponse processFirearmsQueryResponse(Document document) throws Exception
	{
		FirearmsQueryResponse firearmsQueryResponse = new FirearmsQueryResponse();

		//Check for error and access denied
		//We use // for xpath because two potential query responses are potentially sent by the service
		String accessDeniedIndicator = XmlUtils.xPathStringSearch(document, "//iad:InformationAccessDenial/iad:InformationAccessDenialIndicator");
		
		if (StringUtils.isNotBlank(accessDeniedIndicator) && accessDeniedIndicator.equals("true"))
		{
			firearmsQueryResponse.setQueryResultsAccessDeniedIndicator(true);
			
			String queryResultsErrorText = XmlUtils.xPathStringSearch(document, "//iad:InformationAccessDenial/iad:InformationAccessDenialReasonText");
			firearmsQueryResponse.setQueryResultsErrorText(queryResultsErrorText);
			
			String systemName = XmlUtils.xPathStringSearch(document, "//iad:InformationAccessDenial/iad:InformationAccessDenyingSystemNameText");
			firearmsQueryResponse.setSystemName(systemName);
		}	
		
		String firearmsRegistrationErrorText = XmlUtils.xPathStringSearch(document, "//qrm:QueryResultsMetadata/qrer:QueryRequestError/qrer:ErrorText");
	
		if (StringUtils.isNotBlank(firearmsRegistrationErrorText))
		{
			firearmsQueryResponse.setQueryResultsErrorIndicator(true);
			firearmsQueryResponse.setQueryResultsErrorText(firearmsRegistrationErrorText);
		}	
		
		String personGivenName = XmlUtils.xPathStringSearch(document, "//nc:Person/nc:PersonName/nc:PersonGivenName");
		
		if (StringUtils.isNotBlank(personGivenName))
		{
			firearmsQueryResponse.setFirstName(personGivenName);
		}	
		
		String personMiddleName = XmlUtils.xPathStringSearch(document, "//nc:Person/nc:PersonName/nc:PersonMiddleName");
		
		if (StringUtils.isNotBlank(personMiddleName))
		{
			firearmsQueryResponse.setMiddleName(personMiddleName);
		}	
		
		String personSurName = XmlUtils.xPathStringSearch(document, "//nc:Person/nc:PersonName/nc:PersonSurName");
		
		if (StringUtils.isNotBlank(personSurName))
		{
			firearmsQueryResponse.setLastName(personSurName);
		}		
		
		String countyName = XmlUtils.xPathStringSearch(document, "//firearm-ext:ItemRegistration/nc:LocationCountyName");
		
		if (StringUtils.isNotBlank(countyName))
		{
			firearmsQueryResponse.setCounty(countyName);
		}		
		
		String registrationNumber = XmlUtils.xPathStringSearch(document, "//firearm-ext:ItemRegistration/nc:RegistrationIdentification/nc:IdentificationID");
		
		if (StringUtils.isNotBlank(registrationNumber))
		{
			firearmsQueryResponse.setRegistrationNumber(registrationNumber);
		}	
		
		if (StringUtils.isBlank(firearmsQueryResponse.getSystemName()))
		{
			firearmsQueryResponse.setSystemName("Firearms");
		}	
		
        return firearmsQueryResponse;
	}

}
