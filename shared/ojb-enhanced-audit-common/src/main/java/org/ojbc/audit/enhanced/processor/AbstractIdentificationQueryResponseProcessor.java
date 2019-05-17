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
import java.time.format.DateTimeFormatter;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.IdentificationQueryResponse;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public abstract class AbstractIdentificationQueryResponseProcessor {

	private static final Log log = LogFactory.getLog(AbstractIdentificationQueryResponseProcessor.class);
	
	public abstract void auditIdentificationQueryResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID);
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	IdentificationQueryResponse processIdentificationQueryResponse(Document document) throws Exception
	{
		IdentificationQueryResponse identificationQueryResponse = new IdentificationQueryResponse();
		
		String givenName = XmlUtils.xPathStringSearch(document, "/oiirq-res-doc:OrganizationIdentificationInitialResultsQueryResults/oirq-res-ext:IdentifiedPerson/nc30:PersonName/nc30:PersonGivenName");
		
		if (StringUtils.isNotBlank(givenName))
		{
			identificationQueryResponse.setPersonFirstName(givenName);
		}	

		String middleName = XmlUtils.xPathStringSearch(document, "/oiirq-res-doc:OrganizationIdentificationInitialResultsQueryResults/oirq-res-ext:IdentifiedPerson/nc30:PersonName/nc30:PersonMiddleName");
		
		if (StringUtils.isNotBlank(middleName))
		{
			identificationQueryResponse.setPersonMiddleName(middleName);
		}	

		String lastName = XmlUtils.xPathStringSearch(document, "/oiirq-res-doc:OrganizationIdentificationInitialResultsQueryResults/oirq-res-ext:IdentifiedPerson/nc30:PersonName/nc30:PersonSurName");
		
		if (StringUtils.isNotBlank(lastName))
		{
			identificationQueryResponse.setPersonLastName(lastName);
		}	

		String fbiId = XmlUtils.xPathStringSearch(document, "/oiirq-res-doc:OrganizationIdentificationInitialResultsQueryResults/oirq-res-ext:IdentifiedPerson/jxdm50:PersonAugmentation/jxdm50:PersonFBIIdentification/nc30:IdentificationID");
		
		if (StringUtils.isNotBlank(fbiId))
		{
			identificationQueryResponse.setFbiId(fbiId);
		}
		
		String sid = XmlUtils.xPathStringSearch(document, "/oiirq-res-doc:OrganizationIdentificationInitialResultsQueryResults/oirq-res-ext:IdentifiedPerson/jxdm50:PersonAugmentation/jxdm50:PersonStateFingerprintIdentification/nc30:IdentificationID");
		
		if (StringUtils.isNotBlank(sid))
		{
			identificationQueryResponse.setSid(sid);
		}			

		String otn = XmlUtils.xPathStringSearch(document, "/oiirq-res-doc:OrganizationIdentificationInitialResultsQueryResults/oirq-res-ext:IdentifiedPerson/oirq-res-ext:IdentifiedPersonTrackingIdentification/nc30:IdentificationID");
		
		if (StringUtils.isNotBlank(otn))
		{
			identificationQueryResponse.setOtn(otn);
		}			
		
		String oca =  XmlUtils.xPathStringSearch(document, "/oiirq-res-doc:OrganizationIdentificationInitialResultsQueryResults/nc30:EntityOrganization/nc30:OrganizationIdentification/nc30:IdentificationID");
		
		if (StringUtils.isNotBlank(oca))
		{
			identificationQueryResponse.setOca(oca);
		}		
		
		String ori =  XmlUtils.xPathStringSearch(document, "/oiirq-res-doc:OrganizationIdentificationInitialResultsQueryResults/nc30:EntityOrganization/jxdm50:OrganizationAugmentation/jxdm50:OrganizationORIIdentification/nc30:IdentificationID");

		if (StringUtils.isNotBlank(ori))
		{
			identificationQueryResponse.setOri(ori);
		}			
		
		String idDateString = XmlUtils.xPathStringSearch(document, "/oiirq-res-doc:OrganizationIdentificationInitialResultsQueryResults/oirq-res-ext:IdentificationReportedDate/nc30:Date");
		
		if (StringUtils.isNotBlank(idDateString))
		{
			identificationQueryResponse.setIdDate(LocalDate.parse(idDateString, formatter));
		}			
		
        log.debug("Identification Query Result: " + identificationQueryResponse.toString());

        return identificationQueryResponse;
	}

}
