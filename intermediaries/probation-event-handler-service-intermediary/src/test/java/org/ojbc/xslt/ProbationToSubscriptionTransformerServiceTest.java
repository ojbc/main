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

//import static org.hamcrest.Matchers.is;
//import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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

import org.apache.commons.io.FileUtils;
import org.custommonkey.xmlunit.Diff;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ProbationToSubscriptionTransformerServiceTest {

	
	private Document caseInitiationDocument;
	private Document caseTerminationDocument;
	private Document caseInitiationSupervisorDocument;
	private Document caseTerminationSupervisorDocument;
	
	private Source subscriptionXSLT;
	private Source unSubscriptionXSLT;

	@Before
	public void setup() throws Exception {
				
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        caseInitiationDocument = db.parse(new File("src/test/resources/xmlInstances/probation/Probation_Case_Initiation_Organization_Contact.xml"));
        caseTerminationDocument = db.parse(new File("src/test/resources/xmlInstances/probation/Probation_Case_Termination_Organization_Contact.xml"));	
        caseInitiationSupervisorDocument = db.parse(new File("src/test/resources/xmlInstances/probation/ProbationCase_supervisor.xml"));
        caseTerminationSupervisorDocument = db.parse(new File("src/test/resources/xmlInstances/probation/ProbationCase_supervisionRelease.xml"));	
        subscriptionXSLT = new StreamSource(new File("src/main/resources/xslt/probationDocumentToSubscription.xsl"));
        unSubscriptionXSLT = new StreamSource(new File("src/main/resources/xslt/probationDocumentToUnsubscription.xsl"));

	}
	
	@Test
	public void caseInitiationSupervisorToSubscriptionTransform() throws Exception{
				
		Document subscriptionTransformed = transformCaseInitiationToSubscription(caseInitiationSupervisorDocument);
		
		Node subscribeNode = XmlUtils.xPathNodeSearch(subscriptionTransformed, "/b-2:Subscribe");
		assertNotNull(subscribeNode);
		
		Node subscriptionMessageNode = XmlUtils.xPathNodeSearch(subscribeNode, "submsg-exch:SubscriptionMessage");
		assertNotNull(subscriptionMessageNode);
		
		Node officerEmailNode = XmlUtils.xPathNodeSearch(subscriptionMessageNode, "nc:ContactEmailID[2]");
		String officerEmailValue = officerEmailNode.getTextContent();
		assertEquals("officer2.email@local.gov", officerEmailValue);
		
		Node supervisorEmailNode = XmlUtils.xPathNodeSearch(subscriptionMessageNode, "nc:ContactEmailID[1]");
		String supervisorEmailValue = supervisorEmailNode.getTextContent();
		assertEquals("officer1.email@local.gov", supervisorEmailValue);
		
		Node subjectNode = XmlUtils.xPathNodeSearch(subscriptionMessageNode, "submsg-ext:Subject");
		assertNotNull(subjectNode);
		
		Node subjectDOBNode = XmlUtils.xPathNodeSearch(subjectNode, "nc:PersonBirthDate/nc:Date");
		String subjectDOBValue = subjectDOBNode.getTextContent();
		assertEquals("1943-02-22", subjectDOBValue);
		
		Node subjectNameNode = XmlUtils.xPathNodeSearch(subjectNode, "nc:PersonName");
		assertNotNull(subjectNameNode);
		
		Node subjectFirstNameNode = XmlUtils.xPathNodeSearch(subjectNameNode, "nc:PersonGivenName");
		String subjectFirstNameValue = subjectFirstNameNode.getTextContent();
		assertEquals("Given", subjectFirstNameValue);
		
		Node subjectLastNameNode = XmlUtils.xPathNodeSearch(subjectNameNode, "nc:PersonSurName");
		String subjectLastNameValue = subjectLastNameNode.getTextContent();
		assertEquals("Sur", subjectLastNameValue);
		
		Node subjectSIDNode = XmlUtils.xPathNodeSearch(subjectNode, "jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
		String subjectSIDValue = subjectSIDNode.getTextContent();
		assertEquals("A9999999", subjectSIDValue);
		
		Node systemNameNode = XmlUtils.xPathNodeSearch(subscriptionMessageNode, "submsg-ext:SystemName");
		String systemNameValue = systemNameNode.getTextContent();
		assertEquals("systemUri", systemNameValue);
		
		Node subscriptionQualiferNode = XmlUtils.xPathNodeSearch(subscriptionMessageNode, "submsg-ext:SubscriptionQualifierIdentification/nc:IdentificationID");
		String subscriptionQualifierValue = subscriptionQualiferNode.getTextContent();
		assertEquals("999999", subscriptionQualifierValue);
		
		Node startDateNode = XmlUtils.xPathNodeSearch(subscriptionMessageNode, "nc:DateRange/nc:StartDate/nc:Date");
		String startDateValue = startDateNode.getTextContent();
		assertEquals("2012-06-04", startDateValue);

		XmlUtils.printNode(subscriptionTransformed);
	}
	
	@Test
	public void caseInitiationToSubscriptionTransform() throws Exception{
				
		Document subscriptionTransformed = transformCaseInitiationToSubscription(caseInitiationDocument);
		
		Node subscribeNode = XmlUtils.xPathNodeSearch(subscriptionTransformed, "/b-2:Subscribe");
		assertNotNull(subscribeNode);
		
		Node subscriptionMessageNode = XmlUtils.xPathNodeSearch(subscribeNode, "submsg-exch:SubscriptionMessage");
		assertNotNull(subscriptionMessageNode);
		
		Node organizationEmailNode = XmlUtils.xPathNodeSearch(subscriptionMessageNode, "nc:ContactEmailID");
		String emailValue = organizationEmailNode.getTextContent();
		assertEquals("test@test.com", emailValue);

		XmlUtils.printNode(subscriptionTransformed);
	}
	
	@Test
	public void caseTerminationToUnsubscriptionTransform() throws Exception{
				
		Document subscriptionTransformed = transformCaseTerminationToUnsubscription(caseTerminationDocument);
		
		Node unSubscribeNode = XmlUtils.xPathNodeSearch(subscriptionTransformed, "/b-2:Unsubscribe");
		assertNotNull(unSubscribeNode);
		
		Node unSubscriptionMessageNode = XmlUtils.xPathNodeSearch(unSubscribeNode, "unsubmsg-exch:UnsubscriptionMessage");
		assertNotNull(unSubscriptionMessageNode);
		
		Node organizationEmailNode = XmlUtils.xPathNodeSearch(unSubscriptionMessageNode, "nc:ContactEmailID[2]");
		String emailValue = organizationEmailNode.getTextContent();
		assertEquals("test@test.com", emailValue);
		
		Node officerEmailNode = XmlUtils.xPathNodeSearch(unSubscriptionMessageNode, "nc:ContactEmailID[1]");
		String officerEmailValue = officerEmailNode.getTextContent();
		assertEquals("johnjones@aol.com", officerEmailValue);
		
		Node subscriptionQualiferNode = XmlUtils.xPathNodeSearch(unSubscriptionMessageNode, "submsg-ext:SubscriptionQualifierIdentification/nc:IdentificationID");
		String subscriptionQualifierValue = subscriptionQualiferNode.getTextContent();
		assertEquals("1234", subscriptionQualifierValue);

		XmlUtils.printNode(subscriptionTransformed);
	}
	
private Document transformCaseInitiationToSubscription(Document caseInitiation) throws Exception{
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		Transformer transformer = transformerFactory.newTransformer(subscriptionXSLT);
		
		DOMResult domResult = new DOMResult();
		
		DOMSource inDocDomSource = new DOMSource(caseInitiation);
		
		transformer.transform(inDocDomSource, domResult);
		
		Document resultDoc = (Document)domResult.getNode();
			
		return resultDoc;
	}

private Document transformCaseTerminationToUnsubscription(Document caseTermination) throws Exception{
	
	TransformerFactory transformerFactory = TransformerFactory.newInstance();
	
	Transformer transformer = transformerFactory.newTransformer(unSubscriptionXSLT);
	
	DOMResult domResult = new DOMResult();
	
	DOMSource inDocDomSource = new DOMSource(caseTermination);
	
	transformer.transform(inDocDomSource, domResult);
	
	Document resultDoc = (Document)domResult.getNode();
		
	return resultDoc;
}
}
