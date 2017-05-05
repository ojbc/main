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
package org.ojbc.bundles.adapters.personsearch.ndex;

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.cxf.message.Message;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FbiSAMLTokenProcessor {

	public Document insertFBISamlToken(Exchange ex) throws Exception
	{
		Message cxfMessage = ex.getIn().getHeader(CxfConstants.CAMEL_CXF_MESSAGE, Message.class);
		SAMLTokenPrincipal token = null;
		
		Document inputDocument = ex.getIn().getBody(Document.class);
		
        if (cxfMessage != null) {
            token = (SAMLTokenPrincipal) cxfMessage.get("wss4j.principal.result");
        }
        else
        {
        	throw new Exception("Unable to retrieve SAML Token");
        }	

        if (token != null)
        {	
        	Element samlToken = token.getToken().getSaml2().getDOM();
        	//XmlUtils.printNode(samlToken);
        	
        	Node fbiSamlAssertionNode = XmlUtils.xPathNodeSearch(inputDocument, "/ulexsr:doStructuredSearchRequest/ulex:StructuredSearchRequestMessage/ulex:UserAssertionSAML");
        	
        	Node samlTokenImportedToMessage = inputDocument.importNode(samlToken, true);
        	
        	fbiSamlAssertionNode.appendChild(samlTokenImportedToMessage);
        	
        }	
        else
        {
        	throw new Exception("Unable to retrieve SAML Token");
        }	
        
        return inputDocument;        
	}
	
}
