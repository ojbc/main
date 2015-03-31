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
