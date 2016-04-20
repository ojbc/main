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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.processor.subscription.unsubscribe;

import java.io.File;
import java.util.List;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.subscription.SubscriptionNotificationDocumentBuilderUtils;
import org.ojbc.util.xml.subscription.Unsubscription;
import org.w3c.dom.Document;

public class UnsubscriptionMessageProcessorTest {
	
	@Before
	public void init(){		
		
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
	}

	@Test	
	public void testCreateUnsubscriptionMessage() throws Exception{
		
		Unsubscription unsubscription = new Unsubscription("123456", "topic", "CI");
		
		Document unsubscribeDoc = SubscriptionNotificationDocumentBuilderUtils.createUnubscriptionRequest(unsubscription);		
		
		Document expectedUnsubDoc = XmlUtils.parseFileToDocument(new File("src/test/resources/xml/output/unsubscribe/Unsubscribe.xml"));
				
		Diff diff = new Diff(expectedUnsubDoc, unsubscribeDoc);	
		
		DetailedDiff detailedDiff = new DetailedDiff(diff);		
		List<Difference> differenceList = detailedDiff.getAllDifferences();
		
		Assert.assertEquals(detailedDiff.toString(), 0, differenceList.size());		
	}
	
}
