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
package org.ojbc.intermediaries.sn.notification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultExchange;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.intermediaries.sn.notification.filter.NotificationFilterStrategy;
import org.ojbc.intermediaries.sn.testutil.TestNotificationBuilderUtil;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
		})
@DirtiesContext
public class NotificationProcessorTest {

    @Resource
    DataSource dataSource;

    @Resource
    SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;

    private NotificationProcessor notificationProcessor;

    @Before
    public void setUp() throws Exception {
        notificationProcessor = new NotificationProcessor() {

            // no need to test these here, since they are abstract...look in the concrete subclass tests for the necessary tests

            @Override
            protected NotificationRequest makeNotificationRequestFromIncomingMessage(Message msg) throws Exception {
            	return TestNotificationBuilderUtil.returnArrestNotificationRequestForTesting(msg);
            }

        };

        notificationProcessor.setSubscriptionSearchQueryDAO(subscriptionSearchQueryDAO);

        DatabaseOperation.DELETE_ALL.execute(getConnection(), getDataSet("src/test/resources/xmlInstances/dbUnit/emptyDataSet.xml"));
        DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getDataSet("src/test/resources/xmlInstances/dbUnit/subscriptionDataSet.xml"));
    }

    private IDataSet getDataSet(String fileName) throws Exception {
        return new FlatXmlDataSetBuilder().build(new FileInputStream(fileName));
    }
    
    private IDatabaseConnection getConnection() throws Exception {
        Connection con = dataSource.getConnection();
        IDatabaseConnection connection = new DatabaseConnection(con);
        return connection;
    }

    @Test
    public void testNotificationWithWhitelistFilterEnhancementStrategy() throws Exception {

        File notificationMessageFile = new File("src/test/resources/xmlInstances/notificationMessage.xml");
        Document notificationMessageDocument = XmlUtils.parseFileToDocument(notificationMessageFile);
        Node sidElement = XmlUtils.xPathNodeSearch(notificationMessageDocument,
                "//notfm-exch:NotificationMessage/jxdm41:Person[1]/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
        sidElement.setTextContent("A5008305");

        Exchange e = new DefaultExchange((CamelContext) null);
        Message inMessage = e.getIn();
        inMessage.setBody(notificationMessageDocument);

        WhitelistFilteringEmailEnhancementStrategy d = new WhitelistFilteringEmailEnhancementStrategy();
        d.addAddressToWhitelist("po1@localhost");

        notificationProcessor.setEmailEnhancementStrategy(d);

        List<EmailNotification> emailNotifications = notificationProcessor.findSubscriptionsForNotification(e);
        assertEquals(2, emailNotifications.size());

        boolean foundPO1 = false;
        boolean foundPO3Blocked = false;

        for (EmailNotification notification : emailNotifications) {
            // set things up like happens in the processor...this is a consequence of segmenting the logic across camel route and processor
            e.getIn().setBody(notification);
            e.getIn().setHeader(NotificationConstants.HEADER_NOTIFICATION_TOPIC, "");
            notificationProcessor.createNotificationEmail(e);
            String toAddys = (String) e.getOut().getHeader(NotificationConstants.HEADER_TO);
            if (toAddys == null) {
                @SuppressWarnings("unchecked")
                Set<String> blockedAddressees = (Set<String>) e.getOut().getHeader(NotificationConstants.HEADER_BLOCKED);
                assertNotNull(blockedAddressees);
                foundPO3Blocked = blockedAddressees.contains("po3@localhost");
            } else if (toAddys.equals("po1@localhost")) {
                foundPO1 = true;
            }
        }

        assertTrue(foundPO1);
        assertTrue(foundPO3Blocked);

    }

    @Test
    public void testNotificationWithFilter() throws Exception {

        File notificationMessageFile = new File("src/test/resources/xmlInstances/notificationMessage.xml");
        Document notificationMessageDocument = XmlUtils.parseFileToDocument(notificationMessageFile);
        Node sidElement = XmlUtils.xPathNodeSearch(notificationMessageDocument,
                "//notfm-exch:NotificationMessage/jxdm41:Person[1]/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
        sidElement.setTextContent("A5008305"); // a sid that has a subscription

        Exchange e = new DefaultExchange((CamelContext) null);
        Message inMessage = e.getIn();
        inMessage.setBody(notificationMessageDocument);

        notificationProcessor.findSubscriptionsForNotification(e);

        assertNull(e.getProperty(Exchange.ROUTE_STOP));

        notificationProcessor.setNotificationFilterStrategy(new NotificationFilterStrategy() {
            @Override
            public boolean shouldMessageBeFiltered(NotificationRequest nr) {
                return true;
            }
        });

        List<EmailNotification> emailNotifications = notificationProcessor.findSubscriptionsForNotification(e);
        assertEquals(0, emailNotifications.size());
    }

    @Test
    public void testNotificationWithNoSubscription() throws Exception {

        File notificationMessageFile = new File("src/test/resources/xmlInstances/notificationMessage.xml");
        Document notificationMessageDocument = XmlUtils.parseFileToDocument(notificationMessageFile);

        Node sidElement = XmlUtils.xPathNodeSearch(notificationMessageDocument,
                "//notfm-exch:NotificationMessage/jxdm41:Person[1]/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
        sidElement.setTextContent("XXXXXXX"); // a sid that doesn't have a subscription

        Exchange e = new DefaultExchange((CamelContext) null);
        Message inMessage = e.getIn();
        inMessage.setBody(notificationMessageDocument);

        List<EmailNotification> notifications = notificationProcessor.findSubscriptionsForNotification(e);
        assertEquals(0, notifications.size());

    }

    @Test
    public void testCreateUniqueNotificationsNotConsolidated() throws Exception {

        List<Subscription> subscriptions = new ArrayList<Subscription>();

        Subscription subscription = new Subscription();
        Set<String> emailAddresses = new HashSet<String>();
        emailAddresses.add("po1@courts.hawaii.gov");
        emailAddresses.add("po2@courts.hawaii.gov");
        subscription.setEmailAddressesToNotify(emailAddresses);
        subscription.setPersonFullName("Joe Smith");
        subscription.setTopic("topics:person/arrest");
        subscription.setSubscribingSystemIdentifier("{http://demostate.gov/SystemNames/1.0}SystemB");
        subscriptions.add(subscription);

        File notificationMessageFile = new File("src/test/resources/xmlInstances/notificationMessage.xml");
        Document notificationMessageDocument = XmlUtils.parseFileToDocument(notificationMessageFile);
        Exchange e = new DefaultExchange((CamelContext) null);
        Message inMessage = e.getIn();
        inMessage.setBody(notificationMessageDocument);
        NotificationRequest request = notificationProcessor.makeNotificationRequestFromIncomingMessage(inMessage);

        List<EmailNotification> emailNotifications = notificationProcessor.createUniqueNotifications(subscriptions, request);
        Assert.assertEquals(2, emailNotifications.size());

        Assert.assertEquals("po2@courts.hawaii.gov", emailNotifications.get(0).getToAddressees());
        Assert.assertEquals("po1@courts.hawaii.gov", emailNotifications.get(1).getToAddressees());
        

    }

    @Test
    public void testCreateUniqueNotificationsConsolidated() throws Exception {

        notificationProcessor.setConsolidateEmailAddresses(true);

        List<Subscription> subscriptions = new ArrayList<Subscription>();

        Subscription subscription = new Subscription();
        Set<String> emailAddresses = new HashSet<String>();
        emailAddresses.add("po1@courts.hawaii.gov");
        emailAddresses.add("po2@courts.hawaii.gov");
        subscription.setEmailAddressesToNotify(emailAddresses);
        subscription.setPersonFullName("Joe Smith");
        subscription.setTopic("topics:person/arrest");
        subscription.setSubscribingSystemIdentifier("{http://demostate.gov/SystemNames/1.0}SystemB");
        subscriptions.add(subscription);

        subscription = new Subscription();
        emailAddresses = new HashSet<String>();
        emailAddresses.add("po3@courts.hawaii.gov");
        subscription.setEmailAddressesToNotify(emailAddresses);
        subscription.setPersonFullName("Joe Smith");
        subscription.setTopic("topics:person/arrest");
        subscription.setSubscribingSystemIdentifier("{http://demostate.gov/SystemNames/1.0}SystemB");
        subscriptions.add(subscription);

        File notificationMessageFile = new File("src/test/resources/xmlInstances/notificationMessage.xml");
        Document notificationMessageDocument = XmlUtils.parseFileToDocument(notificationMessageFile);
        Exchange e = new DefaultExchange((CamelContext) null);
        Message inMessage = e.getIn();
        inMessage.setBody(notificationMessageDocument);
        NotificationRequest request = notificationProcessor.makeNotificationRequestFromIncomingMessage(inMessage);

        List<EmailNotification> emailNotifications = notificationProcessor.createUniqueNotifications(subscriptions, request);
        Assert.assertEquals(2, emailNotifications.size());

        Assert.assertEquals("po2@courts.hawaii.gov,po1@courts.hawaii.gov", emailNotifications.get(0).getToAddressees());
        Assert.assertEquals("po3@courts.hawaii.gov", emailNotifications.get(1).getToAddressees());

    }

    @Test
    public void testCreateUniqueNotificationsConsolidatedWithDups() throws Exception {

        // Note that the behavior with dups is implemented in a "greedy" manner, which means
        // that the first subscription encountered is the one the email address is added to.

        notificationProcessor.setConsolidateEmailAddresses(true);

        List<Subscription> subscriptions = new ArrayList<Subscription>();

        Subscription subscription = new Subscription();
        Set<String> emailAddresses = new HashSet<String>();
        emailAddresses.add("po1@courts.hawaii.gov");
        emailAddresses.add("po2@courts.hawaii.gov");
        subscription.setEmailAddressesToNotify(emailAddresses);
        subscription.setPersonFullName("Joe Smith");
        subscription.setTopic("topics:person/arrest");
        subscription.setSubscribingSystemIdentifier("{http://demostate.gov/SystemNames/1.0}SystemB");
        subscriptions.add(subscription);

        subscription = new Subscription();
        emailAddresses = new HashSet<String>();
        emailAddresses.add("po1@courts.hawaii.gov");
        subscription.setEmailAddressesToNotify(emailAddresses);
        subscription.setPersonFullName("Joe Smith");
        subscription.setTopic("topics:person/arrest");
        subscription.setSubscribingSystemIdentifier("{http://demostate.gov/SystemNames/1.0}SystemB");
        subscriptions.add(subscription);

        File notificationMessageFile = new File("src/test/resources/xmlInstances/notificationMessage.xml");
        Document notificationMessageDocument = XmlUtils.parseFileToDocument(notificationMessageFile);
        Exchange e = new DefaultExchange((CamelContext) null);
        Message inMessage = e.getIn();
        inMessage.setBody(notificationMessageDocument);
        NotificationRequest request = notificationProcessor.makeNotificationRequestFromIncomingMessage(inMessage);

        List<EmailNotification> emailNotifications = notificationProcessor.createUniqueNotifications(subscriptions, request);
        Assert.assertEquals(1, emailNotifications.size());

        Assert.assertEquals("po2@courts.hawaii.gov,po1@courts.hawaii.gov", emailNotifications.get(0).getToAddressees());

        subscriptions = new ArrayList<Subscription>();

        subscription = new Subscription();
        emailAddresses = new HashSet<String>();
        emailAddresses.add("po1@courts.hawaii.gov");
        subscription.setEmailAddressesToNotify(emailAddresses);
        subscription.setPersonFullName("Joe Smith");
        subscription.setTopic("topics:person/arrest");
        subscription.setSubscribingSystemIdentifier("{http://demostate.gov/SystemNames/1.0}SystemB");
        subscriptions.add(subscription);

        subscription = new Subscription();
        emailAddresses = new HashSet<String>();
        emailAddresses.add("po1@courts.hawaii.gov");
        emailAddresses.add("po2@courts.hawaii.gov");
        subscription.setEmailAddressesToNotify(emailAddresses);
        subscription.setPersonFullName("Joe Smith");
        subscription.setTopic("topics:person/arrest");
        subscription.setSubscribingSystemIdentifier("{http://demostate.gov/SystemNames/1.0}SystemB");
        subscriptions.add(subscription);

        emailNotifications = notificationProcessor.createUniqueNotifications(subscriptions, request);
        Assert.assertEquals(2, emailNotifications.size());

        Assert.assertEquals("po1@courts.hawaii.gov", emailNotifications.get(0).getToAddressees());
        // note how po1 is removed from the second one, because he already was notified for this event...
        Assert.assertEquals("po2@courts.hawaii.gov", emailNotifications.get(1).getToAddressees());

    }

    @Test
    public void testCreateUniqueNotifications() throws Exception {

        List<Subscription> subscriptions = new ArrayList<Subscription>();

        Subscription subscription = new Subscription();
        Set<String> emailAddresses = new HashSet<String>();
        emailAddresses.add("po1@courts.hawaii.gov");
        subscription.setEmailAddressesToNotify(emailAddresses);
        subscription.setPersonFullName("Joe Smith");
        subscription.setTopic("topics:person/arrest");
        subscription.setSubscribingSystemIdentifier("{http://demostate.gov/SystemNames/1.0}SystemB");
        subscriptions.add(subscription);

        subscription = new Subscription();
        emailAddresses = new HashSet<String>();
        emailAddresses.add("po1@courts.hawaii.gov");
        subscription.setEmailAddressesToNotify(emailAddresses);
        subscription.setPersonFullName("Joe Smith");
        subscription.setTopic("topics:person/arrest");
        subscription.setSubscribingSystemIdentifier("{http://demostate.gov/SystemNames/1.0}SystemB");
        subscriptions.add(subscription);

        File notificationMessageFile = new File("src/test/resources/xmlInstances/notificationMessage.xml");
        Document notificationMessageDocument = XmlUtils.parseFileToDocument(notificationMessageFile);
        Exchange e = new DefaultExchange((CamelContext) null);
        Message inMessage = e.getIn();
        inMessage.setBody(notificationMessageDocument);
        NotificationRequest request = notificationProcessor.makeNotificationRequestFromIncomingMessage(inMessage);

        List<EmailNotification> emailNotifications = notificationProcessor.createUniqueNotifications(subscriptions, request);
        Assert.assertEquals(1, emailNotifications.size());

        String emailAddress = emailNotifications.get(0).getToAddressees();
        Assert.assertEquals("po1@courts.hawaii.gov", emailAddress);
        // Assert.assertEquals("12345",uniqueSubscriptions.get(0).getSID());
        Assert.assertEquals("Joe Smith", emailNotifications.get(0).getSubjectName());

        // Confirm that different email addresses will produce two unique subscriptions
        emailAddresses.clear();
        emailAddresses.add("frank.smith@hawaii.gov");

        emailNotifications = notificationProcessor.createUniqueNotifications(subscriptions, request);
        Assert.assertEquals(2, emailNotifications.size());

        // Return to original value
        emailAddresses.clear();
        emailAddresses.add("po1@courts.hawaii.gov");

        // Add a third subscription, confirm only one unique subscription produced
        subscription = new Subscription();
        emailAddresses = new HashSet<String>();
        emailAddresses.add("po1@courts.hawaii.gov");
        subscription.setEmailAddressesToNotify(emailAddresses);
        subscription.setPersonFullName("Joe Smith");
        subscription.setTopic("topics:person/arrest");
        subscription.setSubscribingSystemIdentifier("{http://demostate.gov/SystemNames/1.0}SystemB");
        subscriptions.add(subscription);

        subscriptions.add(subscription);

        emailNotifications = notificationProcessor.createUniqueNotifications(subscriptions, request);
        Assert.assertEquals(1, emailNotifications.size());

        // Add a fourth subscription from a different system, confirm we get two unique records
        subscription = new Subscription();
        emailAddresses = new HashSet<String>();
        emailAddresses.add("po1@courts.hawaii.gov");
        subscription.setEmailAddressesToNotify(emailAddresses);
        subscription.setPersonFullName("Joe Smith");
        subscription.setTopic("topics:person/arrest");
        subscription.setSubscribingSystemIdentifier("{http://demostate.gov/SystemNames/1.0}SystemA");
        subscriptions.add(subscription);

        subscriptions.add(subscription);

        emailNotifications = notificationProcessor.createUniqueNotifications(subscriptions, request);
        Assert.assertEquals(2, emailNotifications.size());
    }

}
