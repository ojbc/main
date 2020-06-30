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

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.CannabisLicensingQueryResponse;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public abstract class AbstractCannabisLicensingQueryResponseProcessor {

	private static final Log log = LogFactory.getLog(AbstractCannabisLicensingQueryResponseProcessor.class);
	
	public abstract void auditCannabisLicenseQueryResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID);
	
	CannabisLicensingQueryResponse processCannabisLicensingQueryResponse(Document document) throws Exception
	{
		CannabisLicensingQueryResponse cannabisLicensingQueryResponse = new CannabisLicensingQueryResponse();

		//Check for error and access denied
		String accessDeniedIndicator = XmlUtils.xPathStringSearch(document, "//qrm:QueryResultsMetadata/iad:InformationAccessDenial/iad:InformationAccessDenialIndicator");
		
		if (StringUtils.isNotBlank(accessDeniedIndicator) && accessDeniedIndicator.equals("true"))
		{
			cannabisLicensingQueryResponse.setQueryResultsAccessDeniedIndicator(true);
			
			String queryResultsErrorText = XmlUtils.xPathStringSearch(document, "//qrm:QueryResultsMetadata/iad:InformationAccessDenial/iad:InformationAccessDenialReasonText");
			cannabisLicensingQueryResponse.setQueryResultsErrorText(queryResultsErrorText);
			
			String systemName = XmlUtils.xPathStringSearch(document, "//qrm:QueryResultsMetadata/iad:InformationAccessDenial/iad:InformationAccessDenyingSystemNameText");
			cannabisLicensingQueryResponse.setSystemName(systemName);
		}	
		
		String errorText = XmlUtils.xPathStringSearch(document, "//qrm:QueryResultsMetadata/qrer:QueryRequestError/qrer:ErrorText");
	
		if (StringUtils.isNotBlank(errorText))
		{
			cannabisLicensingQueryResponse.setQueryResultsErrorIndicator(true);
			cannabisLicensingQueryResponse.setQueryResultsErrorText(errorText);
		}	
		
		String licenseNumber = XmlUtils.xPathStringSearch(document, "//rlq-res-ext:RegulatoryLicenseQueryResult/rlq-res-ext:RegulatoryLicense/rlq-res-ext:RegulatoryLicenseIdentification/nc40:IdentificationID");
		
		if (StringUtils.isNotBlank(licenseNumber))
		{
			cannabisLicensingQueryResponse.setLicenseNumber(licenseNumber);
		}

		String licenseType = XmlUtils.xPathStringSearch(document, "//rlq-res-ext:RegulatoryLicenseQueryResult/rlq-res-ext:RegulatoryLicense/rlq-res-ext:RegulatoryLicenseCategoryText");
		
		if (StringUtils.isNotBlank(licenseType))
		{
			cannabisLicensingQueryResponse.setLicenseType(licenseType);
		}

    	String issueDateString = XmlUtils.xPathStringSearch(document, "//rlq-res-ext:RegulatoryLicenseQueryResult/rlq-res-ext:RegulatoryLicense/rlq-res-ext:RegulatoryLicenseIssueDate/nc40:Date");
    	
    	if (StringUtils.isNotBlank(issueDateString))
    	{
    		LocalDate issueDate = LocalDate.parse(issueDateString);
    		
    		if (issueDate != null)
    		{
    			cannabisLicensingQueryResponse.setIssueDate(issueDate);
    		}	
    	}	
    	
    	String expirationDateString = XmlUtils.xPathStringSearch(document, "//rlq-res-ext:RegulatoryLicenseQueryResult/rlq-res-ext:RegulatoryLicense/rlq-res-ext:RegulatoryLicenseExpirationDate/nc40:Date");
    	
    	if (StringUtils.isNotBlank(expirationDateString))
    	{
    		LocalDate expirationDate = LocalDate.parse(expirationDateString);
    		
    		if (expirationDate != null)
    		{
    			cannabisLicensingQueryResponse.setExpirationDate(expirationDate);
    		}	
    	}	    	
		
    	String systemName = XmlUtils.xPathStringSearch(document, "//rlq-res-ext:RegulatoryLicenseQueryResult/intel40:SystemIdentification/nc40:SystemName");
    			
		if (StringUtils.isNotBlank(systemName))
		{
			cannabisLicensingQueryResponse.setSystemName(systemName);
		}    			
    	
        return cannabisLicensingQueryResponse;
	}

}
