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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.intermediaries.sn.subscription.UnSubscriptionRequest;

import org.apache.camel.Message;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;

public class IncidentUnSubscriptionRequestTest {
	
	Map<String, String> namespaceUris;
	private IncidentSubscriptionProcessor processor;
	
	@Before
	public void setup() {
		namespaceUris = new HashMap<String, String>();
		namespaceUris.put("wsnb2", "http://docs.oasis-open.org/wsn/b-2");
		namespaceUris.put("um", "http://ojbc.org/IEPD/Exchange/UnsubscriptionMessage/1.0");
		namespaceUris.put("smext", "http://ojbc.org/IEPD/Extensions/Subscription/1.0");
		namespaceUris.put("nc20", "http://niem.gov/niem/niem-core/2.0");
		namespaceUris.put("jxdm41", "http://niem.gov/niem/domains/jxdm/4.1");
		processor = new IncidentSubscriptionProcessor();
	}
	
	@Test
	public void test() throws Exception {
		Message message = Mockito.mock(Message.class);
		
		Mockito.when(message.getBody(Document.class)).thenReturn(getMessageBody());
		
		IncidentUnSubscriptionRequest request = new IncidentUnSubscriptionRequest(message);
		
		assertThat(request.getSubscriptionQualifier(), is("302593"));
		assertThat(request.getTopic(), is("{http://ojbc.org/wsn/topics}:person/incident"));
		assertThat(request.getSubjectIdentifiers().size(), is(4));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), is("John"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), is("Doe"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), is("1980-01-01"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), is("302593"));
	}
	
	@Test
    public void makeIncidentSubscription() throws Exception {
        Message mockInMessage = Mockito.mock(Message.class);
        Mockito.when(mockInMessage.getBody(Document.class)).thenReturn(readXmlFileIntoDocument("subscribeSoapRequest-incident.xml"));
        
        SubscriptionRequest request = processor.makeSubscriptionRequestFromIncomingMessage(mockInMessage);
        
        assertTrue(request instanceof IncidentSubscriptionRequest);
    }
    
    @Test
    public void makeIncidentUnSubscription() throws Exception {
        Message mockInMessage = Mockito.mock(Message.class);
        Mockito.when(mockInMessage.getBody(Document.class)).thenReturn(readXmlFileIntoDocument("unSubscribeSoapRequest-incident.xml"));
        
        UnSubscriptionRequest request = processor.makeUnSubscriptionRequestFromIncomingMessage(mockInMessage);
        
        assertTrue(request instanceof IncidentUnSubscriptionRequest);
    }

	private Document getMessageBody() throws Exception {

		File inputFile = new File("src/test/resources/xmlInstances/unSubscribeSoapRequest-incident.xml");

		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		Document document = docBuilder.parse(inputFile);
		
		return document;
		
	}
	
	private Document readXmlFileIntoDocument(String xmlInstanceFileName) throws Exception {
	    
        File inputFile = new File("src/test/resources/xmlInstances/" + xmlInstanceFileName);

        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
        Document document = docBuilder.parse(inputFile);
        
        return document;        
        
    }
}
