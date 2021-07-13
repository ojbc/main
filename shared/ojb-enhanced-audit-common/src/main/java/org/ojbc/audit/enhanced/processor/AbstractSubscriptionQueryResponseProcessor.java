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
import org.ojbc.audit.enhanced.dao.model.SubscriptionQueryResponse;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public abstract class AbstractSubscriptionQueryResponseProcessor {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(AbstractSubscriptionQueryResponseProcessor.class);
	
	public abstract void auditSubscriptionQueryResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID);
	
	SubscriptionQueryResponse processSubscriptionQueryResponse(Document document) throws Exception
	{
		SubscriptionQueryResponse subscriptionQueryResponse = new SubscriptionQueryResponse();

		//Check for error and access denied
		String accessDeniedIndicator = XmlUtils.xPathStringSearch(document, "/sqr:SubscriptionQueryResults/qrm:QueryResultsMetadata/iad:InformationAccessDenial/iad:InformationAccessDenialIndicator");
		
		if (StringUtils.isNotBlank(accessDeniedIndicator) && accessDeniedIndicator.equals("true"))
		{
			subscriptionQueryResponse.setQueryResultsAccessDeniedIndicator(true);
			
			String queryResultsErrorText = XmlUtils.xPathStringSearch(document, "/sqr:SubscriptionQueryResults/qrm:QueryResultsMetadata/iad:InformationAccessDenial/iad:InformationAccessDenialReasonText");
			subscriptionQueryResponse.setQueryResultsErrorText(queryResultsErrorText);
			
			String systemName = XmlUtils.xPathStringSearch(document, "/sqr:SubscriptionQueryResults/qrm:QueryResultsMetadata/iad:InformationAccessDenial/iad:InformationAccessDenyingSystemNameText");
			subscriptionQueryResponse.setSystemName(systemName);
		}	
		
		String subscriptionErrorText = XmlUtils.xPathStringSearch(document, "/sqr:SubscriptionQueryResults/qrm:QueryResultsMetadata/qrer:QueryRequestError/qrer:ErrorText");
	
		if (StringUtils.isNotBlank(subscriptionErrorText))
		{
			subscriptionQueryResponse.setQueryResultsErrorIndicator(true);
			subscriptionQueryResponse.setQueryResultsErrorText(subscriptionErrorText);
		}	
		
		String subscriptionQualiferId = XmlUtils.xPathStringSearch(document, "/sqr:SubscriptionQueryResults/sqr-ext:SubscriptionQueryResult/sqr-ext:Subscription/sqr-ext:SubscriptionQualifierIdentification/nc:IdentificationID");
		
		if (StringUtils.isNotBlank(subscriptionQualiferId))
		{
			subscriptionQueryResponse.setSubscriptionQualifierId(subscriptionQualiferId);
		}

		String fbiSubscriptionId = XmlUtils.xPathStringSearch(document, "/sqr:SubscriptionQueryResults/sqr-ext:SubscriptionQueryResult/sqr-ext:Subscription/sqr-ext:FBISubscription/sqr-ext:SubscriptionFBIIdentification/nc:IdentificationID");
		
		if (StringUtils.isNotBlank(fbiSubscriptionId))
		{
			subscriptionQueryResponse.setFbiSubscriptionId(fbiSubscriptionId);
		}

        return subscriptionQueryResponse;
	}

}
