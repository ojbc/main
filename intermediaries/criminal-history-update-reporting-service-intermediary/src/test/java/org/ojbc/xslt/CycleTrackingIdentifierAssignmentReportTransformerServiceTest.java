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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.Diff;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.custommonkey.xmlunit.XMLUnit;

import java.util.logging.Logger;

public class CycleTrackingIdentifierAssignmentReportTransformerServiceTest {
			
	private Document cycleIdDoc;
	
	private Source xsltSource;
	
	XsltTransformerService xsltTransformerService;
	
	private Logger logger = Logger.getLogger(CycleTrackingIdentifierAssignmentReportTransformerServiceTest.class.getName());

	@Before
	public void setup() throws Exception {
		
		xsltTransformerService = new XsltTransformerService();
		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
				
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        cycleIdDoc = db.parse(new File(
        		"src/test/resources/xmlInstances/cycleTrackingIdentifierAssignmentReport/Cycle-Tracking-Identifier-Assignment-Report.xml"));		
		
        xsltSource = new StreamSource(new File(
        		"src/main/resources/xslt/cycleTrackingIdentifierAssignmentReportToNotifications.xsl"));			
	}

	@Test
	public void cycleTrackingIdentifierAssignmentReportToNotificationTransform() throws Exception{
		
		String xml = FileUtils.readFileToString(new File( 
				"src/test/resources/xmlInstances/cycleTrackingIdentifierAssignmentReport/Cycle-Tracking-Identifier-Assignment-Report.xml"));
		
		String xslt = FileUtils.readFileToString(new File(
				"src/main/resources/xslt/cycleTrackingIdentifierAssignmentReportToNotifications.xsl"));

		Map<String, Object> paramsMap = new HashMap<String, Object>(1);
		paramsMap.put("systemId", "{http://ojbc.org}ProbationChCyleTrackingID");
		
		transformAndValidate(xslt, xml,"output/notifications/notification.xml", paramsMap);
	}
	
