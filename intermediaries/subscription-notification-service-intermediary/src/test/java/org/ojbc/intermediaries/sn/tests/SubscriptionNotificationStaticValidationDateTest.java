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
package org.ojbc.intermediaries.sn.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.intermediaries.sn.dao.DefaultValidationDueDateStrategy;
import org.ojbc.intermediaries.sn.dao.StaticValidationDueDateStrategy;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.intermediaries.sn.topic.arrest.ArrestNotificationProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.subethamail.wiser.WiserMessage;

public class SubscriptionNotificationStaticValidationDateTest extends AbstractSubscriptionNotificationIntegrationTest {
    
    private static final Log log = LogFactory.getLog(SubscriptionNotificationStaticValidationDateTest.class);
    
    @EndpointInject(uri="mock:cxf:bean:fbiEbtsSubscriptionRequestService")
    protected MockEndpoint fbiEbtsSubscriptionMockEndpoint;
	
    //This is used to update database to achieve desired state for test
    private JdbcTemplate jdbcTemplate;
    
	@Before
	public void setUp() throws Exception {
		
		super.setUp();
		
    	context.getRouteDefinition("fbiEbtsSubscriptionSecureRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {    	    
    	    	
    	    	mockEndpointsAndSkip("cxf:bean:fbiEbtsSubscriptionRequestService*");
    	    }              
    	});    	    	
        
		this.jdbcTemplate = new JdbcTemplate(dataSource);
        
    	//Grab the DAO bean from the Spring Registry
    	SubscriptionSearchQueryDAO subscriptionSearchQueryDAO = (SubscriptionSearchQueryDAO)context.getRegistry().lookup("subscriptionSearchQueryDAO");
    	
    	//Create a static valiation due date strategy with a validity period of 365 days
    	StaticValidationDueDateStrategy staticValidationDueDateStrategy = new StaticValidationDueDateStrategy();
    	staticValidationDueDateStrategy.setValidDays(365);
    	subscriptionSearchQueryDAO.setValidationDueDateStrategy(staticValidationDueDateStrategy);

    	//Consolidate email addresses to successfully run tests
    	ArrestNotificationProcessor arrestNotificationProcessor = (ArrestNotificationProcessor)context.getRegistry().lookup("arrestNotificationProcessor");
    	arrestNotificationProcessor.setConsolidateEmailAddresses(true);
    	
	}
	
	@After
	public void tearDown() throws Exception {
		
		super.tearDown();
    	
		//Grab the DAO bean from the Spring Registry and set back to their original state
    	SubscriptionSearchQueryDAO subscriptionSearchQueryDAO = (SubscriptionSearchQueryDAO)context.getRegistry().lookup("subscriptionSearchQueryDAO");
    	subscriptionSearchQueryDAO.setValidationDueDateStrategy(new DefaultValidationDueDateStrategy());

    	ArrestNotificationProcessor arrestNotificationProcessor = (ArrestNotificationProcessor)context.getRegistry().lookup("arrestNotificationProcessor");
    	arrestNotificationProcessor.setConsolidateEmailAddresses(false);
    	
	}
	
    @Test
    public void notificationArrest_staticValidationDateTesting() throws Exception {   
    	
    	
    	//Confirm that we get no notifications, two matching notifications are filtered
        notifyAndAssertBasics("notificationSoapRequest_A5008305.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
                "SID: A5008305", 0);
        
        //Add/update a subscription, it will be valid for 365 days
		String response = invokeRequest("subscribeSoapRequest.xml", notificationBrokerUrl);
		
		assertThat(response, containsString(SUBSCRIPTION_REFERENCE_ELEMENT_STRING));
		
		List<WiserMessage> emails = notifyAndAssertBasics("notificationSoapRequest.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
				"SID: A9999999", 3);
		
		verifyNotificationForSubscribeSoapRequest(emails);
        
    }
    
    @Test
    public void notificationArrest_staticValidationDateTestingWithMultipleRecipientsOnToLine() throws Exception {
    
    	//We will use these three subscriptions for our tests, update their validation dates so they aren't filtered out
    	int rowsUpdated = this.jdbcTemplate.update("update subscription set lastValidationDate = curdate() where id = 1 or id =3 or id=4");
    	assertEquals(3, rowsUpdated);
    	
    	//In this use case, we will get two email sent since there are two subscription matches each with a single email address
    	//We only consolidate email addresses on a single subscription
    	notifyAndAssertBasics("notificationSoapRequest_A5008305.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
            "SID: A5008305", 6);

    	//In this use case, we get a matching subscription with two email addresses, it should be consolidate to a single email (with a CC and BCC)
    	//There are four email sent, A CC, A BCC, and one to each address
    	List<WiserMessage> emails  = notifyAndAssertBasics("notificationSoapRequest_A5008306.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
                "SID: A5008306", 4);
		
		for (WiserMessage email : emails) {
		    
		    //dumpEmail(email);
		    
		    // all the emails should be addressed like this
            assertEquals("po4@localhost, po5@localhost", email.getMimeMessage().getHeader("To", ","));
            assertEquals("sup@localhost", email.getMimeMessage().getHeader("Cc", ","));
            
		}
		
    	
    }
    
    @Test
    public void notificationArrest_endDateLessThanCurrentDate() throws Exception {   
	
    	DateTime yesterday = new DateTime();
    	yesterday = yesterday.minusDays(1);
    	
    	//We will update this subscription with an end date of yesterday and a validation due date on year from today
    	int rowsUpdated = this.jdbcTemplate.update("update subscription set lastValidationDate = curdate(), endDate='"+ yesterday.toString("yyyy-MM-dd") +  "' where id=4");
    	assertEquals(1, rowsUpdated);

    	notifyAndAssertBasics("notificationSoapRequest_A5008306.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
                "SID: A5008306", 0);

    	//We will update this subscription with an end date of yesterday and a validation due date on year from today
    	rowsUpdated = this.jdbcTemplate.update("update subscription set lastValidationDate = '" + yesterday.minusYears(1).toString("yyyy-MM-dd")   +  "', endDate='"+ yesterday.toString("yyyy-MM-dd") +  "' where id=4");
    	assertEquals(1, rowsUpdated);

    	notifyAndAssertBasics("notificationSoapRequest_A5008306.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
                "SID: A5008306", 0);

    }    

    /**
     * This test will test a no match scenario but with a validation due date strategy.
     * @throws Exception
     */
	@Test
    public void notificationWithNoSubscription() throws Exception {
        
        notifyAndAssertBasics("notificationSoapRequest.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
                null, 0);
    }

}
