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
package org.ojbc.web.portal.controllers.helpers;

import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;



public class SamlTokenProcessor {
	
	
	public SamlTokenParsed parseSamlToken(Document samlTokenDoc) throws Exception {
		
		SamlTokenParsed samlTokenParsed = new SamlTokenParsed();
		
		Node rootAssertionNode = XmlUtils.xPathNodeSearch(samlTokenDoc, OjbcNamespaceContext.NS_PREFIX_SAML_ASSERTION + ":Assertion");
		
		Node attributeStatementNode = XmlUtils.xPathNodeSearch(rootAssertionNode, OjbcNamespaceContext.NS_PREFIX_SAML_ASSERTION + ":AttributeStatement");
		
		Node emailAttributeNode = XmlUtils.xPathNodeSearch(attributeStatementNode, OjbcNamespaceContext.NS_PREFIX_SAML_ASSERTION + ":Attribute[@Name='gfipm:2.0:user:EmailAddressText']");
		
		Node emailValueNode = XmlUtils.xPathNodeSearch(emailAttributeNode, OjbcNamespaceContext.NS_PREFIX_SAML_ASSERTION + ":AttributeValue");
		
		String sEmail = emailValueNode.getTextContent();
		
		samlTokenParsed.setEmail(sEmail);
				
		return samlTokenParsed;
	}

}
