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
package org.ojbc.processor.subscription.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.model.person.query.DetailsRequest;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class SubscriptionQueryRequestTest {

	
	@Test
	public void subscriptionQueryRequest() throws Exception {
		
    	//create and populate a person search request POJO
		DetailsRequest subscriptionQueryRequest = new DetailsRequest();
		
		subscriptionQueryRequest.setIdentificationID("62720");
		subscriptionQueryRequest.setIdentificationSourceText("subscriptions");
		subscriptionQueryRequest.setAdmin(true);

		Document subscriptionQueryDocument = RequestMessageBuilderUtilities.createSubscriptionQueryRequest(subscriptionQueryRequest);
		
		XmlUtils.printNode(subscriptionQueryDocument);
		
		assertNotNull(subscriptionQueryDocument);
		
        Node rootResultsNode = XmlUtils.xPathNodeSearch(subscriptionQueryDocument, "/sqreq:SubscriptionQueryRequest");
        assertNotNull(rootResultsNode);        
        
        String identificationId = XmlUtils.xPathStringSearch(subscriptionQueryDocument, "/sqreq:SubscriptionQueryRequest/sqreq-ext:SubscriptionIdentification/nc:IdentificationID");
        assertEquals("62720", identificationId);
        
        String identificationSourceText = XmlUtils.xPathStringSearch(subscriptionQueryDocument, "/sqreq:SubscriptionQueryRequest/sqreq-ext:SubscriptionIdentification/nc:IdentificationSourceText");
        assertEquals("subscriptions", identificationSourceText);

        String adminQueryRequestIndicator = XmlUtils.xPathStringSearch(subscriptionQueryDocument, "/sqreq:SubscriptionQueryRequest/sqreq-ext:AdminQueryRequestIndicator");
        assertEquals("true", adminQueryRequestIndicator);
        
	}	
	
}
