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
package org.ojbc.intermediaries.sn.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.geronimo.mail.util.StringBufferOutputStream;
import org.dbunit.operation.DatabaseOperation;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.notification.filter.DefaultNotificationFilterStrategy;
import org.ojbc.intermediaries.sn.notification.filter.DuplicateNotificationFilterStrategy;
import org.ojbc.intermediaries.sn.topic.incident.IncidentNotificationProcessor;
import org.ojbc.util.model.BooleanPropertyWrapper;
import org.subethamail.wiser.WiserMessage;

public class SubscriptionNotificationIntegrationTest extends AbstractSubscriptionNotificationIntegrationTest {
    
    private static final Log log = LogFactory.getLog(SubscriptionNotificationIntegrationTest.class);
    
    @EndpointInject(uri="mock:cxf:bean:fbiEbtsSubscriptionRequestService")
    protected MockEndpoint fbiEbtsSubscriptionMockEndpoint; 
	
	@Resource
	protected IncidentNotificationProcessor incidentNotificationProcessor;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		
    	context.getRouteDefinition("fbiEbtsSubscriptionSecureRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {    	    
    	    	
    	    	mockEndpointsAndSkip("cxf:bean:fbiEbtsSubscriptionRequestService*");
    	    }              
    	});    	    	
	}
	
	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void subscribeArrest() throws Exception {
		String response = invokeRequest("subscribeSoapRequest.xml", notificationBrokerUrl);
		
		assertThat(response, containsString(SUBSCRIPTION_REFERENCE_ELEMENT_STRING));
        
		compareDatabaseWithExpectedDataset("subscriptionDataSet_afterSubscribe.xml");

		//Query for subscription just added to confirm validation date
		//DB Unit doesn't have good support for this
		//See: http://stackoverflow.com/questions/2856840/date-relative-to-current-in-the-dbunit-dataset
		Map<String, String> subjectIdentifiers = new HashMap<String, String>();
		subjectIdentifiers.put("SID", "A9999999");
		
		List<Subscription> subscriptions = subscriptionSearchQueryDAO.queryForSubscription("{http://ojbc.org/wsn/topics}:person/arrest", "{http://demostate.gov/SystemNames/1.0}SystemA", "SYSTEM", subjectIdentifiers );
		
		//We should only get one result
		assertEquals(1, subscriptions.size());
		
		//Get the validation date from database
		DateTime lastValidationDate = subscriptions.get(0).getLastValidationDate();
		
		//Add one year to the current date
		DateTime todayPlusOneYear = new DateTime();
		todayPlusOneYear.plusYears(1);
		
		//Assert that the date stamp is equal for both dates.
		assertEquals(lastValidationDate.toString("yyyy-MM-dd"), todayPlusOneYear.toString("yyyy-MM-dd"));
	}

	@Test
	public void subscribeArrest_alreadyExists() throws Exception {
		DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getDataSet("src/test/resources/xmlInstances/dbUnit/subscriptionDataSet_afterSubscribe.xml"));
		
		String response = invokeRequest("subscribeSoapRequest.xml", notificationBrokerUrl);
		
		assertThat(response, containsString(SUBSCRIPTION_REFERENCE_ELEMENT_STRING));
		
		compareDatabaseWithExpectedDataset("subscriptionDataSet_afterSubscribe.xml");
	}
	
	@Test
	public void subscribeArrest_alreadyExistsButInactive() throws Exception {
		DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getDataSet("src/test/resources/xmlInstances/dbUnit/subscriptionDataSet_afterUnSubscribe.xml"));
		
		String response = invokeRequest("subscribeSoapRequest.xml", notificationBrokerUrl);
		
		assertThat(response, containsString(SUBSCRIPTION_REFERENCE_ELEMENT_STRING));
		
		compareDatabaseWithExpectedDataset("subscriptionDataSet_afterSubscribe.xml");
	}

	@Test
	public void subscribeArrest_SIDexistsNewActivityID() throws Exception {
		
		String response = invokeRequest("subscribeSoapRequest_SIDexistsNewActivityID.xml", notificationBrokerUrl);
		
		assertThat(response, containsString(SUBSCRIPTION_REFERENCE_ELEMENT_STRING));
		
		compareDatabaseWithExpectedDataset("subscriptionDataSet_afterSubscribe_SIDexistsNewActivityID.xml");
	}

	@Test
	public void subscribeArrest_ActivityIDexistsNewSID() throws Exception {
		
		String response = invokeRequest("subscribeSoapRequest_ActivityIDexistsNewSID.xml", notificationBrokerUrl);
		
		assertThat(response, containsString(SUBSCRIPTION_REFERENCE_ELEMENT_STRING));
		
		compareDatabaseWithExpectedDataset("subscriptionDataSet_afterSubscribe_ActivityIDexistsNewSID.xml");
	}

	
	@Test
	public void subscribeIncident() throws Exception {
		String response = invokeRequest("subscribeSoapRequest-incident.xml", notificationBrokerUrl);
		
		assertThat(response, containsString(SUBSCRIPTION_REFERENCE_ELEMENT_STRING));
        
		compareDatabaseWithExpectedDataset("subscriptionDataSet_afterSubscribeIncident.xml");
	}
	
	@Test
	public void notificationArrest() throws Exception {
		String response = invokeRequest("subscribeSoapRequest.xml", notificationBrokerUrl);
		
		assertThat(response, containsString(SUBSCRIPTION_REFERENCE_ELEMENT_STRING));
		
		List<WiserMessage> emails = notifyAndAssertBasics("notificationSoapRequest.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
				"SID: A9999999", 3);
		
		verifyNotificationForSubscribeSoapRequest(emails);
		
	}

	@Test
	public void notificationArrestWithMultipleChargesInEmailBody() throws Exception {
		String response = invokeRequest("subscribeSoapRequest.xml", notificationBrokerUrl);
		
		assertThat(response, containsString(SUBSCRIPTION_REFERENCE_ELEMENT_STRING));
		
		List<WiserMessage> emails = notifyAndAssertBasics("notificationSoapRequest_MultipleCharges.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
				"SID: A9999999", 3);
		
		// there should be three messages:  one to the "to", one to the "cc", and one to the "bcc"
		for (WiserMessage email : emails) {
		    
			String emailMessage = dumpEmail(email);
			assertThat(emailMessage.replaceAll("[\r\n\t]", ""), containsString("ARREST CHARGES:<br/>Description: KEEP PISTOL Severity: FB, ARN: 14-377370<br/>Description: ELECTRIC GUNS Severity: MD, ARN: 14-377371<br/><br/><br/>Positively identified by fingerprint in demostate."));
		}
		
	}
	
	@Test
    public void notificationWithNoSubscription() throws Exception {
        
        notifyAndAssertBasics("notificationSoapRequest.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
                null, 0);
    }
	
    @Test
    public void notificationArrest_multipleSubscriptions() throws Exception {        
        notifyAndAssertBasics("notificationSoapRequest_A5008305.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
                "SID: A5008305", 6);
    }

    @Test
    public void notificationArrest_nullValidationDate() throws Exception {  
    	DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getDataSet("src/test/resources/xmlInstances/dbUnit/subscriptionDataSet_NullLastValidationDate.xml"));
    	
        notifyAndAssertBasics("notificationSoapRequest_A5008305.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
                "",0);
    }
    
    @Test
    public void notificationArrest_multipleEmailsOneSubscription() throws Exception {        
        List<WiserMessage> emails = notifyAndAssertBasics("notificationSoapRequest_A5008306.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
                "SID: A5008306", 6);
        
        boolean toPo4Found = false;
        boolean toPo5Found = false;
        int ccFoundCount = 0;
        int bccFoundCount = 0;
        
        for (WiserMessage email : emails) {
            
            //dumpEmail(email);
            
            assertEquals("sup@localhost", email.getMimeMessage().getHeader("Cc", ","));
            
            if ("po4@localhost".equals(email.getEnvelopeReceiver())) {
                toPo4Found = true;
            } else if ("po5@localhost".equals(email.getEnvelopeReceiver())) {
                toPo5Found = true;
            } else if ("sup@localhost".equals(email.getEnvelopeReceiver())) {
                ccFoundCount++;
            } else if ("testbcc@localhost".equals(email.getEnvelopeReceiver())) {
                bccFoundCount++;
            }
            
        }
        
        assertTrue(toPo4Found);
        assertTrue(toPo5Found);
        assertEquals(2, ccFoundCount);
        assertEquals(2, bccFoundCount);
        // after the refactoring, you should be able to configure to send multiple messages with one addressee per line, or one message with multiple addressees
    }
    
    @Test
    public void notificationForWhitelistedEmail() throws Exception {

        DateTime today = new DateTime();
        
        // obviously this is not a great test.  it is tightly coupled to the configuration of the non-whitelist filedrop location in the .cfg file for demostate, and
        // it is difficult to get ahold of the specific dump file produced.  but it will probably catch most issues...

        File baseDirectory = new File("/tmp/ojb/demostate/notificationBroker/notificationsNotWhitelisted/");
        File dateDirectory = new File(baseDirectory, today.toString("yyyyMMdd"));
        String[] files = dateDirectory.exists() ? dateDirectory.list() : new String[0];
        
        int preCount = files.length;
        
        notifyAndAssertBasics("notificationSoapRequest_A5008307.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
                "SID: A5008307", 0);
        
        files = dateDirectory.list();
        
        assertEquals(1, files.length - preCount); // obviously not fool proof, but better than nothing...
        
    }

	@Test
	public void notificationArrest_Inactive() throws Exception {
		DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getDataSet("src/test/resources/xmlInstances/dbUnit/subscriptionDataSet_afterUnSubscribe.xml"));

        notifyAndAssertBasics("notificationSoapRequest_A5012703.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
                null, 0);

	}

	@Test
	public void notificationArrest_sendEmailsSetToFalse() throws Exception {
		
		
		BooleanPropertyWrapper sendEmailNotificationsByConfigurationProcessor = (BooleanPropertyWrapper)context.getRegistry().lookup("sendEmailNotificationsByConfigurationProcessor");
		sendEmailNotificationsByConfigurationProcessor.setBooleanProperty(false);
		
		notifyAndAssertBasics("notificationSoapRequest_A5008305.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
                "SID: A5008305", 0);

		sendEmailNotificationsByConfigurationProcessor.setBooleanProperty(true);
	}
	
	
	
	@Test
	public void notificationArrest_MultipleInactive() throws Exception {
        DatabaseOperation.DELETE_ALL.execute(getConnection(), getCleanDataSet());
		DatabaseOperation.CLEAN_INSERT.execute(getConnection(), getDataSet("src/test/resources/xmlInstances/dbUnit/subscriptionDataSetMultipleInactive.xml"));
		
        notifyAndAssertBasics("notificationSoapRequest_A5012703.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingArrest/jxdm41:Arrest/nc:ActivityDate", 
                null, 0);
	}

	
    @Test
    public void notificationIncident() throws Exception {
    	

        String response = invokeRequest("subscribeSoapRequest-incident.xml", notificationBrokerUrl);
        
        assertThat(response, containsString(SUBSCRIPTION_REFERENCE_ELEMENT_STRING));   
        
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        
        notifyAndAssertBasics("notificationSoapRequest-incident.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingIncident/jxdm41:Incident/nc:ActivityDate", 
                "An individual for whom you have subscribed to be notified was involved in an incident documented by the following demostate law enforcement agency:<br/>\n" +
                "Montpelier Police Department<br/>\n" +
                "Incident Date/Time: " + dateString +"<br/>\n" +
                "Incident Report #:123457 <br/>\n" +
                "Subject Name: Doe, John<br/>\n" +
                "Subject date of birth:1980-01-01<br/>\n" +
                "Role: Witness<br/>\n" +
                "Offense Code: Driving Under Influence<br/>Offense Description: Driving Under The Influence, First Offense 23 VSA 1201 90D<br/><br/>\n" +
                "Offense Code: Robbery<br/>Offense Description: Robbery<br/><br/>\n" +
                "Officer: Clancy Wiggum<br/>\n" +
                "To follow up on this incident, please call Montpelier Police Department."
                , 3);
        
    }
    
    @Test
    public void notificationOfDuplicateIncident() throws Exception {
    	
    	assertNotNull(incidentNotificationProcessor);
    	DuplicateNotificationFilterStrategy notificationFilterStrategy = new DuplicateNotificationFilterStrategy();
    	notificationFilterStrategy.setDataSource(dataSource);
    	incidentNotificationProcessor.setNotificationFilterStrategy(notificationFilterStrategy);
        
    	invokeRequest("subscribeSoapRequest-incident.xml", notificationBrokerUrl);
        
        notify("notificationSoapRequest-incident.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingIncident/jxdm41:Incident/nc:ActivityDate");
        
        //There will now be zero notifications because this is a duplicate message
        notifyAndAssertBasics("notificationSoapRequest-incident.xml", "//notfm-exch:NotificationMessage/notfm-ext:NotifyingIncident/jxdm41:Incident/nc:ActivityDate", 
                null, 0);
        
        incidentNotificationProcessor.setNotificationFilterStrategy(new DefaultNotificationFilterStrategy());
        
    }
    
    private static String dumpEmail(WiserMessage email) throws IOException, MessagingException {
        StringBuffer sb = new StringBuffer(1024);
        StringBufferOutputStream sbos = new StringBufferOutputStream(sb);
        email.getMimeMessage().writeTo(sbos);
        log.debug("Email content: " + sb.toString());
        
        return sb.toString();
    }
	
}
