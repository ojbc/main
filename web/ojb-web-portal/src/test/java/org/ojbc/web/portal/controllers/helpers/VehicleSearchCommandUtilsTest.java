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
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ojbc.web.model.vehicle.search.VehicleSearchRequest;
import org.ojbc.web.portal.controllers.dto.VehicleSearchCommand;

public class VehicleSearchCommandUtilsTest {
	
	private VehicleSearchCommandUtils unit;
	private VehicleSearchCommand vehicleSearchCommand;
	private VehicleSearchRequest advanceSearch;
	
	@Before
	public void setUp() {
		unit = new VehicleSearchCommandUtils();
		
		vehicleSearchCommand = new VehicleSearchCommand();
		advanceSearch = new VehicleSearchRequest();
		vehicleSearchCommand.setAdvanceSearch(advanceSearch);
	}

	@Test
	public void deepClone() {
		List<String> sourceSystems = Arrays.asList("src1", "src2");
		
		VehicleSearchRequest request = new VehicleSearchRequest();
		request.setSourceSystems(sourceSystems);
		request.setVehicleColor("color");
		request.setVehicleMake("make");
		request.setVehicleModel("model");
		request.setVehiclePlateNumber("plateNumber");
		request.setVehicleVIN("vin");
		request.setVehicleYearRangeStart(1999);
		request.setVehicleYearRangeEnd(2002);
		
		VehicleSearchRequest clone = unit.cloneVehicleSearchRequest(request);
		assertThat(clone.getVehicleColor(), is("color"));
		assertThat(clone.getVehicleMake(), is("make"));
		assertThat(clone.getVehicleModel(), is("model"));
		assertThat(clone.getVehiclePlateNumber(), is("plateNumber"));
		assertThat(clone.getVehicleVIN(), is("vin"));
		assertThat(clone.getVehicleYearRangeStart(), is(1999));
		assertThat(clone.getVehicleYearRangeEnd(), is(2002));
		assertThat(clone.getSourceSystems(), is(sourceSystems));
		
		assertNotSame(clone.getSourceSystems(), sourceSystems);
	}

}
