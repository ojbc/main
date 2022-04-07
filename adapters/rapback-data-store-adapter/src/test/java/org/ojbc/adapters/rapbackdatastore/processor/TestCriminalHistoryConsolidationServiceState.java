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
package org.ojbc.adapters.rapbackdatastore.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.ojbc.adapters.rapbackdatastore.application.RapbackDatastoreAdapterApplication;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.util.model.rapback.Subscription;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

/**
 * 
 * This class will test state criminal history updates and most common methods.
 * Federal specific updates are in another class.
 * 
 *
 */
@UseAdviceWith
@CamelSpringBootTest
@SpringBootTest(classes=RapbackDatastoreAdapterApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) 
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
        "classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"
		})
public class TestCriminalHistoryConsolidationServiceState {
	
	private static final Log log = LogFactory.getLog( TestCriminalHistoryConsolidationServiceState.class );

    @Resource
    private CriminalHistoryConsolidationProcessor criminalHistoryConsolidationProcessor;
    
    @Resource 
    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
    
    @Resource
    private ModelCamelContext context;

    @Test
    public void testReturnCriminalHistoryConsolidations() throws Exception
    {
    	CamelContext ctx = new DefaultCamelContext(); 
    	Exchange ex = new DefaultExchange(ctx);
    	    
    	List<CriminalHistoryConsolidationNotification> notifications = criminalHistoryConsolidationProcessor.consolidateCriminalHistoryState(ex, "A123458", "A9999999", "9222201", "9222201", "criminalHistoryConsolidationReport", "criminal");
    	
    	assertEquals(7, notifications.size());
    	
    	log.info("CH Notification: " + notifications.get(0));

    	assertThat(notifications.get(0).getEmailTo(), Matchers.anyOf(Matchers.is("bill@local.gov"),Matchers.is("email6@email.com"), Matchers.is("email105@email.com")));
    	assertEquals("Rap Back: SID Consolidation by HCJDC", notifications.get(0).getEmailSubject());
    	assertEquals("A123458 has been consolidated into A9999999.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on A9999999.  A new arrest may or may not have occurred.", notifications.get(0).getEmailBody());

    	assertThat(notifications.get(1).getEmailTo(), Matchers.anyOf(Matchers.is("bill@local.gov"),Matchers.is("email6@email.com"), Matchers.is("email105@email.com")));
    	assertEquals("Rap Back: SID Consolidation by HCJDC", notifications.get(1).getEmailSubject());
    	assertEquals("A123458 has been consolidated into A9999999.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on A9999999.  A new arrest may or may not have occurred.", notifications.get(1).getEmailBody());

    	assertThat(notifications.get(2).getEmailTo(), Matchers.anyOf(Matchers.is("bill@local.gov"),Matchers.is("email6@email.com"), Matchers.is("email105@email.com")));
    	assertEquals("Rap Back: SID Consolidation by HCJDC", notifications.get(2).getEmailSubject());
    	assertEquals("A123458 has been consolidated into A9999999.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on A9999999.  A new arrest may or may not have occurred.", notifications.get(2).getEmailBody());

    	assertThat(notifications.get(3).getEmailTo(), Matchers.anyOf(Matchers.is("bill@local.gov"),Matchers.is("email6@email.com"), Matchers.is("email105@email.com")));
    	assertEquals("Rap Back: Federal Subscription Created: A123458", notifications.get(3).getEmailSubject());
    	assertEquals("SID: A9999999 \n UCN: 9222201\n\nThe UCN associated to this SID has been updated.  Our records show you have an active State Rap Back subscription associated with this offender.  A federal Rap Back subscription was automatically created on your behalf.  Please log onto the HIJIS portal to verify or update your subscription as necessary.  For the updated criminal history record information, logon to OpenFox/NCIC to run a query on this UCN.", notifications.get(3).getEmailBody());

    	assertThat(notifications.get(4).getEmailTo(), Matchers.anyOf(Matchers.is("bill@local.gov"),Matchers.is("email6@email.com"), Matchers.is("email105@email.com")));
    	assertEquals("Rap Back: Federal Subscription Created: A123458", notifications.get(4).getEmailSubject());
    	assertEquals("SID: A9999999 \n UCN: 9222201\n\nThe UCN associated to this SID has been updated.  Our records show you have an active State Rap Back subscription associated with this offender.  A federal Rap Back subscription was automatically created on your behalf.  Please log onto the HIJIS portal to verify or update your subscription as necessary.  For the updated criminal history record information, logon to OpenFox/NCIC to run a query on this UCN.", notifications.get(4).getEmailBody());

    	assertThat(notifications.get(5).getEmailTo(), Matchers.anyOf(Matchers.is("bill@local.gov"),Matchers.is("email6@email.com"), Matchers.is("email105@email.com")));
    	assertEquals("Rap Back: Federal Subscription Created: A123458", notifications.get(5).getEmailSubject());
    	assertEquals("SID: A9999999 \n UCN: 9222201\n\nThe UCN associated to this SID has been updated.  Our records show you have an active State Rap Back subscription associated with this offender.  A federal Rap Back subscription was automatically created on your behalf.  Please log onto the HIJIS portal to verify or update your subscription as necessary.  For the updated criminal history record information, logon to OpenFox/NCIC to run a query on this UCN.", notifications.get(5).getEmailBody());

    	assertEquals("agencyemail@local.gov", notifications.get(6).getEmailTo());
    	assertEquals("Rap Back: Federal Subscription Created: A123458", notifications.get(6).getEmailSubject());
    	assertEquals("Agency: SID: A9999999 \n UCN: 9222201\n\nThe active subscription for this SID did not have a corresponding federal subscription.  This subscription was updated to include the UCN stated above.  A Federal Rap Back subscription request was automatically sent.", notifications.get(6).getEmailBody());

    	
    	//See processor camel context test for the database method tests
    	@SuppressWarnings("unchecked")
		List<Subscription> subscriptions = (List<Subscription>)ex.getIn().getHeader("subscriptionsToModify");
    	
    	log.info("Subscriptions to modify: " + subscriptions);
    	
    	assertEquals(1, subscriptions.size());
    	
    	notifications = criminalHistoryConsolidationProcessor.expungeCriminalHistoryState("A9999999", "", "criminalHistoryExpungementReport", "criminal");
    	
    	assertEquals(4, notifications.size());

    	assertEquals("bill@local.gov", notifications.get(0).getEmailTo());
    	assertEquals("Rap Back: SID Deletion by HCJDC", notifications.get(0).getEmailSubject());
    	assertEquals("A9999999 has been deleted from CJIS-Hawaii and the State AFIS; you will no longer receive Rap Back notifications on this offender.  Please logon to the HIJIS Portal to update your subscription as necessary.", notifications.get(0).getEmailBody());

    	assertEquals("email6@email.com", notifications.get(1).getEmailTo());
    	assertEquals("Rap Back: SID Deletion by HCJDC", notifications.get(1).getEmailSubject());
    	assertEquals("A9999999 has been deleted from CJIS-Hawaii and the State AFIS; you will no longer receive Rap Back notifications on this offender.  Please logon to the HIJIS Portal to update your subscription as necessary.", notifications.get(1).getEmailBody());

    	assertEquals("email105@email.com", notifications.get(2).getEmailTo());
    	assertEquals("Rap Back: SID Deletion by HCJDC", notifications.get(2).getEmailSubject());
    	assertEquals("A9999999 has been deleted from CJIS-Hawaii and the State AFIS; you will no longer receive Rap Back notifications on this offender.  Please logon to the HIJIS Portal to update your subscription as necessary.", notifications.get(2).getEmailBody());

    	assertEquals("agencyemail@local.gov", notifications.get(3).getEmailTo());
    	assertEquals("Agency Notification: SID Expungement for: A9999999", notifications.get(3).getEmailSubject());
    	assertEquals("A9999999 has been deleted from CJIS-Hawaii and the State AFIS; Rap Back notifications will no longer be sent for this offender.", notifications.get(3).getEmailBody());
    	
    	notifications.clear();
    	
    	//We should get four notifications.  3 for the SID consolidation and one for the UCN match notification
    	notifications = criminalHistoryConsolidationProcessor.consolidateCriminalHistoryState(ex, "A9999999", "A8888888", "9222200", "9222201", "criminalHistoryConsolidationReport", "criminal");

    	log.info(notifications);
    	
    	assertEquals(6, notifications.size());

    	assertEquals("criminalHistoryConsolidationReport",notifications.get(0).getConsolidationType());
    	assertEquals("Rap Back: SID Consolidation by HCJDC",notifications.get(0).getEmailSubject());
    	assertEquals("A9999999 has been consolidated into A8888888.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on A8888888.  A new arrest may or may not have occurred.",notifications.get(0).getEmailBody());

    	assertEquals("criminalHistoryConsolidationReport",notifications.get(1).getConsolidationType());
    	assertEquals("Rap Back: SID Consolidation by HCJDC",notifications.get(1).getEmailSubject());
    	assertEquals("A9999999 has been consolidated into A8888888.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on A8888888.  A new arrest may or may not have occurred.",notifications.get(1).getEmailBody());

    	assertEquals("criminalHistoryConsolidationReport",notifications.get(2).getConsolidationType());
    	assertEquals("Rap Back: SID Consolidation by HCJDC",notifications.get(2).getEmailSubject());
    	assertEquals("A9999999 has been consolidated into A8888888.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on A8888888.  A new arrest may or may not have occurred.",notifications.get(2).getEmailBody());
    	
    	assertEquals("reportUCNMmatchToUser",notifications.get(3).getConsolidationType());
    	assertEquals("UCN update occurred for: 9222200",notifications.get(3).getEmailSubject());
    	assertEquals("New UCN: 9222201\nOld UCN: 9222200\n\nThe UCNs stated above have been consolidated or updated in CJIS-Hawaii.  Our records show you have an active federal Rap Back subscription to one of these UCNs. Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to OpenFox/NCIC to run a query on the new UCN. A new arrest may or may not have occurred.\nYou may receive another notification once the UCN is updated in FBI NGI System.",notifications.get(3).getEmailBody());
    	
    }
    
