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
package org.ojbc.bundles.adapters.staticmock;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class AbstractStaticMockTest {

	protected Log log;
	protected DocumentBuilder documentBuilder;
	protected StaticMockQuery staticMockQuery;

	@Before
	public void setUp() throws Exception {
		log = LogFactory.getLog(getClass());
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		documentBuilder = dbf.newDocumentBuilder();
		staticMockQuery = new StaticMockQuery("XpathTestSamples/CriminalHistory", "XpathTestSamples/Warrant", "XpathTestSamples/Incident", 
				"XpathTestSamples/FirearmRegistration", "XpathTestSamples/JuvenileHistory", "XpathTestSamples/Custody", "XpathTestSamples/CourtCase", 
				"XpathTestSamples/VehicleCrash");				
	}

	protected Document buildBasePersonSearchRequestMessagePersonNameOnly(String systemId) throws Exception {
		Document personSearchRequestMessage = buildBasePersonSearchRequestMessage(systemId);
		Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
		NodeList children = personElement.getChildNodes();
		int childCount = children.getLength();
		for (int i = childCount - 1; i >= 0; i--) {
			Node child = children.item(i);
			if (!("PersonName".equals(child.getLocalName()))) {
				personElement.removeChild(child);
			}
		}
		// XmlUtils.printNode(personSearchRequestMessage);
		return personSearchRequestMessage;
	}

	protected Document buildIncidentPersonSearchRequestMessage(int i) throws Exception {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource r = resolver.getResource("TestRequestMessages/Incident_Person_Search_Request.xml");
		Document ret = documentBuilder.parse(r.getInputStream());
		Element idElement = (Element) XmlUtils.xPathNodeSearch(ret, "/isr-doc:IncidentPersonSearchRequest/nc:Person/nc:PersonOtherIdentification/nc:IdentificationID");
		idElement.setTextContent(String.valueOf(i));
		return ret;
	}

	protected Document buildBaseIncidentVehicleSearchRequest(String vin) throws Exception {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource r = resolver.getResource("TestRequestMessages/BaseIncidentVehicleSearchRequest.xml");
		Document ret = documentBuilder.parse(r.getInputStream());
		Element vinElement = (Element) XmlUtils.xPathNodeSearch(ret, "/isr-doc:IncidentVehicleSearchRequest/ivsr:Vehicle/ivsr:VehicleSystemIdentification/nc:IdentificationID");
		vinElement.setTextContent(vin);
		return ret;
	}

	protected Document buildBaseVehicleSearchRequest() throws Exception {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource r = resolver.getResource("TestRequestMessages/BaseVehicleSearchRequest.xml");
		Document ret = documentBuilder.parse(r.getInputStream());
		return ret;
	}

	protected Element removeElement(Document d, String xPath) throws Exception {
		Node n = XmlUtils.xPathNodeSearch(d, xPath);
		Node parent = n.getParentNode();
		parent.removeChild(n);
		return (Element) parent;
	}

	protected Document buildBaseFirearmSearchRequestMessage() throws Exception {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource r = resolver.getResource("TestRequestMessages/BaseFirearmSearchRequest.xml");
		Document ret = documentBuilder.parse(r.getInputStream());
		return ret;
	}

	protected Document buildBasePersonSearchRequestMessage(String systemId) throws Exception {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource r = resolver.getResource("TestRequestMessages/BasePersonSearchRequest.xml");
		Document ret = documentBuilder.parse(r.getInputStream());
		Element systemElement = (Element) XmlUtils.xPathNodeSearch(ret.getDocumentElement(), "psr:SourceSystemNameText");
		systemElement.setTextContent(systemId);
		return ret;
	}
	
	protected Document buildCustodySearchRequestMessage(String systemId) throws Exception{
	
		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		
		Resource resource = resourceResolver.getResource("TestRequestMessages/CustodySearchRequest.xml");
		
		Document custodySearchRequestDoc = documentBuilder.parse(resource.getInputStream());
				
		return custodySearchRequestDoc;
	}
	
	
	protected Document buildCourtCaseSearchRequestMessage(String systemId) throws SAXException, IOException{
		
		PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
		
		Resource resource = resourceResolver.getResource("TestRequestMessages/CourtCaseSearchRequest.xml");
		
		Document courtCaseSearchRequestDoc = documentBuilder.parse(resource.getInputStream());
		
		return courtCaseSearchRequestDoc;		
	}
	

	protected Document buildJuvenilePersonSearchRequestMessage(String systemId) throws Exception {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource r = resolver.getResource("TestRequestMessages/JuvenilePersonSearchRequest.xml");
		Document ret = documentBuilder.parse(r.getInputStream());
		Element systemElement = (Element) XmlUtils.xPathNodeSearch(ret.getDocumentElement(), "psr:SourceSystemNameText");
		systemElement.setTextContent(systemId);
		return ret;
	}

	protected Document buildBaseIncidentSearchRequestMessage() throws Exception {
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource r = resolver.getResource("TestRequestMessages/BaseIncidentSearchRequest.xml");
		Document ret = documentBuilder.parse(r.getInputStream());
		return ret;
	}

	protected Document buildFirearmQueryRequestMessage(String system, String identifier) throws Exception {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document ret = dbf.newDocumentBuilder().newDocument();

		Element root = ret.createElementNS(OjbcNamespaceContext.NS_FIREARM_REGISTRATION_QUERY_REQUEST_DOC, "FirearmRegistrationRequest");
		ret.appendChild(root);
		root.setPrefix(OjbcNamespaceContext.NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_DOC);

		Element id = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_FIREARM_REGISTRATION_QUERY_REQUEST_EXT, "FirearmRegistrationIdentification");
		Element e = XmlUtils.appendElement(id, OjbcNamespaceContext.NS_NC, "IdentificationID");
		e.setTextContent(identifier);
		e = XmlUtils.appendElement(id, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
		e.setTextContent(system);
		XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);

		return ret;

	}

	protected Document buildPersonQueryRequestMessage(String system, String identifier) throws Exception {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document ret = dbf.newDocumentBuilder().newDocument();

		if (StaticMockQuery.INCIDENT_MOCK_ADAPTER_QUERY_SYSTEM_ID.equals(system)) {

			Element root = ret.createElementNS(OjbcNamespaceContext.NS_INCIDENT_QUERY_REQUEST_DOC, "IncidentIdentifierIncidentReportRequest");
			ret.appendChild(root);
			String prefix = XmlUtils.OJBC_NAMESPACE_CONTEXT.getPrefix(OjbcNamespaceContext.NS_INCIDENT_QUERY_REQUEST_DOC);
			root.setPrefix(prefix);

			Element id = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_INCIDENT_QUERY_REQUEST_EXT, "Incident");
			Element e = XmlUtils.appendElement(id, OjbcNamespaceContext.NS_NC, "ActivityIdentification");
			e = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_NC, "IdentificationID");
			e.setTextContent(identifier);
			e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_INCIDENT_QUERY_REQUEST_EXT, "SourceSystemNameText");
			e.setTextContent(system);
			XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);

		} else if (StaticMockQuery.FIREARM_MOCK_ADAPTER_QUERY_BY_PERSON_SYSTEM_ID.equals(system)) {

			Element root = ret.createElementNS(OjbcNamespaceContext.NS_FIREARM_REGISTRATION_QUERY_REQUEST_DOC, "PersonFirearmRegistrationRequest");
			ret.appendChild(root);
			root.setPrefix(OjbcNamespaceContext.NS_PREFIX_FIREARM_REGISTRATION_QUERY_REQUEST_DOC);

			Element id = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_FIREARM_REGISTRATION_QUERY_REQUEST_EXT, "PersonFirearmRegistrationIdentification");
			Element e = XmlUtils.appendElement(id, OjbcNamespaceContext.NS_NC, "IdentificationID");
			e.setTextContent(identifier);
			e = XmlUtils.appendElement(id, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
			e.setTextContent(system);
			XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);

		} else if (StaticMockQuery.JUVENILE_HISTORY_MOCK_ADAPTER_QUERY_SYSTEM_ID.equals(system)) {
			Element root = ret.createElementNS(OjbcNamespaceContext.NS_JUVENILE_HISTORY_QUERY_REQUEST_DOC, "JuvenileHistoryQuery");
			ret.appendChild(root);
			root.setPrefix(OjbcNamespaceContext.NS_PREFIX_JUVENILE_HISTORY_QUERY_REQUEST_DOC);
			Element e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileHistoryQueryCriteria");
			Element id = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationRecordID");
			e = XmlUtils.appendElement(id, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
			e.setTextContent(identifier);
			e = XmlUtils.appendElement(id, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText");
			e.setTextContent(system);
			XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);
		} else {

			Element root = ret.createElementNS(OjbcNamespaceContext.NS_PERSON_QUERY_REQUEST, "PersonRecordRequest");
			ret.appendChild(root);
			String prefix = XmlUtils.OJBC_NAMESPACE_CONTEXT.getPrefix(OjbcNamespaceContext.NS_PERSON_QUERY_REQUEST);
			root.setPrefix(prefix);

			Element id = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_PERSON_QUERY_REQUEST, "PersonRecordRequestIdentification");
			Element e = XmlUtils.appendElement(id, OjbcNamespaceContext.NS_NC, "IdentificationID");
			e.setTextContent(identifier);
			e = XmlUtils.appendElement(id, OjbcNamespaceContext.NS_NC, "IdentificationSourceText");
			e.setTextContent(system);
			XmlUtils.OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);

		}

		return ret;
	}

	protected Document buildFullResultsPersonSearchRequest() throws Exception {
		
		Document personSearchRequestMessage = buildBasePersonSearchRequestMessagePersonNameOnly(StaticMockQuery.CRIMINAL_HISTORY_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
		
		Element personElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:Person");
		Element personNameElement = (Element) XmlUtils.xPathNodeSearch(personElement, "nc:PersonName");
		Element lastNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonSurName");
		lastNameElement.setTextContent("Ivey");
		Element firstNameElement = (Element) XmlUtils.xPathNodeSearch(personNameElement, "nc:PersonGivenName");
		firstNameElement.setTextContent("Larry");
		
		Element sourceSystemTextElement = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest/psr:SourceSystemNameText");
		
		Element root = (Element) XmlUtils.xPathNodeSearch(personSearchRequestMessage, "psr-doc:PersonSearchRequest");
		
		sourceSystemTextElement = XmlUtils.insertElementBefore(root, sourceSystemTextElement, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, "SourceSystemNameText");		
		sourceSystemTextElement.setTextContent(StaticMockQuery.WARRANT_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
		
		sourceSystemTextElement = XmlUtils.insertElementBefore(root, sourceSystemTextElement, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, "SourceSystemNameText");		
		sourceSystemTextElement.setTextContent(StaticMockQuery.INCIDENT_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
		
		sourceSystemTextElement = XmlUtils.insertElementBefore(root, sourceSystemTextElement, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, "SourceSystemNameText");		
		sourceSystemTextElement.setTextContent(StaticMockQuery.FIREARM_MOCK_ADAPTER_SEARCH_SYSTEM_ID);
		
		Element custodySourceSystemElement = XmlUtils.insertElementBefore(root, sourceSystemTextElement, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, "SourceSystemNameText");		
		custodySourceSystemElement.setTextContent(StaticMockQuery.CUSTODY_SEARCH_SYSTEM_ID);
		
		Element courtCaseSourceSystemElement = XmlUtils.insertElementBefore(root, custodySourceSystemElement, OjbcNamespaceContext.NS_PERSON_SEARCH_REQUEST_EXT, "SourceSystemNameText");		
		courtCaseSourceSystemElement.setTextContent(StaticMockQuery.COURT_CASE_SEARCH_SYSTEM_ID);
		
		XmlUtils.printNode(personSearchRequestMessage);
	
		
		return personSearchRequestMessage;
	}

}


