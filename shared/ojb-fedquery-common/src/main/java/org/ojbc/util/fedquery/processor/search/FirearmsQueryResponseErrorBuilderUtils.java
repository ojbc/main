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
package org.ojbc.util.fedquery.processor.search;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ojbc.util.model.accesscontrol.AccessControlResponse;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FirearmsQueryResponseErrorBuilderUtils {

	private static final OjbcNamespaceContext OJBC_NAMESPACE_CONTEXT = new OjbcNamespaceContext();
	
	public static Document createFirearmsQueryAccessDenial(AccessControlResponse accessControlResponse, String denyingSystem) throws Exception
	{
		Document doc = null;
		
        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();

        doc = docBuilder.newDocument();
        Element root = doc.createElementNS(OjbcNamespaceContext.NS_FIREARM_DOC, "FirearmRegistrationQueryResults");
        doc.appendChild(root);
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_FIREARM_DOC);
        
        Element queryResultsMetadata = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_QRM, "QueryResultsMetadata");
		
        Element informationAccessDenial = XmlUtils.appendElement(queryResultsMetadata, OjbcNamespaceContext.NS_IAD, "InformationAccessDenial");

        Element informationAccessDenialIndicator = XmlUtils.appendElement(informationAccessDenial, OjbcNamespaceContext.NS_IAD, "InformationAccessDenialIndicator");
        informationAccessDenialIndicator.setTextContent("true");
        
        Element informationAccessDenyingSystemNameText = XmlUtils.appendElement(informationAccessDenial, OjbcNamespaceContext.NS_IAD, "InformationAccessDenyingSystemNameText");
        informationAccessDenyingSystemNameText.setTextContent(denyingSystem);

        Element informationAccessDenialReasonText = XmlUtils.appendElement(informationAccessDenial, OjbcNamespaceContext.NS_IAD, "InformationAccessDenialReasonText");
        informationAccessDenialReasonText.setTextContent(accessControlResponse.getAccessControlResponseMessage());
        
        OJBC_NAMESPACE_CONTEXT.populateRootNamespaceDeclarations(root);

		return doc;
	}
	
}
