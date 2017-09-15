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

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
public class TestCriminalHistoryConsolidationServiceFederal {
	
	private static final Log log = LogFactory.getLog( TestCriminalHistoryConsolidationServiceFederal.class );

    @Resource
    private CriminalHistoryConsolidationProcessor criminalHistoryConsolidationProcessor;
    
    @Resource
    private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
    
    @Resource
    private ModelCamelContext context;

    @Test
    public void testConsolidateCriminalHistoryFederal() throws Exception
    {
    	CamelContext ctx = new DefaultCamelContext(); 
    	Exchange ex = new DefaultExchange(ctx);
    	    
    	//There are no matching FBI IDs here
    	List<CriminalHistoryConsolidationNotification> notifications = criminalHistoryConsolidationProcessor.consolidateCriminalHistoryFederal(ex, "1", "2", "criminalHistoryConsolidationReport");
    	assertEquals(0, notifications.size());

    	//The test data doesn't have an FBI ID, add one here to test with
    	subscriptionSearchQueryDAO.insertSubjectIdentifier(62723, SubscriptionNotificationConstants.FBI_ID, "1");

    	notifications = criminalHistoryConsolidationProcessor.consolidateCriminalHistoryFederal(ex, "1", "2", "criminalHistoryConsolidationReport");
    	assertEquals(2, notifications.size());
    	
    	log.info("Federal Consolidation Notification User: " + notifications.get(0));
    	log.info("Federal Consolidation Notification Agency: " + notifications.get(1));
    	
    	//Confirm user gets email
    	assertEquals("admin@local.gov", notifications.get(0).getEmailTo());
    	assertEquals("Rap Back: UCN Consolidation by FBI: 1", notifications.get(0).getEmailSubject());
    	assertEquals("New UCN: 2 \n Old UCN: 1\n\nThe FBI's NGI System has consolidated the UCNs stated above. Our records show you have an active federal Rap Back subscription to one of these UCNs. Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to OpenFox/NCIC to run a query on the new UCN. A new arrest may or may not have occurred. You may receive another notification once the UCN is updated in CJIS-Hawaii.", notifications.get(0).getEmailBody());

    	//Confirm agency gets email
    	assertEquals("agencyemail@local.gov", notifications.get(1).getEmailTo());
    	assertEquals("Rap Back: UCN Consolidation by FBI: 1", notifications.get(1).getEmailSubject());
    	assertEquals("New UCN: 2 \n Old UCN: 1\n\nThe FBI's NGI System has consolidated the UCNs stated above. The corresponding State subscription(s) was updated with the consolidated UCN received in the RBN.", notifications.get(1).getEmailBody());

    	//Confirm we have one sub with the FBI ID of 2
    	Map<String,String> subjectIdentifiers = Collections.singletonMap(SubscriptionNotificationConstants.FBI_ID, "2");
    	
    	//Search for active subscriptions with matching SIDs
    	List<Subscription> subscriptionsMatchingFBIID = subscriptionSearchQueryDAO.queryForSubscription(null, null, null, subjectIdentifiers);
    	
    	assertEquals(1,subscriptionsMatchingFBIID.size());
    	
    	//Notice that we now search on FBI ID of '2' since the last consolidation update the value in the DB
    	notifications = criminalHistoryConsolidationProcessor.consolidateCriminalHistoryFederal(ex, "2", "3", "criminalHistoryExpungementReport");
    	assertEquals(2, notifications.size());
    	
    	log.info("Federal Expungement Notification User: " + notifications.get(0));
    	log.info("Federal Expungement Notification Agency: " + notifications.get(1));
    	
    	assertEquals("admin@local.gov", notifications.get(0).getEmailTo());
    	assertEquals("Rap Back: UCN Deleted by FBI: 2", notifications.get(0).getEmailSubject());
    	assertEquals("Deleted UCN:  2 \n This UCN has been deleted from the FBI's NGI System; you will no longer receive federal Rap Back notifications on this offender.  Should a new UCN be reassigned to this offender, a new federal Rap Back subscrition will automatically occur.  Please logon to the HIJIS Portal to update your subscription, if necessary.", notifications.get(0).getEmailBody());

    	assertEquals("agencyemail@local.gov", notifications.get(1).getEmailTo());
    	assertEquals("Rap Back: UCN Deleted by FBI: 2", notifications.get(1).getEmailSubject());
    	assertEquals("Deleted UCN:  2 \n\nThis UCN has been deleted from the FBI's NGI System.\n\nNotifications for this offender will no longer be sent for Federal Rap Back.", notifications.get(1).getEmailBody());
    	
    	//Since we expunged the FBI ID, confirm that it is no longer there.
    	subjectIdentifiers = Collections.singletonMap(SubscriptionNotificationConstants.FBI_ID, "2");
    	
    	//Search for active subscriptions with matching SIDs
    	subscriptionsMatchingFBIID = subscriptionSearchQueryDAO.queryForSubscription(null, null, null, subjectIdentifiers);
    	
    	assertEquals(0,subscriptionsMatchingFBIID.size());
    	

    }
    
