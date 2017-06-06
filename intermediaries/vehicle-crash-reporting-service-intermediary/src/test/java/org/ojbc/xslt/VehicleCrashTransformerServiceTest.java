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
package org.ojbc.xslt;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.xml.transform.sax.SAXSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.xml.sax.InputSource;

public class VehicleCrashTransformerServiceTest {

	private static final Log log = LogFactory.getLog(VehicleCrashTransformerServiceTest.class);
	
	XsltTransformerService unit;

	@Before
	public void setup() {
		unit = new XsltTransformerService();
	}
	
	@After
	public void tearDown() {
		unit = null;
	}	

	@Test
	public void vehicleCrashTransform() throws Exception{
				
		String xml = XmlUtils.getRootNodeAsString("src/test/resources/xmlInstances/vehicleCrashReporting/VehicleCrashReport.xml");
				
		String xslt = FileUtils.readFileToString(new File("src/main/resources/xslt/vehicleCrashToNotification.xsl"));

		transformAndValidate(xslt, xml,"src/test/resources/xmlInstances/output/VehicleCrashReport.out.xml", null);
	}

	@SuppressWarnings("unchecked")
	private void transformAndValidate(String xslPath, String inputXmlPath, String expectedResultsFilePath, 
			Map<String,Object> params) throws Exception {
				
		String sXmlRoot = XmlUtils.getRootNodeAsString(expectedResultsFilePath);
		
		String transformedXml = unit.transform(createSource(inputXmlPath), createSource(xslPath), params);		
		
		log.debug("Transformed XML:" + transformedXml);
		
		Diff diff = new Diff(sXmlRoot, transformedXml);
		
		DetailedDiff detailedDiff = new DetailedDiff(diff);
		
		List<Difference> difList = detailedDiff.getAllDifferences();
		
		assertEquals(detailedDiff.toString(), 0, difList.size());		
	}

	private SAXSource createSource(String xml) {
		InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));
		inputSource.setEncoding(org.apache.commons.lang.CharEncoding.UTF_8);
		return new SAXSource(inputSource);
	}
	
}

