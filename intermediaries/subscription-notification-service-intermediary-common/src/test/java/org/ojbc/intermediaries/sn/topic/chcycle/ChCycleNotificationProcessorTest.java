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
package org.ojbc.intermediaries.sn.topic.chcycle;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.intermediaries.sn.notification.EmailNotification;
import org.ojbc.intermediaries.sn.notification.NotificationConstants;
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
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
		"classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"
		})
@DirtiesContext
public class ChCycleNotificationProcessorTest {

    private static final String EXPECTED_EMAIL_TEXT = "Springfield PD requested an Arrest Tracking Number(ATN) for Maggie Simpson on 2014-11-11";

    @SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(ChCycleNotificationProcessorTest.class);

    @Autowired
    private ChCycleNotificationProcessor chCycleNotifProcessor;

    @Test
    public void testCreateNotificationEmail() throws Exception {

        EmailNotification email = new EmailNotification();
        email.addToAddressee("po1@localhost");
        email.setSubjectName("offenderName");
        email.setSubscribingSystemIdentifier("{http://ojbc.org}ProbationChCyleTrackingID");
        email.setSubscriptionCategoryCode("default");
        email.setNotificationRequest(new ChCycleNotificationRequest(getNotificationMessage()));
        
        Exchange e = new DefaultExchange( new DefaultCamelContext() );
        Message inMessage = e.getIn();
        inMessage.setHeader("notificationTopic", "criminalHistoryCycleTrackingIdentifierAssignment");
        inMessage.setBody(email);

        chCycleNotifProcessor.createNotificationEmail(e);
        
        assertEquals(EXPECTED_EMAIL_TEXT, e.getMessage().getBody());
        assertEquals("po1@localhost", e.getMessage().getHeader(NotificationConstants.HEADER_TO));
        assertEquals("{http://ojbc.org}ProbationChCyleTrackingID", e.getMessage().getHeader(NotificationConstants.HEADER_SUBSCRIBING_SYSTEM_IDENTIFIER));
        
    }
    
    private Document getNotificationMessage() throws Exception {

        File inputFile = new File("src/test/resources/xmlInstances/notificationMessage-crimHistCycleUpdate.xml");

        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
        Document document = docBuilder.parse(inputFile);

        return document;
    }

}
