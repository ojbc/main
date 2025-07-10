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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.notification.EmailNotification;
import org.ojbc.intermediaries.sn.notification.NotificationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.w3c.dom.Document;

@CamelSpringTest
@SpringJUnitConfig(locations={
		"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-subscription.xml",
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
		"classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"
		})
@DirtiesContext
public class IncidentNotificationProcessorTest {
    
    private static final String EXPECTED_EMAIL_TEXT = "An individual for whom you have subscribed to be notified was involved in an incident documented by the following law enforcement agency:<br/>\n" +
    "Montpelier Police Department<br/>\n" +
    "Incident Date/Time: 2024-07-01 09:55:34<br/>\n" +
    "Incident Report #:123457 <br/>\n" +
    "Subject Name: Doe, John<br/>\n" +
    "Subject date of birth:1980-01-01<br/>\n" +
    "Role: Witness<br/>\n" +
    "Offense Code: Driving Under Influence<br/>Offense Description: Driving Under The Influence, First Offense 23 VSA 1201 90D<br/><br/>\n" +
    "Offense Code: ROB<br/>Offense Description: Robbery<br/><br/>\n" +
    "Offense Code: Driving Under Influence<br/>Offense Description: DUI<br/><br/>\n" +
    "To follow up on this incident, please call Montpelier Police Department.";
    
    @SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(IncidentNotificationProcessorTest.class);
	
	@Autowired
    private IncidentNotificationProcessor unit;
	
    @Test
    public void testCreateNotificationEmail() throws Exception {

        EmailNotification email = new EmailNotification();
        email.addToAddressee("po1@localhost");
        email.setSubjectName("offenderName");
        email.setSubscribingSystemIdentifier("{http://hijis.hawaii.gov/ParoleCase/1.0}HawaiiParolingAuthority");
        email.setSubscriptionCategoryCode("default");
        email.setNotificationRequest(new IncidentNotificationRequest(getNotificationMessage()));
        
        Exchange e = new DefaultExchange(new DefaultCamelContext());
        Message inMessage = e.getIn();
        inMessage.setHeader("notificationTopic", "incident");
        inMessage.setBody(email);

        unit.createNotificationEmail(e);
        
        String receivedEmailBody = (String) e.getMessage().getBody();
        assertEquals(EXPECTED_EMAIL_TEXT, receivedEmailBody);
        assertEquals("po1@localhost", e.getMessage().getHeader(NotificationConstants.HEADER_TO));
        assertEquals("{http://hijis.hawaii.gov/ParoleCase/1.0}HawaiiParolingAuthority", e.getMessage().getHeader(NotificationConstants.HEADER_SUBSCRIBING_SYSTEM_IDENTIFIER));
        
    }
    
    private Document getNotificationMessage() throws Exception {
        
        File inputFile = new File("src/test/resources/xmlInstances/notificationMessage-incidentFutureNotificationDate.xml");

        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
        Document document = docBuilder.parse(inputFile);
        
        return document;
    }

}