	@Test
	@Disabled
	public void testReturnCamelEmail() throws Exception
	{
		 CamelContext ctx = new DefaultCamelContext(); 
		 Exchange ex = new DefaultExchange(ctx);
		 
		 CriminalHistoryConsolidationNotification chcNotification = new CriminalHistoryConsolidationNotification();

		 List<Subscription> subscriptions = subscriptionSearchQueryDAO.queryForSubscription("62723");
		 Subscription subscription = subscriptions.get(0);
		 
		 chcNotification.setSubscription(subscription);
		 chcNotification.setEmailTo("someone@local.gov");
		 chcNotification.setEmailSubject("email subject");
		 chcNotification.setEmailBody("Some email body");
		 
		 ex.getIn().setBody(chcNotification);
		 
		 criminalHistoryConsolidationProcessor.returnCamelEmail(ex);
		 
		 assertNotNull(ex);
		 
		 //This was retrieved from the agency profile
		 assertEquals("demo.agency@localhost", ex.getIn().getHeader("To"));
		 assertEquals("email subject", ex.getIn().getHeader("Subject"));
		 assertEquals("Some email body", ex.getIn().getBody(String.class));
		 assertEquals(2, ex.getIn().getHeaders().size());
	}
	
	@Test
	public void testReturnEmailSubjectState() throws Exception
	{
    	CriminalHistoryConsolidationNotification chcNotification = new CriminalHistoryConsolidationNotification();
    	
		chcNotification.setConsolidationType("criminalHistoryExpungementReport");
		assertEquals("Rap Back: SID Deletion by HCJDC", criminalHistoryConsolidationProcessor.returnStateEmailSubject(chcNotification, "123456"));
		
		chcNotification.setConsolidationType("criminalHistoryConsolidationReport");
		assertEquals("Rap Back: SID Consolidation by HCJDC", criminalHistoryConsolidationProcessor.returnStateEmailSubject(chcNotification, "123456"));

		chcNotification.setConsolidationType("criminalHistoryIdentifierUpdateReport");
		assertEquals("Rap Back: SID Update by HCJDC", criminalHistoryConsolidationProcessor.returnStateEmailSubject(chcNotification, "123456"));

		chcNotification.setConsolidationType("reportSIDExpungementToAgency");
		assertEquals("Agency Notification: SID Expungement for: 123456", criminalHistoryConsolidationProcessor.returnStateEmailSubject(chcNotification, "123456"));
		
		chcNotification.setConsolidationType("reportUCNMismatchToAgency");
		assertEquals("Agency Notification: UCN mismatch during SID consolidation for: 123456", criminalHistoryConsolidationProcessor.returnStateEmailSubject(chcNotification, "123456"));
		
		chcNotification.setConsolidationType("reportUCNAddedToAgency");
		assertEquals("Rap Back: Federal Subscription Created: 123456", criminalHistoryConsolidationProcessor.returnStateEmailSubject(chcNotification, "123456"));
		
		chcNotification.setConsolidationType("reportUCNAddedToUser");
		assertEquals("Rap Back: Federal Subscription Created: 123456", criminalHistoryConsolidationProcessor.returnStateEmailSubject(chcNotification, "123456"));
		
		chcNotification.setConsolidationType("reportUCNMmatchToUser");
		assertEquals("UCN update occurred for: 123456", criminalHistoryConsolidationProcessor.returnStateEmailSubject(chcNotification, "123456"));
	}
	
