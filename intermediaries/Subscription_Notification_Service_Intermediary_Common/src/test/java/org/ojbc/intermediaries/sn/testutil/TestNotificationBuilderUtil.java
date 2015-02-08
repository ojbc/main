package org.ojbc.intermediaries.sn.testutil;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ojbc.util.xml.XmlUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class TestNotificationBuilderUtil {

	public static Document getMessageBody(String pathToNotificationRequest) throws Exception {
		File inputFile = new File(pathToNotificationRequest);

		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		Document document = docBuilder.parse(inputFile);
		
		//Get the root element and strip off the soap envelope
		Node rootNode = XmlUtils.xPathNodeSearch(document, "//b-2:Notify");
		
		Document rootNodeInNewDocument = docBuilder.newDocument();
		
		rootNodeInNewDocument.appendChild(rootNodeInNewDocument.adoptNode(rootNode.cloneNode(true)));
		
		//XmlUtils.printNode(rootNodeInNewDocument);
		
		return rootNodeInNewDocument;
	}
}
