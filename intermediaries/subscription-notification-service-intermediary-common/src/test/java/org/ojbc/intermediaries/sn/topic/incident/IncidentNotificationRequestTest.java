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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.notification.Offense;
import org.ojbc.util.xml.XmlUtils;

import org.apache.camel.Message;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class IncidentNotificationRequestTest {

	@Test
	public void test() throws Exception {
		Message message = Mockito.mock(Message.class);
		
		Mockito.when(message.getBody(Document.class)).thenReturn(getMessageBody());
		
		IncidentNotificationRequest request = new IncidentNotificationRequest(message);
		
		assertThat(request.isNotificationEventDateInclusiveOfTime(), is(false));
		assertThat(request.getNotificationEventDate().toString("yyyy-MM-dd"), is("2013-10-01"));
		assertThat(request.getNotifyingAgencyName(), is("Montpelier Police Department"));
		assertThat(request.getNotificationEventIdentifier(), is("123457"));
		assertThat(request.getNotifyingAgencyPhoneNumber(), is("8926610093"));
		assertThat(request.getPersonFirstName(), is("John"));
		assertThat(request.getPersonLastName(), is("Doe"));
		assertThat(request.getPersonBirthDate(), is("1980-01-01"));
		assertThat(request.getOfficerNames().get(0), is("Clancy Wiggum"));
		assertThat(request.getOfficerNames().size(), is(1));
		assertThat(request.getSubjectIdentifiers().size(), is(3));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), is("John"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), is("Doe"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), is("1980-01-01"));
		
		assertThat(request.getOffenses().size(), is(2));
		
		Offense offense1 = request.getOffenses().get(0);
		assertThat(offense1.getFbiNdexCode(), is("Driving Under Influence"));
		assertThat(offense1.getOffenseCategoryText(), is("DUI"));
		assertThat(offense1.getOffenseDescriptionText(), is("Driving Under The Influence, First Offense 23 VSA 1201 90D"));
		
		Offense offense2 = request.getOffenses().get(1);
		assertThat(offense2.getFbiNdexCode(), is("Robbery"));
		assertThat(offense2.getOffenseCategoryText(), is("ROB"));
		assertThat(offense2.getOffenseDescriptionText(), is("Robbery"));
		
		List<String> offenseStrings = request.getOffenseStrings();
		assertEquals(2, offenseStrings.size());
		assertEquals("Offense Code: Driving Under Influence<br/>Offense Description: Driving Under The Influence, First Offense 23 VSA 1201 90D<br/>", offenseStrings.get(0));
		
		assertThat(request.getTopic(), is("{http://ojbc.org/wsn/topics}:person/incident"));
		
		assertThat(request.getPersonNotificationSubjectName(), is("Doe, John"));
		
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