	@Test
	public void testReturnEmailBodyState() throws Exception
	{
    	CriminalHistoryConsolidationNotification chcNotification = new CriminalHistoryConsolidationNotification();
    	String expectedEmailBody="";
    	
		chcNotification.setConsolidationType("criminalHistoryExpungementReport");
		expectedEmailBody="123456 has been deleted from CJIS-Hawaii and the State AFIS; you will no longer receive Rap Back notifications on this offender.  Please logon to the HIJIS Portal to update your subscription as necessary.";
		assertEquals(expectedEmailBody, criminalHistoryConsolidationProcessor.returnStateNotificationEmailBody(chcNotification, "123456", "99999", "criminal"));
		
		chcNotification.setConsolidationType("criminalHistoryConsolidationReport");
		expectedEmailBody="123456 has been consolidated into 99999.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on 99999.  A new arrest may or may not have occurred.";
		assertEquals(expectedEmailBody, criminalHistoryConsolidationProcessor.returnStateNotificationEmailBody(chcNotification, "123456", "99999", "criminal"));

		chcNotification.setConsolidationType("criminalHistoryIdentifierUpdateReport");
		expectedEmailBody="123456 has been updated to 99999.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on 99999.  A new arrest may or may not have occurred.";
		assertEquals(expectedEmailBody, criminalHistoryConsolidationProcessor.returnStateNotificationEmailBody(chcNotification, "123456", "99999", "criminal"));
		
		chcNotification.setConsolidationType("criminalHistoryExpungementReport");
		expectedEmailBody="You are receiving this message because a identity deletion event has occurred related to one or more of your Rap Back subscriptions.  Your subscriptions have been cancelled.";
		assertEquals(expectedEmailBody, criminalHistoryConsolidationProcessor.returnStateNotificationEmailBody(chcNotification, "123456", "99999", "civil"));
		
		chcNotification.setConsolidationType("criminalHistoryConsolidationReport");
		expectedEmailBody="123456 has been updated to 99999.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription and for updated criminal history record information.";
		assertEquals(expectedEmailBody, criminalHistoryConsolidationProcessor.returnStateNotificationEmailBody(chcNotification, "123456", "99999", "civil"));

		chcNotification.setConsolidationType("criminalHistoryIdentifierUpdateReport");
		expectedEmailBody="123456 has been updated to 99999.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription and for updated criminal history record information.";
		assertEquals(expectedEmailBody, criminalHistoryConsolidationProcessor.returnStateNotificationEmailBody(chcNotification, "123456", "99999", "civil"));		

	}	
	
