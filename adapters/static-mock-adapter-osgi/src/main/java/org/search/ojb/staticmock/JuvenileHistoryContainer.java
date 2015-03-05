package org.search.ojb.staticmock;

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
	
	public Document buildReferralHistoryDocument() throws Exception {
		String messageSpecificChildXPath = "/jh-container:JuvenileHistoryContainer/nc30:Referral/nc30:ReferralPerson";
		String messageSpecificLocationXPath = null;
		String messageSpecificNodeXPath = "/jh-container:JuvenileHistoryContainer/nc30:Referral";
		return buildMessageSpecificDocument(messageSpecificChildXPath, messageSpecificLocationXPath, messageSpecificNodeXPath, OjbcNamespaceContext.NS_JUVENILE_HISTORY_REFERRAL_EXT, "JuvenileReferralHistory");
	}

	public Document buildIntakeHistoryDocument() throws Exception {
		String messageSpecificChildXPath = "/jh-container:JuvenileHistoryContainer/cyfs:JuvenileIntakeAssessment/cyfs:Juvenile";
		String messageSpecificLocationXPath = null;
		String messageSpecificNodeXPath = "/jh-container:JuvenileHistoryContainer/cyfs:JuvenileIntakeAssessment";
		return buildMessageSpecificDocument(messageSpecificChildXPath, messageSpecificLocationXPath, messageSpecificNodeXPath, OjbcNamespaceContext.NS_JUVENILE_HISTORY_INTAKE_EXT, "JuvenileIntakeHistory");
	}

	public Document buildHearingHistoryDocument() throws Exception {
		String messageSpecificChildXPath = "/jh-container:JuvenileHistoryContainer/cyfs:CourtCase/jxdm50:CaseAugmentation/jxdm50:CaseDefendantParty";
		String messageSpecificLocationXPath = null;
		String messageSpecificNodeXPath = "/jh-container:JuvenileHistoryContainer/cyfs:CourtCase";
		return buildMessageSpecificDocument(messageSpecificChildXPath, messageSpecificLocationXPath, messageSpecificNodeXPath, OjbcNamespaceContext.NS_JUVENILE_HISTORY_HEARING_EXT, "JuvenileHearingHistory");
	}

	public Document buildPlacementHistoryDocument() throws Exception {
		String messageSpecificChildXPath = "/jh-container:JuvenileHistoryContainer/cyfs:JuvenilePlacement/jh-placement:JuvenilePlacementAugmentation/cyfs:JuvenilePlacementFacilityAssociation/cyfs:PlacedJuvenile";
		String messageSpecificLocationXPath = "/jh-container:JuvenileHistoryContainer/cyfs:JuvenilePlacement/jh-placement:JuvenilePlacementAugmentation/cyfs:JuvenilePlacementFacilityAssociation/cyfs:PlacementFacility/nc30:FacilityLocation";
		String messageSpecificNodeXPath = "/jh-container:JuvenileHistoryContainer/cyfs:JuvenilePlacement";
		return buildMessageSpecificDocument(messageSpecificChildXPath, messageSpecificLocationXPath, messageSpecificNodeXPath, OjbcNamespaceContext.NS_JUVENILE_HISTORY_PLACEMENT_EXT, "JuvenilePlacementHistory");
	}

	public Document buildCasePlanHistoryDocument() throws Exception {
		String messageSpecificChildXPath = "/jh-container:JuvenileHistoryContainer/cyfs:ParentChildAssociation/cyfs:Child";
		String messageSpecificLocationXPath = null;
		String messageSpecificNodeXPath = "/jh-container:JuvenileHistoryContainer/jh-case-plan:AssessmentIndicator | /jh-container:JuvenileHistoryContainer/jh-case-plan:CasePlanIndicator";
		return buildMessageSpecificDocument(messageSpecificChildXPath, messageSpecificLocationXPath, messageSpecificNodeXPath, OjbcNamespaceContext.NS_JUVENILE_HISTORY_CASE_PLAN_EXT, "JuvenileCasePlanHistory");
	}

	public Document buildOffenseHistoryDocument() throws Exception {
		String messageSpecificChildXPath = "/jh-container:JuvenileHistoryContainer/jxdm50:OffenseChargeAssociation/jxdm50:Charge/jxdm50:ChargeSubject";
		String messageSpecificLocationXPath = "/jh-container:JuvenileHistoryContainer/jxdm50:OffenseLocationAssociation/nc30:Location";
		String messageSpecificNodeXPath = "/jh-container:JuvenileHistoryContainer/jxdm50:OffenseChargeAssociation";
		Document ret = buildMessageSpecificDocument(messageSpecificChildXPath, messageSpecificLocationXPath, messageSpecificNodeXPath, OjbcNamespaceContext.NS_JUVENILE_HISTORY_OFFENSE_EXT, "JuvenileOffenseHistory");
		Element root = ret.getDocumentElement();
		NodeList offenseLocationAssociationNodes = XmlUtils.xPathNodeListSearch(containerDocument, "/jh-container:JuvenileHistoryContainer/jxdm50:OffenseLocationAssociation");
		for (int i=0;i < offenseLocationAssociationNodes.getLength();i++) {
			root.appendChild(ret.adoptNode(offenseLocationAssociationNodes.item(i)));
		}
		return ret;
	}

	protected Document buildMessageSpecificDocument(String messageSpecificChildXPath, String messageSpecificLocationXPath, String messageSpecificNodeXPath, String rootElementNamespaceURI, String rootElementName) throws Exception {
		Document ret = documentBuilder.newDocument();
		
		Element root = ret.createElementNS(rootElementNamespaceURI, rootElementName);
		root.setPrefix(namespaceContext.getPrefix(rootElementNamespaceURI));
		ret.appendChild(root);
		
		Element e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationAvailabilityCode");
		e.setTextContent("FOUND");
		
		Element containerAvailabilityMetadataElement = (Element) XmlUtils.xPathNodeSearch(containerDocument, "/jh-container:JuvenileHistoryContainer/jh-ext:JuvenileInformationAvailabilityMetadata");
		root.appendChild(ret.adoptNode(containerAvailabilityMetadataElement));
		
		// TODO: Hold pending resolution to an identification structure for each object...
		//Element recordId = XmlUtils.appendElement(containerAvailabilityMetadataElement, OjbcNamespaceContext.NS_JUVENILE_HISTORY_EXT, "JuvenileInformationRecordID");
		//XmlUtils.appendElement(recordId, OjbcNamespaceContext.NS_NC_30, "IdentificationID").setTextContent(arg0)
		
		String childrenNodesXPath = "/jh-container:JuvenileHistoryContainer/nc30:Person[@s30:id = " + messageSpecificChildXPath + "/@s30:ref]";
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
			root.appendChild(ret.adoptNode(childrenNodes.item(i)));
		}
		
		for (int i=0;i < parentNodes.getLength();i++) {
			root.appendChild(ret.adoptNode(parentNodes.item(i)));
		}
		
		Set<String> locationIds = new HashSet<String>();
		
		for (int i=0;i < parentResidenceLocationNodes.getLength();i++) {
			Element parentResidenceLocationNode = (Element) parentResidenceLocationNodes.item(i);
			locationIds.add(XmlUtils.xPathStringSearch(parentResidenceLocationNode, "@s30:id"));
			root.appendChild(ret.adoptNode(parentResidenceLocationNode));
		}

		for (int i=0;i < childrenResidenceLocationNodes.getLength();i++) {
			Element childrenResidenceLocationNode = (Element) childrenResidenceLocationNodes.item(i);
			if (!locationIds.contains(XmlUtils.xPathStringSearch(childrenResidenceLocationNode, "@s30:id"))) {
				root.appendChild(ret.adoptNode(childrenResidenceLocationNode));
			}
		}
		
		if (messageSpecificLocationXPath != null) {
			NodeList locationNodes = XmlUtils.xPathNodeListSearch(containerDocument, "/jh-container:JuvenileHistoryContainer/nc30:Location[@s30:id = " + messageSpecificLocationXPath + "/@s30:ref]");
			for (int i=0;i < locationNodes.getLength();i++) {
				Element locationNode = (Element) locationNodes.item(i);
				if (!locationIds.contains(XmlUtils.xPathStringSearch(locationNode, "@s30:id"))) {
					root.appendChild(ret.adoptNode(locationNode));
				}
			}
		}

		for (int i=0;i < parentResidenceNodes.getLength();i++) {
			root.appendChild(ret.adoptNode(parentResidenceNodes.item(i)));
		}
		
		for (int i=0;i < childResidenceNodes.getLength();i++) {
			root.appendChild(ret.adoptNode(childResidenceNodes.item(i)));
		}
		
		for (int i=0;i < parentChildNodes.getLength();i++) {
			root.appendChild(ret.adoptNode(parentChildNodes.item(i)));
		}
		
		NodeList messageSpecificNodes = XmlUtils.xPathNodeListSearch(containerDocument, messageSpecificNodeXPath);
		
		for (int i=0;i < messageSpecificNodes.getLength();i++) {
			root.appendChild(ret.adoptNode(messageSpecificNodes.item(i)));
		}
		
		namespaceContext.populateRootNamespaceDeclarations(ret.getDocumentElement());
		return ret;
		
	}

}
