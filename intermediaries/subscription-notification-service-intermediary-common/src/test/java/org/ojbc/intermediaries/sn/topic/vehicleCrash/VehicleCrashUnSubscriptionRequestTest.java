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
import static org.junit.Assert.assertTrue;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.Message;
import org.junit.Test;
import org.mockito.Mockito;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.intermediaries.sn.subscription.UnSubscriptionRequest;
import org.w3c.dom.Document;

public class VehicleCrashUnSubscriptionRequestTest {
	
	private VehicleCrashSubscriptionProcessor processor = new VehicleCrashSubscriptionProcessor();
	
	@Test
	public void test() throws Exception {
		Message message = Mockito.mock(Message.class);
		
		Mockito.when(message.getBody(Document.class)).thenReturn(getMessageBody());
		
		VehicleCrashUnSubscriptionRequest request = new VehicleCrashUnSubscriptionRequest(message);
		
		assertThat(request.getSubscriptionQualifier(), is("302593"));
		assertThat(request.getTopic(), is("{http://ojbc.org/wsn/topics}:person/vehicleCrash"));
		assertThat(request.getSubjectIdentifiers().size(), is(4));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.FIRST_NAME), is("John"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.LAST_NAME), is("Doe"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.DATE_OF_BIRTH), is("1980-01-01"));
		assertThat(request.getSubjectIdentifiers().get(SubscriptionNotificationConstants.SUBSCRIPTION_QUALIFIER), is("302593"));
	}
	
	@Test
    public void makeVehicleCrashSubscription() throws Exception {
        Message mockInMessage = Mockito.mock(Message.class);
        Mockito.when(mockInMessage.getBody(Document.class)).thenReturn(readXmlFileIntoDocument("subscribeSoapRequest-vehicleCrash.xml"));
        
        SubscriptionRequest request = processor.makeSubscriptionRequestFromIncomingMessage(mockInMessage);
        
        assertTrue(request instanceof VehicleCrashSubscriptionRequest);
    }
    
    @Test
    public void makeVehicleCrashUnSubscription() throws Exception {
        Message mockInMessage = Mockito.mock(Message.class);
        Mockito.when(mockInMessage.getBody(Document.class)).thenReturn(readXmlFileIntoDocument("unSubscribeSoapRequest-vehicleCrash.xml"));
        
        UnSubscriptionRequest request = processor.makeUnSubscriptionRequestFromIncomingMessage(mockInMessage);
        
        assertTrue(request instanceof VehicleCrashUnSubscriptionRequest);
    }

	private Document getMessageBody() throws Exception {

		File inputFile = new File("src/test/resources/xmlInstances/vehicleCrash/unSubscribeSoapRequest-vehicleCrash.xml");

		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		Document document = docBuilder.parse(inputFile);
		
		return document;
		
	}
	
	private Document readXmlFileIntoDocument(String xmlInstanceFileName) throws Exception {
	    
        File inputFile = new File("src/test/resources/xmlInstances/vehicleCrash/" + xmlInstanceFileName);

        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
        Document document = docBuilder.parse(inputFile);
        
        return document;        
        
    }
}
