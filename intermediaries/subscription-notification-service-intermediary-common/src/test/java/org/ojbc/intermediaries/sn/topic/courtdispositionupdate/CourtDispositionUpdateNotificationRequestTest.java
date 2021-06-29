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
package org.ojbc.intermediaries.sn.topic.courtdispositionupdate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.Message;
import org.junit.Test;
import org.mockito.Mockito;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class CourtDispositionUpdateNotificationRequestTest {

	@Test
	public void testCourtDispositionUpdateNotificationRequestTest() throws Exception {
		
		Message message = Mockito.mock(Message.class);
		
		Mockito.when(message.getBody(Document.class)).thenReturn(getMessageBody());
		
		CourtDispositionUpdateNotificationRequest courtDispositionUpdateNotificationRequest = new CourtDispositionUpdateNotificationRequest(message);		
		
		assertFalse(courtDispositionUpdateNotificationRequest.isNotificationEventDateInclusiveOfTime());
		
		//TODO: Update these asssertions after xpaths are fixed.
		//assertEquals(courtDispositionUpdateNotificationRequest.getNotificationEventDate().toString("yyyy-MM-dd"), ""));
		
		//assertEquals(courtDispositionUpdateNotificationRequest.getNotifyingAgencyName(), ""));
		
		assertEquals(courtDispositionUpdateNotificationRequest.getNotificationEventIdentifier(), "123456A");

		assertEquals(courtDispositionUpdateNotificationRequest.getPersonFirstName(), "Walter");
		assertEquals(courtDispositionUpdateNotificationRequest.getPersonLastName(), "White");
		
		assertEquals(courtDispositionUpdateNotificationRequest.getSubjectIdentifiers().size(), 4);
		
		assertEquals(courtDispositionUpdateNotificationRequest.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), "Walter");
		assertEquals(courtDispositionUpdateNotificationRequest.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), "White");
		assertEquals(courtDispositionUpdateNotificationRequest.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), "1977-12-17");
		assertEquals(courtDispositionUpdateNotificationRequest.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID), "A9999999");
		
        assertEquals(courtDispositionUpdateNotificationRequest.getTopic(), "{http://ojbc.org/wsn/topics}:person/CourtDispositionUpdate");
								
	}

	private Document getMessageBody() throws Exception {
		
		File inputFile = new File("src/test/resources/xmlInstances/courtDispositionUpdate/Court_Disposition_Update_NotificationMessage_Sample.xml");

		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		Document document = docBuilder.parse(inputFile);
		
		//Get the root element and strip off the soap envelope
		Node rootNode = XmlUtils.xPathNodeSearch(document, "//b-2:Notify");
		
		Document rootNodeInNewDocument = docBuilder.newDocument();
		
		rootNodeInNewDocument.appendChild(rootNodeInNewDocument.adoptNode(rootNode.cloneNode(true)));
		
		//XmlUtils.printNode(rootNodeInNewDocument);
		
		return rootNodeInNewDocument;
	}
}
