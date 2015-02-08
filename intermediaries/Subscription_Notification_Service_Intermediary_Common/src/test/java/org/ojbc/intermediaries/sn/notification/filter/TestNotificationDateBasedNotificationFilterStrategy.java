package org.ojbc.intermediaries.sn.notification.filter;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.ojbc.intermediaries.sn.notification.NotificationRequest;
import org.ojbc.intermediaries.sn.topic.incident.IncidentNotificationRequest;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class TestNotificationDateBasedNotificationFilterStrategy {

	@Test
	public void testMessageShouldBeFiltered() throws Exception
	{
		
		EventDateBasedNotificationFilterStrategy notificationDateBasedNotificationFilterStrategy = new EventDateBasedNotificationFilterStrategy();
		notificationDateBasedNotificationFilterStrategy.setFilterNotificationsWithEventDatesOlderThanThisManyDays(30);
		
		
		CamelContext ctx = new DefaultCamelContext(); 
		Exchange ex = new DefaultExchange(ctx);
		
	    //Read the subscription request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/notificationMessage-incidentOldNotificationDate.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    assertNotNull(inputStr);
	    
	    //Set it as the message message body
	    ex.getIn().setBody(inputStr);
		
		NotificationRequest notificationRequest = new IncidentNotificationRequest(ex.getIn());
	
		Assert.assertTrue(notificationDateBasedNotificationFilterStrategy.shouldMessageBeFiltered(notificationRequest));
		
	}
	

	@Test
	public void testMessageShouldNotBeFiltered() throws Exception
	{
		
		EventDateBasedNotificationFilterStrategy notificationDateBasedNotificationFilterStrategy = new EventDateBasedNotificationFilterStrategy();
		notificationDateBasedNotificationFilterStrategy.setFilterNotificationsWithEventDatesOlderThanThisManyDays(30);
		
		
		CamelContext ctx = new DefaultCamelContext(); 
		Exchange ex = new DefaultExchange(ctx);
		
	    //Read the subscription request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/notificationMessage-incidentFutureNotificationDate.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    assertNotNull(inputStr);
	    
	    //Set it as the message message body
	    ex.getIn().setBody(inputStr);
		
		NotificationRequest notificationRequest = new IncidentNotificationRequest(ex.getIn());
	
		Assert.assertFalse(notificationDateBasedNotificationFilterStrategy.shouldMessageBeFiltered(notificationRequest));
		
	}
	
}