    @Test
    public void testFederalUCNRestoration() throws Exception
    {
    	CamelContext ctx = new DefaultCamelContext(); 
    	Exchange ex = new DefaultExchange(ctx);
    	    
    	List<CriminalHistoryConsolidationNotification> notifications = criminalHistoryConsolidationProcessor.federalUCNRestoration(ex, "9222201");
    	
    	assertEquals(1, notifications.size());

    	log.info("UCN Restoration Notification: " + notifications.get(0));
    	
    	assertEquals("agencyemail@local.gov", notifications.get(0).getEmailTo());
    	assertEquals("UCN Restoration message for: 9222201", notifications.get(0).getEmailSubject());
    	assertEquals("Restored UCN: 9222201\n\nThis UCN was restored to the FBI's NGI System.", notifications.get(0).getEmailBody());

    }
    

	@Test
	public void testReturnEmailSubjectFederal() throws Exception
	{
    	CriminalHistoryConsolidationNotification chcNotification = new CriminalHistoryConsolidationNotification();
    	
		chcNotification.setConsolidationType("reportUCNConsolidationToAgency");
		assertEquals("Agency UCN consolidation report for UNC for: 123456", criminalHistoryConsolidationProcessor.returnFederalEmailSubject(chcNotification, "123456"));
		
		chcNotification.setConsolidationType("reportUCNConsolidationToUser");
		assertEquals("UCN consolidation report for UNC for: 123456", criminalHistoryConsolidationProcessor.returnFederalEmailSubject(chcNotification, "123456"));

		chcNotification.setConsolidationType("reportUCNExpungementToAgency");
		assertEquals("Agency UCN expungement report for UNC for: 123456", criminalHistoryConsolidationProcessor.returnFederalEmailSubject(chcNotification, "123456"));

		chcNotification.setConsolidationType("reportUCNExpungementToUser");
		assertEquals("UCN expungement report for UNC for: 123456", criminalHistoryConsolidationProcessor.returnFederalEmailSubject(chcNotification, "123456"));

	}
	
	@Test
	public void testReturnEmailBodyFederal() throws Exception
	{
    	CriminalHistoryConsolidationNotification chcNotification = new CriminalHistoryConsolidationNotification();
    	String expectedEmailBody="";
    	
		chcNotification.setConsolidationType("reportUCNConsolidationToAgency");
		expectedEmailBody="New UCN: 99999 \n Old UCN: 123456\n\nThe FBI's NGI System has consolidated the UCNs stated above. The corresponding State subscription(s) was updated with the consolidated UCN received in the RBN.";
		assertEquals(expectedEmailBody, criminalHistoryConsolidationProcessor.returnFederalNotificationEmailBody(chcNotification, "123456", "99999"));
		
		chcNotification.setConsolidationType("reportUCNConsolidationToUser");
		expectedEmailBody="New UCN: 99999 \n Old UCN: 123456\n\nThe FBI's NGI System has consolidated the UCNs stated above. Our records show you have an active federal Rap Back subscription to one of these UCNs. Please logon to the HIJIS portal to verify your subscription.  For the updated criminal history record information, logon to OpenFox/NCIC to run a query on the new UCN. A new arrest may or may not have occurred. You may receive another notification once the UCN is updated in CJIS-Hawaii.";
		assertEquals(expectedEmailBody, criminalHistoryConsolidationProcessor.returnFederalNotificationEmailBody(chcNotification, "123456", "99999"));

		chcNotification.setConsolidationType("reportUCNExpungementToAgency");
		expectedEmailBody="Deleted UCN:  123456 \n\nThis UCN has been deleted from the FBI's NGI System.\n\nNotifications for this offender will no longer be sent for Federal Rap Back.";
		assertEquals(expectedEmailBody, criminalHistoryConsolidationProcessor.returnFederalNotificationEmailBody(chcNotification, "123456", "99999"));

		chcNotification.setConsolidationType("reportUCNExpungementToUser");
		expectedEmailBody="Deleted UCN:  123456 \n This UCN has been deleted from the FBI's NGI System; you will no longer receive federal Rap Back notifications on this offender.  Should a new UCN be reassigned to this offender, a new federal Rap Back subscrition will automatically occur.  Please logon to the HIJIS Portal to update your subscription, if necessary.";
		assertEquals(expectedEmailBody, criminalHistoryConsolidationProcessor.returnFederalNotificationEmailBody(chcNotification, "123456", "99999"));

	}	
	
	
	
}
