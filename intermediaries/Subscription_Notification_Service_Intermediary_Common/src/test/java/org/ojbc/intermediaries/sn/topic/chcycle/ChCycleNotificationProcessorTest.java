package org.ojbc.intermediaries.sn.topic.chcycle;

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
public class ChCycleNotificationProcessorTest {

    private static final String EXPECTED_EMAIL_TEXT = "Springfield PD requested an Arrest Tracking Number(ATN) for Maggie Simpson on 2014-11-11";

    private static final Log log = LogFactory.getLog(ChCycleNotificationProcessorTest.class);

    @Autowired
    private ChCycleNotificationProcessor chCycleNotifProcessor;

    @Test
    public void testCreateNotificationEmail() throws Exception {

        EmailNotification email = new EmailNotification();
        email.addToAddressee("po1@localhost");
        email.setSubjectName("offenderName");
        email.setSubscribingSystemIdentifier("{http://ojbc.org}ProbationChCyleTrackingID");
        email.setNotificationRequest(new ChCycleNotificationRequest(getNotificationMessage()));
        
        Exchange e = new DefaultExchange((CamelContext) null);
        Message inMessage = e.getIn();
        inMessage.setHeader("notificationTopic", "criminalHistoryCycleTrackingIdentifierAssignment");
        inMessage.setBody(email);

        chCycleNotifProcessor.createNotificationEmail(e);
        
        assertEquals(EXPECTED_EMAIL_TEXT, e.getOut().getBody());
        assertEquals("po1@localhost", e.getOut().getHeader(NotificationConstants.HEADER_TO));
        
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
