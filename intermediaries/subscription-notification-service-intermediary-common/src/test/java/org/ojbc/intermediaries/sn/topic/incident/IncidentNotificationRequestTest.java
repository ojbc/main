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
package org.ojbc.intermediaries.sn.topic.incident;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.Message;
import org.junit.Test;
import org.mockito.Mockito;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.notification.Offense;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class IncidentNotificationRequestTest {

	@Test
	public void test() throws Exception {
		Message message = Mockito.mock(Message.class);
		
		Mockito.when(message.getBody(Document.class)).thenReturn(getMessageBody());
		
		IncidentNotificationRequest request = new IncidentNotificationRequest(message);
		
		assertEquals(request.isNotificationEventDateInclusiveOfTime(), false);
		assertEquals(request.getNotificationEventDate().toString("yyyy-MM-dd"), "2013-10-01");
		assertEquals(request.getNotifyingAgencyName(), "Montpelier Police Department");
		assertEquals(request.getNotificationEventIdentifier(), "123457");
		assertEquals(request.getNotifyingAgencyPhoneNumber(), "8926610093");
		assertEquals(request.getPersonFirstName(), "John");
		assertEquals(request.getPersonLastName(), "Doe");
		assertEquals(request.getPersonBirthDate(), "1980-01-01");
		assertEquals(request.getOfficerNames().get(0), "Clancy Wiggum");
		assertEquals(request.getOfficerNames().size(), 1);
		assertEquals(request.getSubjectIdentifiers().size(), 3);
		assertEquals(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), "John");
		assertEquals(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), "Doe");
		assertEquals(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), "1980-01-01");
		
		assertEquals(request.getOffenses().size(), 2);
		
		Offense offense1 = request.getOffenses().get(0);
		assertEquals(offense1.getFbiNdexCode(), "Driving Under Influence");
		assertEquals(offense1.getOffenseCategoryText(), "DUI");
		assertEquals(offense1.getOffenseDescriptionText(), "Driving Under The Influence, First Offense 23 VSA 1201 90D");
		
		Offense offense2 = request.getOffenses().get(1);
		assertEquals(offense2.getFbiNdexCode(), "Robbery");
		assertEquals(offense2.getOffenseCategoryText(), "ROB");
		assertEquals(offense2.getOffenseDescriptionText(), "Robbery");
		
		List<String> offenseStrings = request.getOffenseStrings();
		assertEquals(2, offenseStrings.size());
		assertEquals("Offense Code: Driving Under Influence<br/>Offense Description: Driving Under The Influence, First Offense 23 VSA 1201 90D<br/>", offenseStrings.get(0));
		
		assertEquals(request.getTopic(), "{http://ojbc.org/wsn/topics}:person/incident");
		
		assertEquals(request.getPersonNotificationSubjectName(), "Doe, John");
		
	}

	private Document getMessageBody() throws Exception {
		File inputFile = new File(
				"src/test/resources/xmlInstances/notificationSoapRequest-incident.xml");

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
