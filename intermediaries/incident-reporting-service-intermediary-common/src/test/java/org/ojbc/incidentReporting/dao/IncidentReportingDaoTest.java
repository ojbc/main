package org.ojbc.incidentReporting.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.helper.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-incident-reporting-state-cache.xml"})
public class IncidentReportingDaoTest {

	private static final Log log = LogFactory.getLog(IncidentReportingDaoTest.class);
	
	@Autowired
	private IncidentReportingDaoImpl incidentReportingDaoImpl;

	@Test
	public void testAddPersonInvolvement() throws Exception
	{
		String personInvolvementHash = Hash.sha256("Wallace_William_1976-01-01");
		incidentReportingDaoImpl.addPersonInvolvement("12345", "valcour", personInvolvementHash, IncidentReportPersonInvolvementProcessor.PERSON_INVOLVMENT_ACTIVITY_INCIDENT);
		
		//We just added this, we will get back a result
		Assert.assertNotNull(incidentReportingDaoImpl.isThereAPriorPersonInvolvment("12345", "valcour", personInvolvementHash, IncidentReportPersonInvolvementProcessor.PERSON_INVOLVMENT_ACTIVITY_INCIDENT));
		
		//No prior involvement, different incident number, return null
		Assert.assertNull(incidentReportingDaoImpl.isThereAPriorPersonInvolvment("1234567", "valcour", personInvolvementHash, IncidentReportPersonInvolvementProcessor.PERSON_INVOLVMENT_ACTIVITY_INCIDENT));
		
		//No prior involvement, different vendor, return null
		Assert.assertNull(incidentReportingDaoImpl.isThereAPriorPersonInvolvment("12345", "spillman", personInvolvementHash, IncidentReportPersonInvolvementProcessor.PERSON_INVOLVMENT_ACTIVITY_INCIDENT));

		//No prior involvement, different invovlement activity, return null
		Assert.assertNull(incidentReportingDaoImpl.isThereAPriorPersonInvolvment("12345", "spillman", personInvolvementHash, ArrestReportProcessor.PERSON_INVOLVMENT_ACTIVITY_ARREST));

		personInvolvementHash = Hash.sha256("Wallace_William_1976-01-02");
		//No prior involvement, different hash, return null
		Assert.assertNull(incidentReportingDaoImpl.isThereAPriorPersonInvolvment("12345", "valcour", personInvolvementHash, IncidentReportPersonInvolvementProcessor.PERSON_INVOLVMENT_ACTIVITY_INCIDENT));

	}
	
}
