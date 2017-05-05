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
package org.ojbc.utilities.opendata.processor;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.utilities.opendata.IncidentArrestUtils;
import org.ojbc.utilities.opendata.dao.TestOpenDataDAOImpl;
import org.ojbc.utilities.opendata.dao.model.IncidentArrest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/properties-context.xml",
        "classpath:META-INF/spring/dao.xml"
        })
@DirtiesContext
public class TestIncrementalUpdateUtilityProcessor {

	private static final Log log = LogFactory.getLog(TestOpenDataDAOImpl.class);
	
	@Autowired
	private IncrementalUpdateUtilityProcessor incrementalUpdateUtilityProcessor;
	
	@Before
	public void setUp() throws Exception {
		Assert.assertNotNull(incrementalUpdateUtilityProcessor);
	}

	
	@Test
	public void testRetrieveAllIncidentsAndCreateJSONrequest() throws Exception
	{
		
		IncidentArrest ia = IncidentArrestUtils.returnExampleIncidentArrest();
		
		List<IncidentArrest> incidentArrests = new ArrayList<IncidentArrest>();
		incidentArrests.add(ia);
		
		List<List<IncidentArrest>> allIncidentArrests = new ArrayList<List<IncidentArrest>>();
		allIncidentArrests.add(incidentArrests);
		
		
		for (List<IncidentArrest> incidentArrestSubList : allIncidentArrests)
		{
			String json = incrementalUpdateUtilityProcessor.addIncidentArrestToJSONRequest(incidentArrestSubList);
			log.info("JSON request from sub list: " + json + "\n\n\n\n\n\n\n\n\n\n\n\n\n");
			
			String expectedJson = "[{\"row_identifier\":\"1\",\"incident_case_number\":\"IC Number\",\"reporting_agency\":\"reporting agency\",\"incident_date_time\":\"2003-12-05 08:57:12\",\"incident_location_town\":\"inc town\",\"incident_location_county\":\"incident county\",\"arrest_county\":\"arrest county\",\"age_in_years\":\"29\",\"person_race_description\":\"race\",\"arrest_drug_involved\":\"True\",\"arrest_drug_involved_description\":\"marijuana|heroin\",\"arrestee_sex_description\":\"Male\",\"arresting_agency\":\"Local PD\",\"incident_load_timestamp\":\"2016-02-19 03:18:03\",\"arrest_charges\":\"charge1|charge2\",\"incident_type_description\":\"Desc1|Desc2\",\"incident_category_description\":\"Category1|Category2\"}]";
			
			assertEquals(expectedJson,json);
		}	
		
	}
	
}
