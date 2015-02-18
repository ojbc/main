package org.ojbc.util.helper;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OJBCXMLUtils {
	public static Document createDocument() throws Exception{

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().newDocument();

		return doc;
	}
	
	public static Element createElement(Document doc, String elementNamespace, String elementName) {

		Element element = doc.createElementNS(elementNamespace, elementName);
		return element;
		
	}
}
