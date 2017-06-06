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

import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class TestCustodySampleGenerator extends AbstractSampleGeneratorTestCase{
	
	
	@Test
	public void testGenerateSample() throws Exception{
		
		CustodySampleGenerator custodySampleGenerator = new CustodySampleGenerator();
		
		List<Document> custodySampleList = custodySampleGenerator.generateCustodySamples(5);
		
		Assert.assertEquals(5, custodySampleList.size());
				 					
		String iepdRootPath = "ssp/Custody_Query_Results/artifacts/service_model/information_model/IEPD/xsd/";
		
		List<String> supportingSchemaList = Arrays.asList(iepdRootPath + "impl/adams_county/booking_codes.xsd");
		
		for(Document doc : custodySampleList){
		
			XmlUtils.validateInstance(iepdRootPath, "Subset/niem", "exchange.xsd", supportingSchemaList, doc);
		}		
			
		Document custodyDetailSampleDoc = custodySampleList.get(0);
		XmlUtils.printNode(custodyDetailSampleDoc);
	}
	
	
	@Override
	protected String getSchemaRootFolderName() {

		return ".";
	}

	@Override
	protected String getIEPDRootPath() {

		return "ssp/Custody_Query_Results/artifacts/service_model/information_model/IEPD/xsd/";
	}

	@Override
	protected String getRootSchemaFileName() {

		return "exchange.xsd";
	}
	
}

