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
package org.ojbc.processor.subscription.subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.util.xml.subscription.Subscription;
import org.ojbc.util.xml.subscription.SubscriptionNotificationDocumentBuilderUtils;
import org.w3c.dom.Document;

public class SubscribeMessageTest {
	
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");
	
	@Before
	public void setup(){		
		
		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	XMLUnit.setXSLTVersion("2.0");    	
	}	
	
	
	@Test
	public void subscribeMessageTest() throws Exception{
		
		Subscription subscription = getSampleSubscriptionPojo();
		
		Document generatedSubscriptinDoc = SubscriptionNotificationDocumentBuilderUtils.createSubscriptionRequest(subscription);	
		
		String subQualId = XmlUtils.xPathStringSearch(generatedSubscriptinDoc, "//submsg-ext:SubscriptionQualifierIdentification/nc:IdentificationID");
		
		String sGeneratedSubscriptionDoc = XmlUtils.getStringFromNode(generatedSubscriptinDoc);
						
		String sExpectedXmlSubDoc = XmlUtils.getRootNodeAsString("src/test/resources/xml/subscriptionRequest/Arrest_Subscription_Document.xml");				
		sExpectedXmlSubDoc = sExpectedXmlSubDoc.replace("@SUB_QUAL_ID@", subQualId);
						
		Diff diff = new Diff(sExpectedXmlSubDoc, sGeneratedSubscriptionDoc);		
		DetailedDiff detailDiff = new DetailedDiff(diff);		
		List<Difference> diffList = detailDiff.getAllDifferences();
		int diffCount = diffList.size();
						
		Assert.assertEquals(detailDiff.toString(), 0, diffCount);
	}
	
	
	private Subscription getSampleSubscriptionPojo() throws ParseException{
		
		Subscription subscription = new Subscription();

		subscription.setTopic("topics:person/arrest");		
		subscription.setCaseId("0123ABC");
		
		Date dob = SDF.parse("1972-08-02");		
		subscription.setDateOfBirth(dob);
		
		subscription.setEmailList(Arrays.asList("testimap@locahost", "email@local.gov"));
		subscription.setFbiId("123456789");
		subscription.setFirstName("John");
		subscription.setLastName("Doe");
		subscription.setStateId("A398118900");

		Date subStartDate = SDF.parse("2014-06-20");
		subscription.setSubscriptionStartDate(subStartDate);
		
		Date subEndDate = SDF.parse("2015-06-20");		
		subscription.setSubscriptionEndDate(subEndDate);
		
		subscription.setSubscriptionPurpose("CI");		
		subscription.setSystemId("{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB");
		
		return subscription;
	}

}
