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
