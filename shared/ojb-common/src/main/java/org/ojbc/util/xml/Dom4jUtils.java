package org.ojbc.util.xml;

import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;

import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.dom4j.io.DocumentSource;

public class Dom4jUtils {

	private static final TransformerFactory transformerFactory = TransformerFactory.newInstance();
	
	
	public static org.w3c.dom.Document toW3c(org.dom4j.Document dom4jdoc)
			throws TransformerException {

		SAXSource source = new DocumentSource(dom4jdoc);
		DOMResult result = new DOMResult();

		Transformer transformer = transformerFactory.newTransformer();

		transformer.transform(source, result);
		return (org.w3c.dom.Document) result.getNode();
	}
	
	  /**
	   * converts a W3C DOM document into a dom4j document
	   * 
	   * @param domDocument
	   * @param namespaceUris
	   * @return
	   */
	@SuppressWarnings("rawtypes")
	public static Document buildDocment(org.w3c.dom.Document domDocument, Map namespaceUris) {
	    DOMReader xmlReader = new DOMReader();
	    xmlReader.getDocumentFactory().setXPathNamespaceURIs(namespaceUris);
	    return xmlReader.read(domDocument);
	  }
}
