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
