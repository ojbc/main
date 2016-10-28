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
package org.ojbc.utilities.opendata.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.utilities.opendata.dao.model.Charge;
import org.ojbc.utilities.opendata.dao.model.IncidentArrest;
import org.ojbc.utilities.opendata.dao.model.IncidentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/dao.xml",
		"classpath:META-INF/spring/properties-context.xml",
		"classpath:META-INF/spring/camel-context.xml",
		})
@DirtiesContext
public class TestOpenDataDAOImpl {

	private static final Log log = LogFactory.getLog(TestOpenDataDAOImpl.class);
	
    @Resource  
    private DataSource dataSource;  
	
	@Autowired
	private OpenDataDAOImpl openDataDAOImpl;
	
	@Before
	public void setUp() throws Exception {
		Assert.assertNotNull(openDataDAOImpl);
		Assert.assertNotNull(dataSource);
	}
		
	//TODO: Update h2-mock-database with sample analytics data
	@Test
	@Ignore("Requires MySQL connection to run")
	public void testSearchForIncidentsByLoadTimestamp() throws Exception
	{
		LocalDateTime julyDate = LocalDateTime.of(2016, Month.JULY, 4, 0, 0, 0);
		
		List<String> incidentArrests = openDataDAOImpl.searchForIncidentsArrestsByLoadTimestamp(julyDate);
		
		assertNotNull(incidentArrests);
		
		log.info("Incident Arrests: " + incidentArrests);
		
		assertEquals(1271, incidentArrests.size());
		
	}

	//TODO: Update h2-mock-database with sample analytics data
	@Test
	@Ignore("Requires MySQL connection to run")
	public void testReturnAllIncidentsNumbers() throws Exception
	{
		List<String> incidentNumbers = openDataDAOImpl.returnAllIncidentsNumbers();
		assertNotNull(incidentNumbers);
		
	}
	
	//TODO: Update h2-mock-database with sample analytics data
	@Test
	@Ignore("Requires MySQL connection to run")
	public void testReturnChargesFromArrest() throws Exception
	{
		List<Charge> charges = openDataDAOImpl.returnChargesFromArrest("125");
		assertNotNull(charges);
		assertEquals(2, charges.size());
		
		for (Charge charge : charges)
		{
			log.info("Charge Statute: " + charge.getArrestChargeStatute());
			assertEquals("None", charge.getInvolvedDrugDescription());
		}	
		
	}
	
	//TODO: Update h2-mock-database with sample analytics data
	@Test
	@Ignore("Requires MySQL connection to run")
	public void testReturnIncidentCategoryFromIncidentID() throws Exception
	{
		List<IncidentType> incidentTypes = openDataDAOImpl.returnIncidentTypesFromIncident("1382562");
		assertNotNull(incidentTypes);
		assertEquals(2, incidentTypes.size());
		
		for (IncidentType incidentType : incidentTypes)
		{
			log.info("Incident Category: " + incidentType.getIncidentCategoryDescription());
			log.info("Incident Type Description: " + incidentType.getIncidentTypeDescription());
		}	
		
	}
	
	@Test
	@Ignore("Requires MySQL connection to run")
	public void testSearchForIncidentByIncidentId() throws Exception
	{
		List<IncidentArrest> incidentArrests = openDataDAOImpl.searchForIncidentByIncidentId("1992147");
		assertNotNull(incidentArrests);
		assertEquals(2, incidentArrests.size());
		
		for (IncidentArrest incidentArrest : incidentArrests)
		{
			log.info("Incident Arrest ID: " + incidentArrest);
		}	
		
	}

}
