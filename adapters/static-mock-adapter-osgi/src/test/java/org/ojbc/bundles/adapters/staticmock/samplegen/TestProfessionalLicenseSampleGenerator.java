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

import java.util.List;

import org.junit.Test;
import org.ojbc.bundles.adapters.staticmock.samplegen.staticname.professionalLicense.ProfessionalLicenseMatthewsSampleGenerator;
import org.w3c.dom.Document;

import junit.framework.Assert;

public class TestProfessionalLicenseSampleGenerator extends AbstractSampleGeneratorTestCase{
	
	@Test
	public void testGenerateSample() throws Exception{
				
		ProfessionalLicenseSampleGenerator professionalLicenseSampleGenerator = new ProfessionalLicenseSampleGenerator();
		
		List<Document> professionalLicenseSampleList = professionalLicenseSampleGenerator.generateProfessionalLicenseDetailSamples(5);
		
		Assert.assertEquals(5, professionalLicenseSampleList.size());
		
		//TODO: Fix validate instance below.  Generated document is not schema valid
		for(Document iProfessionalDoc : professionalLicenseSampleList){

			//XmlUtils.validateInstance("ssp/Professional_License_Query_Results/artifacts/service_model/information_model/IEPD/xsd", 
			//		"Subset/niem", "exchange.xsd", extraXsdPathList, iProfessionalDoc);
		}		
	}
	
	@Test
	public void testJoeyMatthewsSampleGenerator() throws Exception{
		
		ProfessionalLicenseSampleGenerator professionalLicenseMatthewsSampleGenerator = new ProfessionalLicenseMatthewsSampleGenerator();
		
		List<Document> professionalLicenseSampleList = professionalLicenseMatthewsSampleGenerator
				.generateProfessionalLicenseDetailSamples(5);
		
		Assert.assertEquals(5, professionalLicenseSampleList.size());

		//TODO: Fix validate instance below.  Generated document is not schema valid
		for(Document iProfessionalDoc : professionalLicenseSampleList){

			//XmlUtils.validateInstance("ssp/Professional_License_Query_Results/artifacts/service_model/information_model/IEPD/xsd", 
			//		"Subset/niem", "exchange.xsd", extraXsdPathList, iProfessionalDoc);
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