	@Test
	public void testCycleIdToNotifyTransform() throws Exception{
				
		Map<String, Object> paramsMap = new HashMap<String, Object>(1);
		paramsMap.put("systemId", "{http://ojbc.org}ProbationChCyleTrackingID");		
		
		Document notifyDocTransformed = transformCylceIdToNotifyDoc(cycleIdDoc, paramsMap);
		
		Node notifyNode = XmlUtils.xPathNodeSearch(notifyDocTransformed, "/b-2:Notify");
		
		Node notifyMesssageNode = XmlUtils.xPathNodeSearch(notifyNode, "b-2:NotificationMessage");
		
		Node messageNode = XmlUtils.xPathNodeSearch(notifyMesssageNode, "b-2:Message");
		assertNotNull(messageNode);
		
		Node notificationMesssageNode = XmlUtils.xPathNodeSearch(messageNode, "notfm-exch:NotificationMessage");
		assertNotNull(notificationMesssageNode);
		
		Node notifyingCriminalHistoryUpdateNode = XmlUtils.xPathNodeSearch(notificationMesssageNode, "notfm-ext:NotifyingCriminalHistoryUpdate");
		assertNotNull(notifyingCriminalHistoryUpdateNode);
		
		String systemNameText = XmlUtils.xPathStringSearch(notifyingCriminalHistoryUpdateNode, "notfm-ext:NotifyingActivityReportingSystemNameText");
		assertEquals("{http://ojbc.org}ProbationChCyleTrackingID", systemNameText);			
		
		Node personNode = XmlUtils.xPathNodeSearch(notificationMesssageNode, "jxdm41:Person");
		assertNotNull(personNode);
		
		Node personNameNode = XmlUtils.xPathNodeSearch(personNode, "nc:PersonName");
		assertNotNull(personNameNode);
		
		Node personGivenNameNode = XmlUtils.xPathNodeSearch(personNameNode, "nc:PersonGivenName");
		String givenName = personGivenNameNode.getTextContent();
		assertEquals("GIVEN", givenName);
				
		Node personSurNameNode = XmlUtils.xPathNodeSearch(personNameNode, "nc:PersonSurName");
		String surName = personSurNameNode.getTextContent();
		assertEquals("SUR", surName);
		
		Node cycleTrackingIdentifierAssignmentNode = XmlUtils.xPathNodeSearch(notifyingCriminalHistoryUpdateNode, "chu:CycleTrackingIdentifierAssignment");
		assertNotNull(cycleTrackingIdentifierAssignmentNode);
		
		Node cycleTrackingIdentificationNode = XmlUtils.xPathNodeSearch(cycleTrackingIdentifierAssignmentNode, "chu:CycleTrackingIdentification/nc:IdentificationID");
		String cycleTrackingIdentificationValue = cycleTrackingIdentificationNode.getTextContent();
		assertEquals("123456A", cycleTrackingIdentificationValue);
		
		//confirms there are two charge nodes
		Node chargeNode = XmlUtils.xPathNodeSearch(cycleTrackingIdentifierAssignmentNode, "jxdm41:Charge[2]");
		assertNotNull(chargeNode);
		
		Node subRefNode = XmlUtils.xPathNodeSearch(notifyMesssageNode, "b-2:SubscriptionReference");
		
		assertNotNull(subRefNode);
		
		Node topicNode = XmlUtils.xPathNodeSearch(notifyMesssageNode, "b-2:Topic[@Dialect='http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete']");
		
		String topicValue = topicNode.getTextContent();
		
		assertEquals("{http://ojbc.org/wsn/topics}:person/criminalHistoryCycleTrackingIdentifierAssignment", topicValue);
		
		Node producerRefNode = XmlUtils.xPathNodeSearch(notifyMesssageNode, "b-2:ProducerReference");
		
		Node prodRefAddNode = XmlUtils.xPathNodeSearch(producerRefNode, "add:Address");
		String prodRefAddress = prodRefAddNode.getTextContent();
		
		assertEquals("http://www.ojbc.org/criminalHistoryTrackingIdentifierAssignmentNotificationProducer", prodRefAddress);
		
		Node prodRefParamsNode = XmlUtils.xPathNodeSearch(producerRefNode, "add:ReferenceParameters");
		
		assertNotNull(prodRefParamsNode);
				
		XmlUtils.printNode(notifyDocTransformed);				
	}
	
	
	private Document transformCylceIdToNotifyDoc(Document cycleIdDoc, Map<String, Object> paramsMap) throws Exception{
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = transformerFactory.newTransformer(xsltSource);
		
		DOMResult domResult = new DOMResult();
		
		DOMSource inDocDomSource = new DOMSource(cycleIdDoc);
		
		for(String trasformParamKey : paramsMap.keySet()){
			transformer.setParameter(trasformParamKey, paramsMap.get(trasformParamKey));	
		}				
		
		transformer.transform(inDocDomSource, domResult);
		
		Document resultDoc = (Document)domResult.getNode();
			
		return resultDoc;
	}
	
	@SuppressWarnings("unchecked")
	private void transformAndValidate(String xslPath, String inputXmlPath, String expectedXMLPath, Map<String,Object> paramsMap) throws Exception {
		
		String expectedXml = FileUtils.readFileToString(new File("src/test/resources/xmlInstances/"+expectedXMLPath));
		
		String convertedResult = xsltTransformerService.transform(createSource(inputXmlPath), createSource(xslPath), paramsMap);
		
		logger.info("Converted Result:\n" + convertedResult);

		Diff diff = new Diff(expectedXml, convertedResult);
		
		assertTrue(diff.identical());
	}

	private SAXSource createSource(String xml) {
		InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));
		inputSource.setEncoding(CharEncoding.UTF_8);
		return new SAXSource(inputSource);
	}
		
}

