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
package org.ojbc.connectors.subscriptionmanagement.processor;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.util.xml.subscription.Subscription;
import org.ojbc.util.xml.subscription.SubscriptionNotificationDocumentBuilderUtils;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@Service
public class SubscriptionXmlProcessor {

	private static final Log log = LogFactory.getLog(SubscriptionXmlProcessor.class);
	
	public Document createSubscriptionsXML(
			List<Subscription> subscriptions) throws Exception {
		
		Document subscriptionsDocument = OJBCXMLUtils.createDocument();
		Element root = subscriptionsDocument.createElement("SubscriptionRecords");

		subscriptionsDocument.appendChild(root);
		
		for (Subscription subscription : subscriptions)
		{	
			Element wrapper = null;
			Document subscriptionRequest = null;
			
			wrapper = subscriptionsDocument.createElement("Wrapper");
			
			try {
				subscriptionRequest = SubscriptionNotificationDocumentBuilderUtils.createSubscriptionRequest(subscription);

				Node importedNode = subscriptionsDocument.importNode(subscriptionRequest.getDocumentElement(), true);
				wrapper.appendChild(importedNode);
				root.appendChild(wrapper);
	
			} catch (Exception e) {
				log.error("Unable to create subscription: " + subscription);
			}
		}
		
		return subscriptionsDocument;
	}
}
