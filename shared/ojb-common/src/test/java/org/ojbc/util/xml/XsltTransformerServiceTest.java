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
package org.ojbc.util.xml;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.sax.SAXSource;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

public class XsltTransformerServiceTest {

	private XsltTransformer unit;

	@Before
	public void setup() {
		unit = new XsltTransformer();
	}

	@Test
	public void returnNullWhenXmlIsNull() {
		
		String expectedXml = unit.transform(null, null,null);

		assertEquals(expectedXml, null);
	}

	@Test
	public void throwsExceptionWhenXmlIsBad() {
		
		try {
			unit.transform(createSource("A really bad xml"), null,null);
			fail();
		} catch (RuntimeException ex) {
			assertTrue(ex.getMessage().startsWith("An error occured when applying XSLT - "));			
		}

	}

	@Test
	public void identityTransformWhenXslIsNull() {
		
		SAXSource xml = createSource("<cars ></cars>");

		String expectedXml = unit.transform(xml, null,null);

		assertEquals(expectedXml, "<?xml version=\"1.0\" encoding=\"UTF-8\"?><cars/>");
	}

	@Test
	public void simpleTransform() throws Exception{
		
		String xml = FileUtils.readFileToString(new File( "src/test/resources/xml/simple/simpleXml.xml"));
		String xslt = FileUtils.readFileToString(new File("src/test/resources/xml/simple/simpleXmlTransform.xsl"));
		String expectedXml = unit.transform(createSource(xml), createSource(xslt),null);

		assertTrue(expectedXml.contains("<div>Maker:Tella</div>"));
		assertTrue(expectedXml.contains("<div>Maker:Mini</div>"));
	}

	@Test
	public void simpleTransformWithParameters() throws Exception{
		
		String xml = "<xml></xml>";
		String xslt = FileUtils.readFileToString(new File("src/test/resources/xml/simple/simpleXmlTransformWithParams.xsl"));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("param1","value1");
		params.put("param2", 2);
		String expectedXml = unit.transform(createSource(xml), createSource(xslt),params );
		
		assertTrue(expectedXml.contains("<div>Parameter 1: value1</div>"));
		assertTrue(expectedXml.contains("<div>Parameter 2: 2</div>"));
	}

	
	private SAXSource createSource(String xml) {
		
		InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));
		inputSource.setEncoding(CharEncoding.UTF_8);
		return new SAXSource(inputSource);
	}
	
}
