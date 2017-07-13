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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.ModelCamelContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * This class will test state criminal history updates and most common methods.
 * Federal specific updates are in another class.
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/spring-context.xml",
        "classpath:META-INF/spring/cxf-endpoints.xml",      
        "classpath:META-INF/spring/properties-context.xml",
        "classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
        "classpath:META-INF/spring/subscription-management-routes.xml"
      })
@DirtiesContext
public class TestCriminalHistoryConsolidationServiceState {
	
	private static final Log log = LogFactory.getLog( TestCriminalHistoryConsolidationServiceState.class );

    @Resource
    private CriminalHistoryConsolidationProcessor criminalHistoryConsolidationProcessor;
    
    
    @Resource
    private ModelCamelContext context;

    @Test
    public void testReturnCriminalHistoryConsolidations() throws Exception
    {
    	CamelContext ctx = new DefaultCamelContext(); 
    	Exchange ex = new DefaultExchange(ctx);
    	    
    	List<CriminalHistoryConsolidationNotification> notifications = criminalHistoryConsolidationProcessor.consolidateCriminalHistoryState(ex, "A123457", "A123458", "9222201", "9222201", "criminalHistoryConsolidationReport");
    	
    	assertEquals(3, notifications.size());
    	
    	log.info("CH Notification: " + notifications.get(0));
    	
    	assertEquals("admin@local.gov", notifications.get(0).getEmailTo());
    	assertEquals("Criminal History Consolidation for SID for: A123457", notifications.get(0).getEmailSubject());
    	assertEquals("A123457 has been consolidated into A123458.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on A123458.  A new arrest may or may not have occurred.", notifications.get(0).getEmailBody());
    	
    	assertEquals("admin@local.gov", notifications.get(1).getEmailTo());
    	assertEquals("UCN added during SID consolidation/federal subscription created for: A123457", notifications.get(1).getEmailSubject());
    	assertEquals("SID: A123458 \n UCN: 9222201\n\nThe UCN associated to this SID has been updated.  Our records show you have an active State Rap Back subscription associated with this offender.  A federal Rap Back subscription was automatically created on your behalf.  Please log onto the HIJIS portal to verify or update your subscription as necessary.  For the updated criminal history record information, logon to OpenFox/NCIC to run a query on this UCN.", notifications.get(1).getEmailBody());

    	assertEquals("agencyemail@local.gov", notifications.get(2).getEmailTo());
    	assertEquals("Agency Notification: UCN added during SID consolidation/federal subscription created for: A123457", notifications.get(2).getEmailSubject());
    	assertEquals("Agency: SID: A123458 \n UCN: 9222201\n\nThe UCN associated to this SID has been updated.  Federal subscription added for user.", notifications.get(2).getEmailBody());

    	
    	//See processor camel context test for the database method tests
    	@SuppressWarnings("unchecked")
		List<Subscription> subscriptions = (List<Subscription>)ex.getIn().getHeader("subscriptionsMissingUCNtoAdd");
    	
    	log.info("Subscriptions to modify: " + subscriptions);
    	
    	assertEquals(1, subscriptions.size());
    	
    	notifications = criminalHistoryConsolidationProcessor.expungeCriminalHistoryState("A123458", "", "criminalHistoryExpungementReport");
    	
    	assertEquals(2, notifications.size());
    	
    	assertEquals("admin@local.gov", notifications.get(0).getEmailTo());
    	assertEquals("SID Expungement for: A123458", notifications.get(0).getEmailSubject());
    	assertEquals("A123458 has been deleted from CJIS-Hawaii and the State AFIS; you will no longer receive Rap Back notifications on this offender.  Please logon to the HIJIS Portal to update your subscription as necessary.", notifications.get(0).getEmailBody());

    	assertEquals("agencyemail@local.gov", notifications.get(1).getEmailTo());
    	assertEquals("Agency Notification: SID Expungement for: A123458", notifications.get(1).getEmailSubject());
    	assertEquals("A123458 EMAIL TEMPLATE PENDING", notifications.get(1).getEmailBody());

    }
    
	@Test
	public void testReturnCamelEmail() throws Exception
	{
		 CamelContext ctx = new DefaultCamelContext(); 
		 Exchange ex = new DefaultExchange(ctx);
		 
		 CriminalHistoryConsolidationNotification chcNotification = new CriminalHistoryConsolidationNotification();
		 
		 chcNotification.setEmailTo("someone@local.gov");
		 chcNotification.setEmailSubject("email subject");
		 chcNotification.setEmailBody("Some email body");
		 
		 ex.getIn().setBody(chcNotification);
		 
		 criminalHistoryConsolidationProcessor.returnCamelEmail(ex);
		 
		 assertNotNull(ex);
		 assertEquals("someone@local.gov", ex.getIn().getHeader("To"));
		 assertEquals("email subject", ex.getIn().getHeader("Subject"));
		 assertEquals("Some email body", ex.getIn().getBody(String.class));
		 assertEquals(2, ex.getIn().getHeaders().size());
	}
	
	@Test
	public void testReturnEmailSubjectState() throws Exception
	{
    	CriminalHistoryConsolidationNotification chcNotification = new CriminalHistoryConsolidationNotification();
    	
		chcNotification.setConsolidationType("criminalHistoryExpungementReport");
		assertEquals("SID Expungement for: 123456", criminalHistoryConsolidationProcessor.returnStateEmailSubject(chcNotification, "123456"));
		
		chcNotification.setConsolidationType("criminalHistoryConsolidationReport");
		assertEquals("Criminal History Consolidation for SID for: 123456", criminalHistoryConsolidationProcessor.returnStateEmailSubject(chcNotification, "123456"));

		chcNotification.setConsolidationType("criminalHistoryIdentifierUpdateReport");
		assertEquals("Criminal History Update for SID for: 123456", criminalHistoryConsolidationProcessor.returnStateEmailSubject(chcNotification, "123456"));

	}
	
	@Test
	public void testReturnEmailBodyState() throws Exception
	{
    	CriminalHistoryConsolidationNotification chcNotification = new CriminalHistoryConsolidationNotification();
    	String expectedEmailBody="";
    	
		chcNotification.setConsolidationType("criminalHistoryExpungementReport");
		expectedEmailBody="123456 has been deleted from CJIS-Hawaii and the State AFIS; you will no longer receive Rap Back notifications on this offender.  Please logon to the HIJIS Portal to update your subscription as necessary.";
		assertEquals(expectedEmailBody, criminalHistoryConsolidationProcessor.returnStateNotificationEmailBody(chcNotification, "123456", "99999"));
		
		chcNotification.setConsolidationType("criminalHistoryConsolidationReport");
		expectedEmailBody="123456 has been consolidated into 99999.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on 99999.  A new arrest may or may not have occurred.";
		assertEquals(expectedEmailBody, criminalHistoryConsolidationProcessor.returnStateNotificationEmailBody(chcNotification, "123456", "99999"));

		chcNotification.setConsolidationType("criminalHistoryIdentifierUpdateReport");
		expectedEmailBody="123456 has been updated to 99999.  Our records show you have an active Rap Back subscription to one of these SIDs.  Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to CJIS-Hawaii to run a query on 99999.  A new arrest may or may not have occurred.";
		assertEquals(expectedEmailBody, criminalHistoryConsolidationProcessor.returnStateNotificationEmailBody(chcNotification, "123456", "99999"));

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
