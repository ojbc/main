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
