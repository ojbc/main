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
package org.ojbc.processor.subscription.unsubscribe;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.ojbc.test.util.XmlTestUtils;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.subscription.SubscriptionNotificationDocumentBuilderUtils;
import org.ojbc.util.xml.subscription.Unsubscription;
import org.w3c.dom.Document;

public class UnsubscriptionMessageProcessorTest {
	
	@Test	
	public void testCreateUnsubscriptionMessage() throws Exception{
		
		Unsubscription unsubscription = new Unsubscription("123456", "topic", "CI", null, null, null, null);
		
		Document unsubscribeDoc = SubscriptionNotificationDocumentBuilderUtils.createUnubscriptionRequest(unsubscription);		
		
		Document expectedUnsubDoc = XmlUtils.parseFileToDocument(new File("src/test/resources/xml/output/unsubscribe/Unsubscribe.xml"));
		XmlTestUtils.compareDocuments(unsubscribeDoc, expectedUnsubDoc);

	}
	
}
