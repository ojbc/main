package org.ojbc.web.portal.controllers.helpers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ojbc.web.SearchFieldMetadata;
import org.ojbc.web.model.firearm.search.FirearmSearchRequest;
import org.ojbc.web.portal.controllers.dto.FirearmSearchCommand;

public class FirearmSearchCommandUtilsTest {
	
	private FirearmSearchCommandUtils unit;
	private FirearmSearchCommand firearmSearchCommand;
	private FirearmSearchRequest advanceSearch;

	@Before
	public void setUp() throws Exception {
		unit = new FirearmSearchCommandUtils();
		
		firearmSearchCommand = new FirearmSearchCommand();
		advanceSearch = new FirearmSearchRequest();
		firearmSearchCommand.setAdvanceSearch(advanceSearch);
	}

	@Test
	public void deepClone() {
		List<String> sourceSystems = Arrays.asList("src1", "src2");
		
		FirearmSearchRequest request = new FirearmSearchRequest();
		request.setFirearmCounty("county");
		request.setFirearmType("firearmType");
		request.setFirearmMake("make");
		request.setFirearmModel("model");
		request.setFirearmRegistrationNumber("regNumber");
		request.setFirearmSerialNumber("serialNumber");
		request.setFirearmSerialNumberMetaData(SearchFieldMetadata.Partial);
		request.setOnBehalfOf("onBehalfOf");
		request.setPurpose("purpose");
		request.setFirearmCurrentRegOnly(true);
		request.setSourceSystems(sourceSystems);
		
		FirearmSearchRequest clone = unit.cloneFirearmSearchRequest(request);
		
		assertThat(clone.getFirearmCounty(), is("county"));
		assertThat(clone.getFirearmType(), is("firearmType"));
		assertThat(clone.getFirearmMake(), is("make"));
		assertThat(clone.getFirearmModel(), is("model"));
		assertThat(clone.getFirearmRegistrationNumber(), is("regNumber"));
		assertThat(clone.getFirearmSerialNumber(), is("serialNumber"));
		assertThat(clone.getFirearmSerialNumberMetaData(), is(SearchFieldMetadata.Partial));
		assertThat(clone.getOnBehalfOf(), is("onBehalfOf"));
		assertThat(clone.getPurpose(), is("purpose"));
		assertThat(clone.getFirearmCurrentRegOnly(), is(true));
		assertThat(clone.getSourceSystems(), is(sourceSystems));
		
		assertNotSame(clone.getSourceSystems(), sourceSystems);
	}

}
