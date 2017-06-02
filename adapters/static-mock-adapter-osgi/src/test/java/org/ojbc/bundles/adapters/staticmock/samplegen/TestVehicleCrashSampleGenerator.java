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
package org.ojbc.bundles.adapters.staticmock.samplegen;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Ignore;
import org.junit.Test;
import org.ojbc.bundles.adapters.staticmock.samplegen.staticname.vehiclecrash.VehicleCrashMatthewsSampleGenerator;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class TestVehicleCrashSampleGenerator extends AbstractSampleGeneratorTestCase{
	
	@Test
	public void testGenerateSample() throws Exception{
				
		VehicleCrashSampleGenerator vehicleCrashSampleGenerator = new VehicleCrashSampleGenerator();
		
		List<Document> vehicleCrashSampleList = vehicleCrashSampleGenerator.generateVehicleCrashDetailSamples(5);
		
		Assert.assertEquals(5, vehicleCrashSampleList.size());

		List<String> extraXsdPathList = Arrays.asList(
				"ssp/Vehicle_Crash_Query_Results/artifacts/service_model/information_model/IEPD/xsd/impl/maine/vehicle_crash_codes.xsd");
		
		//TODO: Fix validate instance below.  Generated document is not schema valid
		for(Document iVehicleDoc : vehicleCrashSampleList){

			//XmlUtils.validateInstance("ssp/Vehicle_Crash_Query_Results/artifacts/service_model/information_model/IEPD/xsd", 
			//		"Subset/niem", "exchange.xsd", extraXsdPathList, iVehicleDoc);
		}		
	}
	
	@Test
	public void testJoeyMatthewsSampleGenerator() throws Exception{
		
		VehicleCrashSampleGenerator vehicleCrashMatthewsSampleGenerator = new VehicleCrashMatthewsSampleGenerator();
		
		List<Document> vehicleCrashSampleList = vehicleCrashMatthewsSampleGenerator
				.generateVehicleCrashDetailSamples(5);
		
		Assert.assertEquals(5, vehicleCrashSampleList.size());

		List<String> extraXsdPathList = Arrays.asList(
				"ssp/Vehicle_Crash_Query_Results/artifacts/service_model/information_model/IEPD/xsd/impl/maine/vehicle_crash_codes.xsd");
		
		//TODO: Fix validate instance below.  Generated document is not schema valid
		for(Document iVehicleDoc : vehicleCrashSampleList){

			//XmlUtils.validateInstance("ssp/Vehicle_Crash_Query_Results/artifacts/service_model/information_model/IEPD/xsd", 
			//		"Subset/niem", "exchange.xsd", extraXsdPathList, iVehicleDoc);
		}			
	}
	
	
	
	
	
	
	
	@Override
	protected String getSchemaRootFolderName() {

		return null;
	}

	@Override
	protected String getIEPDRootPath() {

		return null;
	}

	@Override
	protected String getRootSchemaFileName() {

		return null;
	}
	
}

