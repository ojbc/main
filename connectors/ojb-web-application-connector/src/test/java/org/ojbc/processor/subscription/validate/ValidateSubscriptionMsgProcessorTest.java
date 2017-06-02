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
package org.ojbc.processor.subscription.validate;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.w3c.dom.Document;

public class ValidateSubscriptionMsgProcessorTest {

	@Test	
	public void testCreateUnsubscriptionMessage() throws Exception{
		
		Document doc = RequestMessageBuilderUtilities.createValidateSubscriptionRequest("123456", "topic", "CI");
		
		String subscriptionIdentificationId = XmlUtils.xPathStringSearch(doc, 
				"b-2:Validate/svm:SubscriptionValidationMessage/submsg-ext:SubscriptionIdentification/nc:IdentificationID");
		
		assertEquals("123456", subscriptionIdentificationId);	
		
		String topic = XmlUtils.xPathStringSearch(doc, "b-2:Validate/b-2:TopicExpression[@Dialect='http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete']");  
		
		assertEquals("topic", topic);
		
		String subscriptionReasonCode = XmlUtils.xPathStringSearch(doc, 
				"b-2:Validate/svm:SubscriptionValidationMessage/submsg-ext:CriminalSubscriptionReasonCode");
		assertEquals("CI", subscriptionReasonCode);

		Document civilDoc = RequestMessageBuilderUtilities.createValidateSubscriptionRequest("123456", "topic", "I");
		String civilSubscriptionReasonCode = XmlUtils.xPathStringSearch(civilDoc, 
				"b-2:Validate/svm:SubscriptionValidationMessage/submsg-ext:CivilSubscriptionReasonCode");
		assertEquals("I", civilSubscriptionReasonCode);


	}
		
}
