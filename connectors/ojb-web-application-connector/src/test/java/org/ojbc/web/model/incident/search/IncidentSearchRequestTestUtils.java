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
