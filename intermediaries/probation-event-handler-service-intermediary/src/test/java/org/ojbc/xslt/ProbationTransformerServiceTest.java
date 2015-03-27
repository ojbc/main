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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.xslt;

//import static org.hamcrest.Matchers.is;
//import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.sax.SAXSource;

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.Diff;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ProbationTransformerServiceTest {

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
	public void probationSubscribeTransform() throws Exception{
		String xml = FileUtils.readFileToString(new File( "src/test/resources/xmlInstances/probation/probationCaseInitiation.xml"));
		String xslt = FileUtils.readFileToString(new File("src/main/resources/xslt/probationDocumentToSubscription.xsl"));

		transformAndValidate(xslt, xml,"output/subscription.xml", getParams());
	}
	
	@Test
	public void probationSubscribeNonBiometricTransform() throws Exception{
		String xml = FileUtils.readFileToString(new File( "src/test/resources/xmlInstances/probation/ProbationCase_nonBiometric.xml"));
		String xslt = FileUtils.readFileToString(new File("src/main/resources/xslt/probationDocumentToSubscription.xsl"));

		transformAndValidate(xslt, xml,"output/subscribe_nonBiometric.xml", getParams());
	}
	
	@Test
	public void probationSubscribeTransform_status1() throws Exception{
		String xml = FileUtils.readFileToString(new File( "src/test/resources/xmlInstances/probation/ProbationCase_status1.xml"));
		String xslt = FileUtils.readFileToString(new File("src/main/resources/xslt/probationDocumentToSubscription.xsl"));

		transformAndValidate(xslt, xml,"output/subscribe_status1.xml", getParams());
	}
	
	@Test
	public void probationSubscribeTransform_status2() throws Exception{
		String xml = FileUtils.readFileToString(new File( "src/test/resources/xmlInstances/probation/ProbationCase_status2.xml"));
		String xslt = FileUtils.readFileToString(new File("src/main/resources/xslt/probationDocumentToSubscription.xsl"));

		transformAndValidate(xslt, xml,"output/subscribe_status2.xml", getParams());
	}
	
	@Test
	public void probationSubscribeTransform_status3() throws Exception{
		String xml = FileUtils.readFileToString(new File( "src/test/resources/xmlInstances/probation/ProbationCase_status3.xml"));
		String xslt = FileUtils.readFileToString(new File("src/main/resources/xslt/probationDocumentToSubscription.xsl"));

		transformAndValidate(xslt, xml,"output/subscribe_status3.xml", getParams());
	}
	
	@Test
	public void probationSubscribeTransform_status4() throws Exception{
		String xml = FileUtils.readFileToString(new File( "src/test/resources/xmlInstances/probation/ProbationCase_status4.xml"));
		String xslt = FileUtils.readFileToString(new File("src/main/resources/xslt/probationDocumentToSubscription.xsl"));

		transformAndValidate(xslt, xml,"output/subscribe_status4.xml", getParams());
	}
	
	@Test
	public void probationSubscribeTransform_status5() throws Exception{
		String xml = FileUtils.readFileToString(new File( "src/test/resources/xmlInstances/probation/ProbationCase_status5.xml"));
		String xslt = FileUtils.readFileToString(new File("src/main/resources/xslt/probationDocumentToSubscription.xsl"));

		transformAndValidate(xslt, xml,"output/subscribe_status5.xml", getParams());
	}
	
	@Test
	public void probationSubscribeTransform_status6() throws Exception{
		String xml = FileUtils.readFileToString(new File( "src/test/resources/xmlInstances/probation/ProbationCase_status6.xml"));
		String xslt = FileUtils.readFileToString(new File("src/main/resources/xslt/probationDocumentToSubscription.xsl"));

		transformAndValidate(xslt, xml,"output/subscribe_status6.xml", getParams());
	}
		
	@Test
	public void probationUnSubscribeTransform_status7() throws Exception{
		String xml = FileUtils.readFileToString(new File( "src/test/resources/xmlInstances/probation/ProbationCase_status7.xml"));
		String xslt = FileUtils.readFileToString(new File("src/main/resources/xslt/probationDocumentToUnsubscription.xsl"));

		transformAndValidate(xslt, xml,"output/unsubscribe_status7.xml", getParams());
	}
	
	@Test
	public void probationUnSubscribeTransform_nonBiometricStatus7() throws Exception{
		String xml = FileUtils.readFileToString(new File( "src/test/resources/xmlInstances/probation/ProbationCase_nonBiometricStatus7.xml"));
		String xslt = FileUtils.readFileToString(new File("src/main/resources/xslt/probationDocumentToUnsubscription.xsl"));

		transformAndValidate(xslt, xml,"output/unsubscribe_nonBiometricStatus7.xml", getParams());
	}
	
	@Test
	public void probationUnSubscribeTransform_supervisionRelease() throws Exception{
		String xml = FileUtils.readFileToString(new File( "src/test/resources/xmlInstances/probation/ProbationCase_supervisionRelease.xml"));
		String xslt = FileUtils.readFileToString(new File("src/main/resources/xslt/probationDocumentToUnsubscription.xsl"));

		transformAndValidate(xslt, xml,"output/unsubscribe_supervisionRelease.xml", getParams());
	}
	
	@Test
	public void probationUnsubscribeTransform() throws Exception{
		String xml = FileUtils.readFileToString(new File( "src/test/resources/xmlInstances/probation/probationCaseTermination.xml"));
		String xslt = FileUtils.readFileToString(new File("src/main/resources/xslt/probationDocumentToUnsubscription.xsl"));

		transformAndValidate(xslt, xml,"output/unsubscription.xml", getParams());
	}
	
	@SuppressWarnings("unchecked")
	private void transformAndValidate(String xslPath, String inputXmlPath, String expectedHtmlPath, Map<String,Object> params) throws IOException {

		String expectedXml = FileUtils.readFileToString(new File("src/test/resources/xmlInstances/"+expectedHtmlPath));
		String convertResult = unit.transform(createSource(inputXmlPath), createSource(xslPath),params);
		
		System.out.println(convertResult);
		
		// XMLUnit comparison: 	
		Diff myDiff = null;		
		try {
			myDiff = new Diff(expectedXml, convertResult);
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		assertTrue("XML similar " + myDiff.toString(), myDiff.similar());
		assertTrue("XML identical " + myDiff.toString(), myDiff.identical());		
	}

	private SAXSource createSource(String xml) {
		InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));
		inputSource.setEncoding(org.apache.commons.lang.CharEncoding.UTF_8);
		return new SAXSource(inputSource);
	}

	private Map<String, Object> getParams() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("topicExpression", "theTopic");
		return map;
	}
}
