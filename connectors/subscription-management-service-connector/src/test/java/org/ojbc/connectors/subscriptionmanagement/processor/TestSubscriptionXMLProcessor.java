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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.subscription.Subscription;
import org.w3c.dom.Document;

public class TestSubscriptionXMLProcessor {

	@Test
	public void testProcessSubscriptionEntries() throws Exception
	{
		SubscriptionCSVProcessor subscriptionCSVProcessor = new SubscriptionCSVProcessor();
		subscriptionCSVProcessor.setTopic("topics:person/incident");
		
		String csvExtractLine="John,Doe,1961-01-02,email@domain.com";
		subscriptionCSVProcessor.processCsvEntry(csvExtractLine);

		csvExtractLine="Jane,Doe,1981-01-02,email2@domain.com";
		subscriptionCSVProcessor.processCsvEntry(csvExtractLine);
		
		List<Subscription> subscriptions = subscriptionCSVProcessor.getSubscriptions();
		
		SubscriptionXmlProcessor subscriptionXmlProcessor = new SubscriptionXmlProcessor();
		
		Document subscriptionsDocument =  subscriptionXmlProcessor.createSubscriptionsXML(subscriptions);
		
		XmlUtils.printNode(subscriptionsDocument);
		
		assertEquals(2, XmlUtils.xPathNodeListSearch(subscriptionsDocument, "/SubscriptionRecords/Wrapper/b-2:Subscribe").getLength());
	}

	
}
