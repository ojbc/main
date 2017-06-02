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
package org.ojbc.bundles.adapters.staticmock;

import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class JuvenileHistoryContainer {
	
	private Document containerDocument;
	private DocumentBuilder documentBuilder;
	private OjbcNamespaceContext namespaceContext;
	
	public JuvenileHistoryContainer(Document containerDocument) {
		try {
			if (!XmlUtils.nodeExists(containerDocument, "/jh-container:JuvenileHistoryContainer")) {
				Element documentElement = containerDocument.getDocumentElement();
				throw new IllegalArgumentException("Invalid document passed to Juvenile History Container, root element is:" +
						documentElement.getPrefix() + ":" + documentElement.getLocalName());
			}
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw ((RuntimeException) e);
			}
			throw new RuntimeException(e);
		}
		this.containerDocument = containerDocument;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			documentBuilder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
		namespaceContext = new OjbcNamespaceContext();
	}
	
	public Document getDocumentForContext(Object context) throws Exception {
		if ("JuvenileReferralHistory".equals(context)) {
			return buildReferralHistoryDocument();
		}
		if ("JuvenileIntakeHistory".equals(context)) {
			return buildIntakeHistoryDocument();
		}
		if ("JuvenilePlacementHistory".equals(context)) {
			return buildPlacementHistoryDocument();
		}
		if ("JuvenileOffenseHistory".equals(context)) {
			return buildOffenseHistoryDocument();
		}
		if ("JuvenileCasePlanHistory".equals(context)) {
			return buildCasePlanHistoryDocument();
		}
		if ("JuvenileHearingHistory".equals(context)) {
			return buildHearingHistoryDocument();
		}
		throw new IllegalArgumentException("Unknown context: " + context);
	}
	
	public Document buildReferralHistoryDocument() throws Exception {
		String messageSpecificLocationXPath = null;
		String messageSpecificNodeXPath = "/jh-container:JuvenileHistoryContainer/nc30:Referral";
		return buildMessageSpecificDocument(messageSpecificLocationXPath, messageSpecificNodeXPath, OjbcNamespaceContext.NS_JUVENILE_HISTORY_REFERRAL_EXT, "JuvenileReferralHistory");
	}

	public Document buildIntakeHistoryDocument() throws Exception {
		String messageSpecificLocationXPath = null;
		String messageSpecificNodeXPath = "/jh-container:JuvenileHistoryContainer/cyfs:JuvenileIntakeAssessment";
		return buildMessageSpecificDocument(messageSpecificLocationXPath, messageSpecificNodeXPath, OjbcNamespaceContext.NS_JUVENILE_HISTORY_INTAKE_EXT, "JuvenileIntakeHistory");
	}

	public Document buildHearingHistoryDocument() throws Exception {
		String messageSpecificLocationXPath = null;
		String messageSpecificNodeXPath = "/jh-container:JuvenileHistoryContainer/cyfs:CourtCase";
		return buildMessageSpecificDocument(messageSpecificLocationXPath, messageSpecificNodeXPath, OjbcNamespaceContext.NS_JUVENILE_HISTORY_HEARING_EXT, "JuvenileHearingHistory");
	}

	public Document buildPlacementHistoryDocument() throws Exception {
		String messageSpecificLocationXPath = "/jh-container:JuvenileHistoryContainer/cyfs:JuvenilePlacement/jh-placement:JuvenilePlacementAugmentation/cyfs:JuvenilePlacementFacilityAssociation/cyfs:PlacementFacility/nc30:FacilityLocation";
		String messageSpecificNodeXPath = "/jh-container:JuvenileHistoryContainer/cyfs:JuvenilePlacement";
		return buildMessageSpecificDocument(messageSpecificLocationXPath, messageSpecificNodeXPath, OjbcNamespaceContext.NS_JUVENILE_HISTORY_PLACEMENT_EXT, "JuvenilePlacementHistory");
	}

	public Document buildCasePlanHistoryDocument() throws Exception {
		String messageSpecificLocationXPath = null;
		String messageSpecificNodeXPath = "/jh-container:JuvenileHistoryContainer/jh-case-plan:CasePlan";
		return buildMessageSpecificDocument(messageSpecificLocationXPath, messageSpecificNodeXPath, OjbcNamespaceContext.NS_JUVENILE_HISTORY_CASE_PLAN_EXT, "JuvenileCasePlanHistory");
	}

	public Document buildOffenseHistoryDocument() throws Exception {
		String messageSpecificLocationXPath = "/jh-container:JuvenileHistoryContainer/jxdm50:OffenseLocationAssociation/nc30:Location";
		String messageSpecificNodeXPath = "/jh-container:JuvenileHistoryContainer/jxdm50:OffenseChargeAssociation";
		Document ret = buildMessageSpecificDocument(messageSpecificLocationXPath, messageSpecificNodeXPath, OjbcNamespaceContext.NS_JUVENILE_HISTORY_OFFENSE_EXT, "JuvenileOffenseHistory");
		Element root = ret.getDocumentElement();
		NodeList offenseLocationAssociationNodes = XmlUtils.xPathNodeListSearch(containerDocument, "/jh-container:JuvenileHistoryContainer/jxdm50:OffenseLocationAssociation");
		for (int i=0;i < offenseLocationAssociationNodes.getLength();i++) {
			root.appendChild(ret.importNode(offenseLocationAssociationNodes.item(i), true));
		}
		return ret;
	}

	protected Document buildMessageSpecificDocument(String messageSpecificLocationXPath, String messageSpecificNodeXPath, String rootElementNamespaceURI, String rootElementName) throws Exception {
		
		Document ret = documentBuilder.newDocument();
		
		Element root = ret.createElementNS(rootElementNamespaceURI, rootElementName);
		root.setPrefix(namespaceContext.getPrefix(rootElementNamespaceURI));
		ret.appendChild(root);
		
		Element availabilityCodeElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationAvailabilityCode");
		availabilityCodeElement.setTextContent("FOUND");
		
		Element containerAvailabilityMetadataElement = (Element) XmlUtils.xPathNodeSearch(containerDocument, "/jh-container:JuvenileHistoryContainer/jh-ext:JuvenileInformationAvailabilityMetadata");
		root.appendChild(ret.importNode(containerAvailabilityMetadataElement, true));
		
		String childrenNodesXPath = "/jh-container:JuvenileHistoryContainer/nc30:Person[@s30:id = 'child']";
		NodeList childrenNodes = XmlUtils.xPathNodeListSearch(containerDocument, childrenNodesXPath);
		
		String parentNodesXPath = "/jh-container:JuvenileHistoryContainer/nc30:Person[@s30:id = /jh-container:JuvenileHistoryContainer/cyfs:ParentChildAssociation[cyfs:Child/@s30:ref = " + childrenNodesXPath + "/@s30:id]/cyfs:Parent/@s30:ref]";
		NodeList parentNodes = XmlUtils.xPathNodeListSearch(containerDocument, parentNodesXPath);
		
		NodeList parentChildNodes = XmlUtils.xPathNodeListSearch(containerDocument, "/jh-container:JuvenileHistoryContainer/cyfs:ParentChildAssociation[cyfs:Child/@s30:ref = " + childrenNodesXPath + "/@s30:id]");
		
		String parentResidenceNodesXPath = "/jh-container:JuvenileHistoryContainer/nc30:PersonResidenceAssociation[nc30:Person/@s30:ref = " + parentNodesXPath + "/@s30:id]";
		NodeList parentResidenceNodes = XmlUtils.xPathNodeListSearch(containerDocument, parentResidenceNodesXPath);
		String childrenResidenceNodesXPath = "/jh-container:JuvenileHistoryContainer/nc30:PersonResidenceAssociation[nc30:Person/@s30:ref = " + childrenNodesXPath + "/@s30:id]";
		NodeList childResidenceNodes = XmlUtils.xPathNodeListSearch(containerDocument, childrenResidenceNodesXPath);
		
		NodeList parentResidenceLocationNodes = XmlUtils.xPathNodeListSearch(containerDocument, "/jh-container:JuvenileHistoryContainer/nc30:Location[@s30:id = " + parentResidenceNodesXPath + "/nc30:Location/@s30:ref]");
		NodeList childrenResidenceLocationNodes = XmlUtils.xPathNodeListSearch(containerDocument, "/jh-container:JuvenileHistoryContainer/nc30:Location[@s30:id = " + childrenResidenceNodesXPath + "/nc30:Location/@s30:ref]");
		
		for (int i=0;i < childrenNodes.getLength();i++) {
			root.appendChild(ret.importNode(childrenNodes.item(i), true));
		}
		
		for (int i=0;i < parentNodes.getLength();i++) {
			root.appendChild(ret.importNode(parentNodes.item(i), true));
		}
		
		Set<String> locationIds = new HashSet<String>();
		
		for (int i=0;i < parentResidenceLocationNodes.getLength();i++) {
			Element parentResidenceLocationNode = (Element) parentResidenceLocationNodes.item(i);
			locationIds.add(XmlUtils.xPathStringSearch(parentResidenceLocationNode, "@s30:id"));
			root.appendChild(ret.importNode(parentResidenceLocationNode, true));
		}

		for (int i=0;i < childrenResidenceLocationNodes.getLength();i++) {
			Element childrenResidenceLocationNode = (Element) childrenResidenceLocationNodes.item(i);
			if (!locationIds.contains(XmlUtils.xPathStringSearch(childrenResidenceLocationNode, "@s30:id"))) {
				root.appendChild(ret.importNode(childrenResidenceLocationNode, true));
			}
		}
		
		if (messageSpecificLocationXPath != null) {
			NodeList locationNodes = XmlUtils.xPathNodeListSearch(containerDocument, "/jh-container:JuvenileHistoryContainer/nc30:Location[@s30:id = " + messageSpecificLocationXPath + "/@s30:ref]");
			for (int i=0;i < locationNodes.getLength();i++) {
				Element locationNode = (Element) locationNodes.item(i);
				if (!locationIds.contains(XmlUtils.xPathStringSearch(locationNode, "@s30:id"))) {
					root.appendChild(ret.importNode(locationNode, true));
				}
			}
		}

		for (int i=0;i < parentResidenceNodes.getLength();i++) {
			root.appendChild(ret.importNode(parentResidenceNodes.item(i), true));
		}
		
		for (int i=0;i < childResidenceNodes.getLength();i++) {
			root.appendChild(ret.importNode(childResidenceNodes.item(i), true));
		}
		
		for (int i=0;i < parentChildNodes.getLength();i++) {
			root.appendChild(ret.importNode(parentChildNodes.item(i), true));
		}
		
		NodeList messageSpecificNodes = XmlUtils.xPathNodeListSearch(containerDocument, messageSpecificNodeXPath);
		
		for (int i=0;i < messageSpecificNodes.getLength();i++) {
			root.appendChild(ret.importNode(messageSpecificNodes.item(i), true));
		}
		
		if (messageSpecificNodes.getLength() == 0) {
			availabilityCodeElement.setTextContent("NOT FOUND");
		}
		
		namespaceContext.populateRootNamespaceDeclarations(ret.getDocumentElement());
		return ret;
		
	}

}