	@Test
	public void testReturnUniqueEmailNotifications() throws Exception
	{
		List<CriminalHistoryConsolidationNotification> criminalHistoryConsolidationNotifications = new ArrayList<CriminalHistoryConsolidationNotification>();
		
		CriminalHistoryConsolidationNotification chcNotification1 = new CriminalHistoryConsolidationNotification();
		chcNotification1.setEmailTo("1@local.gov");
		
		CriminalHistoryConsolidationNotification chcNotification2 = new CriminalHistoryConsolidationNotification();
		chcNotification2.setEmailTo("1@local.gov");
		
		CriminalHistoryConsolidationNotification chcNotification3 = new CriminalHistoryConsolidationNotification();
		chcNotification3.setEmailTo("3@local.gov");
		
		criminalHistoryConsolidationNotifications.add(chcNotification1);
		criminalHistoryConsolidationNotifications.add(chcNotification2);
		criminalHistoryConsolidationNotifications.add(chcNotification3);
		
		List<CriminalHistoryConsolidationNotification> uniqueNotifications = criminalHistoryConsolidationProcessor.returnUniqueEmailNotifications(criminalHistoryConsolidationNotifications);
		
		assertEquals(2, uniqueNotifications.size());
		
		boolean containsEmailOne = false;
		boolean containsEmailThree = false;
		
		for (CriminalHistoryConsolidationNotification uniqueNotification : uniqueNotifications)
		{
			if (uniqueNotification.getEmailTo().equals("1@local.gov"))
			{
				containsEmailOne = true;
			}
			
			if (uniqueNotification.getEmailTo().equals("3@local.gov"))
			{
				containsEmailThree = true;
			}	
			
		}	
		
		assertTrue(containsEmailOne);
		assertTrue(containsEmailThree);
		
	}
	
	
}
