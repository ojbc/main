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
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.PersonQueryCriminalHistoryResponse;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public abstract class AbstractPersonQueryCriminalHistoryResponseProcessor {

	private static final Log log = LogFactory.getLog(AbstractPersonQueryCriminalHistoryResponseProcessor.class);
	
	public abstract void auditPersonQueryCriminalHistoryResponseResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID);
	
	PersonQueryCriminalHistoryResponse processPersonQueryCriminalHistoryResponse(Document document) throws Exception
	{
		PersonQueryCriminalHistoryResponse personQueryCriminalHistoryResponse = new PersonQueryCriminalHistoryResponse();

		String givenName = XmlUtils.xPathStringSearch(document, "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonName/nc:PersonGivenName");
		
		if (StringUtils.isNotBlank(givenName))
		{
			personQueryCriminalHistoryResponse.setFirstName(givenName);
		}	

		String middleName = XmlUtils.xPathStringSearch(document, "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonName/nc:PersonMiddleName");
		
		if (StringUtils.isNotBlank(middleName))
		{
			personQueryCriminalHistoryResponse.setMiddleName(middleName);
		}	

		String lastName = XmlUtils.xPathStringSearch(document, "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:Introduction/rap:RapSheetRequest/rap:RapSheetPerson/nc:PersonName/nc:PersonSurName");
		
		if (StringUtils.isNotBlank(lastName))
		{
			personQueryCriminalHistoryResponse.setLastName(lastName);
		}	

		String fbiId = XmlUtils.xPathStringSearch(document, "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");
		
		if (StringUtils.isNotBlank(fbiId))
		{
			personQueryCriminalHistoryResponse.setFbiId(fbiId);
		}
		
		String sid = XmlUtils.xPathStringSearch(document, "/ch-doc:CriminalHistory/ch-ext:RapSheet/rap:RapSheetPerson/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
		
		if (StringUtils.isNotBlank(sid))
		{
			personQueryCriminalHistoryResponse.setSid(sid);
		}			

		String errorText = XmlUtils.xPathStringSearch(document, "/ch-doc:CriminalHistory/error:PersonQueryResultError/error:ErrorText");
		
		if (StringUtils.isNotBlank(errorText))
		{
			personQueryCriminalHistoryResponse.setQueryResultsErrorText(errorText);
			personQueryCriminalHistoryResponse.setQueryResultsErrorIndicator(true);
		}			

		personQueryCriminalHistoryResponse.setSystemName("Criminal History");
		
		log.debug("Criminal History Response: " + personQueryCriminalHistoryResponse.toString());
		
        return personQueryCriminalHistoryResponse;
	}

}
