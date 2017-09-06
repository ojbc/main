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
package org.ojbc.intermediaries.sn.dao.audit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.notification.EmailNotification;
import org.ojbc.intermediaries.sn.notification.RapbackTriggeringEvent;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",		
		"classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"
}) 
public class AuditDaoImplTest {
	private final Log log = LogFactory.getLog(this.getClass());
	    
	@Resource
	private AuditDAOImpl auditDAOImpl;
	
    @Resource  
    private DataSource dataSourceAudit;  

	@Before
	public void setUp() throws Exception {
		assertNotNull(auditDAOImpl);
	}
	
	@Test
	public void testLogNotification()
	{
		EmailNotification emailNotification = new EmailNotification();
		
		emailNotification.addToAddressee("to1@email.com");
		emailNotification.addToAddressee("to2@email.com");

		emailNotification.addCcAddressee("cc1@email.com");
		emailNotification.addCcAddressee("cc2@email.com");

		emailNotification.addBccAddressee("bcc1@email.com");
		emailNotification.addBccAddressee("bcc2@email.com");
		
		
		Subscription subscription = new Subscription();
		
		subscription.setId(1);
		subscription.setOri("ori");
		subscription.setSubscriptionOwner("owner");
		subscription.setSubscriptionOwnerEmailAddress("owner@email.com");
		subscription.setSubscribingSystemIdentifier("probation");
		subscription.setTopic("arrest");
		
		List<RapbackTriggeringEvent> triggeringEvents = new ArrayList<RapbackTriggeringEvent>();
		
		RapbackTriggeringEvent triggeringEvent = new RapbackTriggeringEvent();
		triggeringEvent.setTriggeringEventCode("Triggering Event Code");
		triggeringEvents.add(triggeringEvent);
		
		emailNotification.setTriggeringEvents(triggeringEvents);
		
		
		emailNotification.setSubscription(subscription);

		Integer notificationPk = auditDAOImpl.saveNotificationLogEntry(emailNotification);
		
		log.info("Notification Saved PK: "  + notificationPk);
		
		NotificationsSent notificationsSent = auditDAOImpl.retrieveNotificationSentById(notificationPk);
		
		log.info("Notification Sent: " + notificationsSent);
		
		assertEquals(1, notificationsSent.getSubscription().getId());
		assertEquals("ori", notificationsSent.getSubscription().getOri());
		assertEquals("owner", notificationsSent.getSubscription().getSubscriptionOwner());
		assertEquals("owner@email.com", notificationsSent.getSubscription().getSubscriptionOwnerEmailAddress());
		assertEquals("probation", notificationsSent.getSubscription().getSubscribingSystemIdentifier());
		assertEquals("arrest", notificationsSent.getSubscription().getTopic());
		
		assertEquals(6, notificationsSent.getNotificationMechanisms().size());
		
		for (NotificationMechanism mechanism : notificationsSent.getNotificationMechanisms())
		{
			if (mechanism.getNotificationAddress().equals("to1@email.com"))
			{
				assertEquals("email", mechanism.getNotificationMechansim());
				assertEquals("to", mechanism.getNotificationRecipientType());
			}	
		}	
		
		assertEquals(1, notificationsSent.getNotificationProperties().size());
		assertEquals("Triggering Event Code", notificationsSent.getNotificationProperties().get(0).getPropertyValue());
		assertEquals("RAPBACK_TRIGERRING_EVENT_CODE", notificationsSent.getNotificationProperties().get(0).getPropertyName());
		
		Integer rowsDeleted = auditDAOImpl.deleteNotificationLogEntry(notificationPk);
		
		log.info("Rows Deleted: "  + rowsDeleted);
		
		assertEquals(1, rowsDeleted.intValue());
		
	}
	
	
}
