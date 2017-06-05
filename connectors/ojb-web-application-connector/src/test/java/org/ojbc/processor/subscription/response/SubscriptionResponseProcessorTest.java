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
package org.ojbc.processor.subscription.response;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.ojbc.processor.subscription.subscribe.SubscriptionResponseProcessor;
import org.ojbc.web.model.subscription.response.SubscriptionAccessDenialResponse;
import org.ojbc.web.model.subscription.response.SubscriptionInvalidEmailResponse;
import org.ojbc.web.model.subscription.response.SubscriptionInvalidSecurityTokenResponse;
import org.ojbc.web.model.subscription.response.SubscriptionRequestErrorResponse;
import org.ojbc.web.model.subscription.response.SubscriptionSuccessResponse;
import org.ojbc.web.model.subscription.response.UnsubscriptionAccessDenialResponse;
import org.ojbc.web.model.subscription.response.common.SubscriptionResponse;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class SubscriptionResponseProcessorTest {
		
	@Test
	public void testSuccessResponse() throws Exception{
						
		Document doc = getDocument("src/test/resources/xml/subscriptionResponse/Subscription_Success_Response.xml");
		
		SubscriptionResponseProcessor subResponseProcessor = new SubscriptionResponseProcessor();
		
		List<SubscriptionResponse> subResponseList = subResponseProcessor.processResponse(doc);
		
		Assert.assertEquals(1, subResponseList.size());
				
		SubscriptionSuccessResponse subSuccessResponse = (SubscriptionSuccessResponse)subResponseList.get(0);
		
		Assert.assertEquals("http://www.ojbc.org/SubcribeNotify/", subSuccessResponse.getSubReferenceAddress());
		
		Assert.assertEquals("2014-06-23T12:00:00", subSuccessResponse.getSubReferenceCurrentTime());
		
		Assert.assertEquals(true, subSuccessResponse.isSubscriptionCreatedIndicator());
	}
	
	@Test
	public void testRequestErrorResponse() throws Exception{
		
		Document doc = getDocument("src/test/resources/xml/subscriptionResponse/Subscription_Error_Response.xml");
		
		SubscriptionResponseProcessor subResponseProcessor = new SubscriptionResponseProcessor();
		
		List<SubscriptionResponse> subResponseList = subResponseProcessor.processResponse(doc);
		
		Assert.assertEquals(1, subResponseList.size());	
		
		SubscriptionRequestErrorResponse subReqErrRep = (SubscriptionRequestErrorResponse)subResponseList.get(0);
		
		Assert.assertEquals("subscription doesn't contain a start date", subReqErrRep.getRequestErrorTxt());
		
		Assert.assertEquals("Subscription Notification", subReqErrRep.getRequestErrorSystemName());
		
		Assert.assertEquals("2014-06-23T12:00:00", subReqErrRep.getTimestamp());
	}
	
	
	@Test
	public void testInvalidTokenResponse() throws Exception{
		
		Document doc = getDocument("src/test/resources/xml/subscriptionResponse/Subscription_Invalid_Token_Response.xml");
		
		SubscriptionResponseProcessor subResponseProcessor = new SubscriptionResponseProcessor();
		
		List<SubscriptionResponse> subResponseList = subResponseProcessor.processResponse(doc);
		
		Assert.assertEquals(1, subResponseList.size());		
		
		SubscriptionInvalidSecurityTokenResponse invalidTokenResponse = (SubscriptionInvalidSecurityTokenResponse)subResponseList.get(0);
		
		Assert.assertEquals(true, invalidTokenResponse.isInvalidSecurityTokenIndicator());
		
		Assert.assertEquals("Token is missing required information: Federation Identifier.", invalidTokenResponse.getInvalidSecurityTokenDescription());
		
		Assert.assertEquals("2014-06-23T12:00:00", invalidTokenResponse.getTimestamp());		
	}
	
	
	@Test
	public void testInvalidEmailResponse() throws Exception{
		
		Document doc = getDocument("src/test/resources/xml/subscriptionResponse/Subscription_Invalid_Email_Response.xml");
		
		SubscriptionResponseProcessor subResponseProcessor = new SubscriptionResponseProcessor();
		
		List<SubscriptionResponse> subResponseList = subResponseProcessor.processResponse(doc);
		
		Assert.assertEquals(1, subResponseList.size());		
		
		SubscriptionInvalidEmailResponse invalidEmailResponse = (SubscriptionInvalidEmailResponse) subResponseList.get(0);
		
		List<String> invalidEmailList = invalidEmailResponse.getInvalidEmailList();
		
		String[] aExpectedInvalidEmails = {"andrew@gmail.com", "andrew@yahoo.com"};
		
		Assert.assertArrayEquals(aExpectedInvalidEmails, invalidEmailList.toArray());
		
		Assert.assertEquals("2014-06-23T12:00:00", invalidEmailResponse.getTimestamp());
	}
	
	
	@Test
	public void testAccessDenialResponse() throws Exception{

		Document doc = getDocument("src/test/resources/xml/subscriptionResponse/Subscription_Access_Denial_Response.xml");
		
		SubscriptionResponseProcessor subResponseProcessor = new SubscriptionResponseProcessor();
		
		List<SubscriptionResponse> subResponseList = subResponseProcessor.processResponse(doc);
		
		Assert.assertEquals(1, subResponseList.size());		
		
		SubscriptionAccessDenialResponse subAccessDenyResp = (SubscriptionAccessDenialResponse)subResponseList.get(0);
		
		Assert.assertEquals(true, subAccessDenyResp.isAccessDenialIndicator());
		
		Assert.assertEquals("System", subAccessDenyResp.getAccessDenyingSystemName());
		
		Assert.assertEquals("The user is not privileged to create subscriptions", subAccessDenyResp.getAccessDenyingReason());
		
		Assert.assertEquals("2014-06-23T12:00:00", subAccessDenyResp.getTimestamp());
	}
				
	@Test
	public void testUnsubscribeResponse() throws Exception{
		
		Document doc = getDocument("src/test/resources/xml/subscriptionResponse/Unsubscription_Access_Denial_Response.xml");
		
		SubscriptionResponseProcessor subResponseProcessor = new SubscriptionResponseProcessor();
		
		List<SubscriptionResponse> subResponseList = subResponseProcessor.processResponse(doc);
		
		Assert.assertEquals(1, subResponseList.size());
		
		UnsubscriptionAccessDenialResponse unsubDenyAccessResp = (UnsubscriptionAccessDenialResponse) subResponseList.get(0);
		
		Assert.assertEquals("The user is not privileged to delete this subscription", unsubDenyAccessResp.getAccessDenyingReason());
		
		Assert.assertEquals(true, unsubDenyAccessResp.isAccessDenialIndicator());
		
		Assert.assertEquals("System Name", unsubDenyAccessResp.getAccessDenyingSystemName());
		
		Assert.assertEquals("2014-06-23T12:00:00", unsubDenyAccessResp.getTimestamp());		
	}
	
	
	private Document getDocument(String fileClassPath) throws SAXException, IOException, ParserConfigurationException{
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		
		Document doc = dBuilder.parse(fileClassPath);		
		
		return doc;
	}

}


