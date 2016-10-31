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
package org.ojbc.intermediaries.sn.topic.arrest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;

import org.apache.camel.Message;
import org.junit.Test;
import org.mockito.Mockito;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.w3c.dom.Document;

public class ArrestUnSubscriptionRequestTest {

	@Test
	public void test() throws Exception {
		Message message = Mockito.mock(Message.class);
		
		Mockito.when(message.getBody(Document.class)).thenReturn(getMessageBody());
		
		ArrestUnSubscriptionRequest request = new ArrestUnSubscriptionRequest(message);
		
		assertThat(request.getSubscriptionQualifier(), is("1234578"));
		Assert.assertNotNull(request.getEmailAddresses());
		assertThat(request.getTopic(), is("{http://ojbc.org/wsn/topics}:person/arrest"));
		assertThat(request.getSubjectIdentifiers().size(), is(5));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), is("A9999999"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), is("1234578"));
		assertNull(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME));
		assertNull(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME));
		assertNull(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH));
		
	}

	private Document getMessageBody() throws Exception {
		
		File inputFile = new File("src/test/resources/xmlInstances/unSubscribeSoapRequest.xml");

		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		Document document = docBuilder.parse(inputFile);
		
		return document;
	}
}
