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
package org.ojbc.web.portal.controllers.helpers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.web.model.incident.search.IncidentSearchRequest;
import org.ojbc.web.portal.controllers.dto.IncidentSearchCommand;

public class IncidentSearchCommandUtilsTest {
	
	private IncidentSearchCommandUtils unit;
	private IncidentSearchCommand incidentSearchCommand;
	private IncidentSearchRequest advanceSearch;
	
	@Before
	public void setUp() {
		unit = new IncidentSearchCommandUtils();
		
		incidentSearchCommand = new IncidentSearchCommand();
		advanceSearch = new IncidentSearchRequest();
		incidentSearchCommand.setAdvanceSearch(advanceSearch);
	}

	@Test
	public void deepClone() {
		List<String> sourceSystems = Arrays.asList("src1", "src2");
		DateTime startDate = new DateTime();
		DateTime endDate = new DateTime();
		
		IncidentSearchRequest request = new IncidentSearchRequest();
		request.setSourceSystems(sourceSystems);
		request.setIncidentCityTown("cityTown");
		request.setIncidentNumber("number");
		request.setIncidentType("type");
		request.setIncidentDateRangeStart(startDate);
		request.setIncidentDateRangeEnd(endDate);
		
		IncidentSearchRequest clone = unit.cloneIncidentSearchRequest(request);
		assertThat(clone.getIncidentCityTown(), is("cityTown"));
		assertThat(clone.getIncidentNumber(), is("number"));
		assertThat(clone.getIncidentType(), is("type"));
		assertThat(clone.getIncidentDateRangeStart(), is(startDate));
		assertThat(clone.getIncidentDateRangeEnd(), is(endDate));
		
		assertThat(clone.getSourceSystems(), is(sourceSystems));
		
		assertNotSame(clone.getSourceSystems(), sourceSystems);
	}

}
