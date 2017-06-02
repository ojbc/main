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
package org.ojbc.incidentReporting.dao;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-incident-reporting-state-cache.xml"})
public class IncidentReportPersonInvolvementProcessorTest {

	@Autowired
	private IncidentReportPersonInvolvementProcessor incidentReportPersonInvolvementProcessor;

	@Test
	@DirtiesContext
	public void testHasThisPersonArrestBeenProcessedBefore() throws Exception
	{
		Document notificationDocument = null;
		
		// We run this notification through twice.  The first time it will be new and thus persisted to the database.  The second time it will already be in the database 
		// and indicate that the notification has already been processed.

		
		notificationDocument = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/notifications/notification.xml"));
		
		Assert.assertFalse(incidentReportPersonInvolvementProcessor.hasThisPersonInvolvmentBeenProcessedBefore(notificationDocument));
		Assert.assertTrue(incidentReportPersonInvolvementProcessor.hasThisPersonInvolvmentBeenProcessedBefore(notificationDocument));
	}
}
