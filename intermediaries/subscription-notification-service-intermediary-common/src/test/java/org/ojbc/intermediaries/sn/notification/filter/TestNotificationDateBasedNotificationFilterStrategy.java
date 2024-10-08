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
package org.ojbc.intermediaries.sn.notification.filter;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.nio.charset.Charset;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;
import org.ojbc.intermediaries.sn.topic.incident.IncidentNotificationRequest;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
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
	    String inputStr = FileUtils.readFileToString(inputFile, Charset.defaultCharset());

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
	    String inputStr = FileUtils.readFileToString(inputFile, Charset.defaultCharset());

	    assertNotNull(inputStr);
	    
	    //Set it as the message message body
	    ex.getIn().setBody(inputStr);
		
		NotificationRequest notificationRequest = new IncidentNotificationRequest(ex.getIn());
	
		Assert.assertTrue(notificationDateBasedNotificationFilterStrategy.shouldMessageBeFiltered(notificationRequest));
		
	}
	
}
