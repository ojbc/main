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
package org.ojbc.incidentReporting.dao;

import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.helper.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-context-incident-reporting-state-cache.xml"})
public class IncidentReportingDaoTest {

	private static final Log log = LogFactory.getLog(IncidentReportingDaoTest.class);
	
	@Autowired
	private IncidentReportingDaoImpl incidentReportingDaoImpl;

	@Test
	@DirtiesContext
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
