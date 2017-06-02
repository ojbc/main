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
package org.ojbc.web.model.incident.search;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;

public class IncidentSearchRequestTestUtils {
	public static IncidentSearchRequest createIncidentSearchRequestModel() {
		List<String> sourceSystems = Arrays.asList("System 1", "System 2");
		
		IncidentSearchRequest request = new IncidentSearchRequest();
		request.setIncidentCityTown("Brattleboro");
		request.setIncidentDateRangeStart(new DateTime(2013, 4, 1, 0, 0, 0, 0));
		request.setIncidentDateRangeEnd(new DateTime(2013, 4, 5, 0, 0, 0, 0));
		request.setIncidentType("Law");
		request.setIncidentNumber("12345");
		request.setOnBehalfOf("John Doe");
		request.setPurpose("Criminal Justice");
		request.setSourceSystems(sourceSystems);
		
		return request;
	}

	public static IncidentSearchRequest createIncidentSearchRequestModelSameDate() {
		List<String> sourceSystems = Arrays.asList("System 1", "System 2");
		
		IncidentSearchRequest request = new IncidentSearchRequest();
		request.setIncidentCityTown("Brattleboro");
		request.setIncidentDateRangeStart(new DateTime(2013, 4, 1, 0, 0, 0, 0));
		request.setIncidentDateRangeEnd(new DateTime(2013, 4, 1, 0, 0, 0, 0));
		request.setIncidentType("Law");
		request.setIncidentNumber("12345");
		request.setOnBehalfOf("John Doe");
		request.setPurpose("Criminal Justice");
		request.setSourceSystems(sourceSystems);
		
		return request;
	}

	public static IncidentSearchRequest createIncidentSearchRequestModelOnlyStartDate() {
		List<String> sourceSystems = Arrays.asList("System 1", "System 2");
		
		IncidentSearchRequest request = new IncidentSearchRequest();
		request.setIncidentCityTown("Brattleboro");
		request.setIncidentDateRangeStart(new DateTime(2013, 4, 1, 0, 0, 0, 0));
		request.setIncidentType("Law");
		request.setIncidentNumber("12345");
		request.setOnBehalfOf("John Doe");
		request.setPurpose("Criminal Justice");
		request.setSourceSystems(sourceSystems);
		
		return request;
	}

}
