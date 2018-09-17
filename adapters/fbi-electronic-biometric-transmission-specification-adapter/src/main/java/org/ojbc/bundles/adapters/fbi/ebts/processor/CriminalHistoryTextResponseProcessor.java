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
package org.ojbc.bundles.adapters.fbi.ebts.processor;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.Exchange;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CriminalHistoryTextResponseProcessor {

	public Document createCriminalHistoryTextResponse(Exchange ex) throws Exception
	{
		Document doc = null;
		
		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		
		Document fbiResponseDoc = ex.getIn().getBody(Document.class);
		
		String textContent = XmlUtils.xPathStringSearch(fbiResponseDoc, "//ebts:TransactionElectronicRapSheetText");
		
		String sBase64Rapsheet = "";
		
		if (StringUtils.isNotBlank(textContent))
		{	
			sBase64Rapsheet = Base64.encodeBase64String(textContent.getBytes());
		}	
		
		doc = docBuilder.newDocument();
		
		Element rootElement = doc.createElementNS(OjbcNamespaceContext.NS_CRIMINAL_HISTORY_TEXT_DOC, "cht-doc:CriminalHistoryTextDocument");
		rootElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_CRIMINAL_HISTORY_TEXT_DOC);
		doc.appendChild(rootElement);
		
		Element fchrd = XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_CRIMINAL_HISTORY_TEXT_DOC, "cht-doc:FederalCriminalHistoryRecordDocument");
		
		Element base64 = XmlUtils.appendElement(fchrd, OjbcNamespaceContext.NS_CRIMINAL_HISTORY_TEXT_DOC, "cht-doc:Base64BinaryObject");
		
		base64.setTextContent(sBase64Rapsheet);

		return doc;
		
	}
	
}
