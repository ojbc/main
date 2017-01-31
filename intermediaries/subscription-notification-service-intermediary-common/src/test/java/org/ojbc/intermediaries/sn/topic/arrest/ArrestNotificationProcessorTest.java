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

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ojbc.intermediaries.sn.notification.EmailNotification;
import org.ojbc.intermediaries.sn.notification.NotificationConstants;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-subscription.xml",
		})
@DirtiesContext
public class ArrestNotificationProcessorTest {
    
    private static final Log log = LogFactory.getLog(ArrestNotificationProcessorTest.class);
	
	@Autowired
    private ArrestNotificationProcessor arrestNotificationProcessor;
	
    @Test
    public void testCreateNotificationEmail() throws Exception {

        String expectedEmailBody = "Booking Name: Simpson, Homer<br/>Name in Parole case information: offenderName<br/>\n" +
        "SID: A9999999<br/>\n" +
        "DATE/TIME OF BOOKING: 2013-09-06<br/>\n" +
        "ARRESTING AGENCY: Honolulu PD<br/>ATTORNEY GENERAL CASE: false<br/>\n" +
        "ARREST CHARGES:<br/>\n" +
        "Description: Assault Severity: very severe, ARN: I-04679<br/>\n" +
        "<br/><br/>Positively identified by fingerprint.";

        EmailNotification email = new EmailNotification();
        email.addToAddressee("po1@localhost");
        email.setSubjectName("offenderName");
        email.setSubscribingSystemIdentifier("{http://demostate.gov/SystemNames/1.0}SystemA");
        email.setSubscriptionCategoryCode("default");
        email.setNotificationRequest(new ArrestNotificationRequest(getNotificationMessage()));
        
        Exchange e = new DefaultExchange((CamelContext) null);
        Message inMessage = e.getIn();
        inMessage.setHeader("notificationTopic", "arrest");
        inMessage.setBody(email);

        arrestNotificationProcessor.createNotificationEmail(e);
        
        Object receivedEmailBody = e.getOut().getBody();
        assertEquals(expectedEmailBody, receivedEmailBody);
        assertEquals("po1@localhost", e.getOut().getHeader(NotificationConstants.HEADER_TO));
        assertEquals("{http://demostate.gov/SystemNames/1.0}SystemA", e.getOut().getHeader(NotificationConstants.HEADER_SUBSCRIBING_SYSTEM_IDENTIFIER));
    }
    
    @Test
    public void testCreateNotificationWithSpecifiedEmailTemplate() throws Exception {

        String expectedEmailBody = "An event occurred for [Subject offenderName]. Contact their employer.";

        EmailNotification email = new EmailNotification();
        email.addToAddressee("po1@localhost");
        email.setSubjectName("offenderName");
        email.setSubscribingSystemIdentifier("{http://demostate.gov/SystemNames/1.0}SystemA");
        email.setSubscriptionCategoryCode("I");
        email.setNotificationRequest(new ArrestNotificationRequest(getNotificationMessage()));
        
        Exchange e = new DefaultExchange((CamelContext) null);
        Message inMessage = e.getIn();
        inMessage.setHeader("notificationTopic", "arrest");
        inMessage.setBody(email);

        arrestNotificationProcessor.createNotificationEmail(e);
        
        Object receivedEmailBody = e.getOut().getBody();
        assertEquals(expectedEmailBody, receivedEmailBody);
        assertEquals("po1@localhost", e.getOut().getHeader(NotificationConstants.HEADER_TO));
        
    }    
    
    @Test
    public void testCreateNotificationEmailManualSubscription() throws Exception {

        String expectedEmailBody = "Booking Name: Simpson, Homer<br/>Name in Subscription: offenderName<br/>\n" +
        "SID: A9999999<br/>\n" +
        "DATE/TIME OF BOOKING: 2013-09-06<br/>\n" +
        "ARRESTING AGENCY: Honolulu PD<br/>ATTORNEY GENERAL CASE: false<br/>\n" +
        "ARREST CHARGES:<br/>\n" +
        "Description: Assault Severity: very severe, ARN: I-04679<br/>\n" +
        "<br/><br/>Positively identified by fingerprint.";
        
        EmailNotification email = new EmailNotification();
        email.addToAddressee("po1@localhost");
        email.setSubjectName("offenderName");
        email.setSubscribingSystemIdentifier("{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB");
        email.setSubscriptionCategoryCode("default");
        email.setNotificationRequest(new ArrestNotificationRequest(getNotificationMessage()));

        Exchange e = new DefaultExchange((CamelContext) null);
        Message inMessage = e.getIn();
        inMessage.setHeader("notificationTopic", "arrest");
        inMessage.setBody(email);

        arrestNotificationProcessor.createNotificationEmail(e);
        
        assertEquals(expectedEmailBody, e.getOut().getBody());
        assertEquals("po1@localhost", e.getOut().getHeader(NotificationConstants.HEADER_TO));
        
    }  
    
    @Test
    public void testCreateNotificationEmailWithCC() throws Exception {

        String expectedEmailBody = "Booking Name: Simpson, Homer<br/>Name in Parole case information: offenderName<br/>\n" +
        "SID: A9999999<br/>\n" +
        "DATE/TIME OF BOOKING: 2013-09-06<br/>\n" +
        "ARRESTING AGENCY: Honolulu PD<br/>ATTORNEY GENERAL CASE: false<br/>\n" +
        "ARREST CHARGES:<br/>\n" +
        "Description: Assault Severity: very severe, ARN: I-04679<br/>\n" +
        "<br/><br/>Positively identified by fingerprint.";
        
        EmailNotification email = new EmailNotification();
        email.addToAddressee("po1@localhost");
        email.setSubjectName("offenderName");
        email.setSubscribingSystemIdentifier("{http://demostate.gov/SystemNames/1.0}SystemA");
        email.setSubscriptionCategoryCode("default");
        email.setNotificationRequest(new ArrestNotificationRequest(getNotificationMessage()));

        Exchange e = new DefaultExchange((CamelContext) null);
        Message inMessage = e.getIn();
        inMessage.setHeader("notificationTopic", "arrest");
        inMessage.setBody(email);
        
        arrestNotificationProcessor.createNotificationEmail(e);
        
        assertEquals(expectedEmailBody, e.getOut().getBody());
        assertEquals("po1@localhost", e.getOut().getHeader(NotificationConstants.HEADER_TO));
        assertEquals("sup@localhost", e.getOut().getHeader(NotificationConstants.HEADER_CC));
        
    }
    
	private Document getNotificationMessage() throws Exception {
		
		File inputFile = new File("src/test/resources/xmlInstances/notificationMessage.xml");

		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		Document document = docBuilder.parse(inputFile);
		
		return document;
	}

}
