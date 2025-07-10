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
public class VehicleCrashNotificationProcessorTest {
    
    private static final String EXPECTED_EMAIL_TEXT = "The following individual, to who, you are subscribed, was involved in a vehicle crash. Details outlined below :\n"
													+"<br/>\n"
													+"Crash Location: HighwayFullText<br/>\n"
													+"Crash Date/Time: 2017-06-22 15:08:15<br/>\n"
													+"<br/>\n"
													+"Subject Name: Doe, Jane<br/>\n"
													+"Subject DOB:1955-12-25<br/>\n"
													+"Subject Role: Driver<br/>\n"
													+"Agency: Maine Auburn Police Department\n"
													+"Officer: John V. Matthews<br/>\n"
													+"Citation #:CitationNumber <br/>\n"
													+"If you require more information, please utilize the Federated Query Tool - https://ojbc.maine.gov/ojb-web-portal/portal/index or call Maine State Police";
    
    private static final Log log = LogFactory.getLog(VehicleCrashNotificationProcessorTest.class);
	
	@Autowired
    private VehicleCrashNotificationProcessor unit;
	
    @Test
    public void testCreateNotificationEmail() throws Exception {

        EmailNotification email = new EmailNotification();
        email.addToAddressee("po1@localhost");
        email.setSubjectName("offenderName");
        email.setSubscribingSystemIdentifier("someSystem");
        email.setSubscriptionCategoryCode("default");
        email.setNotificationRequest(new VehicleCrashNotificationRequest(getNotificationMessage()));
        
        Exchange e = new DefaultExchange( new DefaultCamelContext() );
        Message inMessage = e.getIn();
        inMessage.setHeader("notificationTopic", "incident");
        inMessage.setBody(email);

        unit.createNotificationEmail(e);
        
        String receivedEmailBody = (String) e.getMessage().getBody();
        
        log.info("Recieved email body: "  + receivedEmailBody);
        
        assertEquals(EXPECTED_EMAIL_TEXT, receivedEmailBody);
        assertEquals("po1@localhost", e.getMessage().getHeader(NotificationConstants.HEADER_TO));
        assertEquals("someSystem", e.getMessage().getHeader(NotificationConstants.HEADER_SUBSCRIBING_SYSTEM_IDENTIFIER));
        
    }
    
    private Document getNotificationMessage() throws Exception {
        
        File inputFile = new File("src/test/resources/xmlInstances/vehicleCrash/VehicleCrash_NotificationMessage.xml");

        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
        Document document = docBuilder.parse(inputFile);
        
        return document;
    }

}
