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
package org.ojbc.intermediaries.sn.topic.vehicleCrash;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.Message;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.mockito.Mockito;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class VehicleCrashNotificationRequestTest {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	@Test
	public void test() throws Exception {
		Message message = Mockito.mock(Message.class);
		
		Mockito.when(message.getBody(Document.class)).thenReturn(getMessageBody());
		
		VehicleCrashNotificationRequest request = new VehicleCrashNotificationRequest(message);
		
		assertThat(request.isNotificationEventDateInclusiveOfTime(), is(true));
		
		DateTime notifiationEventDateTime = request.getNotificationEventDate();
		assertThat(notifiationEventDateTime.toString(DATE_FORMATTER), is("2017-06-22T15:08:15"));
		
		assertThat(request.getNotifyingAgencyName(), is("Auburn Police Department"));
		assertThat(request.getNotificationEventIdentifier(), is("CitationNumber"));
		assertThat(request.getPersonFirstName(), is("Jane"));
		assertThat(request.getPersonLastName(), is("Doe"));
		assertThat(request.getPersonBirthDate(), is("1955-12-25"));
		assertThat(request.getOfficerNames().get(0), is("John V. Matthews"));
		assertThat(request.getOfficerNames().size(), is(1));
		assertThat(request.getSubjectIdentifiers().size(), is(3));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), is("Jane"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), is("Doe"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), is("1955-12-25"));
		assertThat(request.getCrashLocation(), is("HighwayFullText"));
		
		assertThat(request.getTopic(), is("{http://ojbc.org/wsn/topics}:person/vehicleCrash"));
		
		assertThat(request.getPersonNotificationSubjectName(), is("Doe, Jane"));
		
	}

	private Document getMessageBody() throws Exception {
		File inputFile = new File(
				"src/test/resources/xmlInstances/vehicleCrash/VehicleCrash_NotificationMessage.xml");

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
