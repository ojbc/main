package org.ojbc.incidentReporting.dao;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
