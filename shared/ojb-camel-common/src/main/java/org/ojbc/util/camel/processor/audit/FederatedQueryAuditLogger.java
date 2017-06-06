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
package org.ojbc.util.camel.processor.audit;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.camel.Header;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.security.saml.OJBSamlMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FederatedQueryAuditLogger {

	private OJBSamlMap OJBSamlMap;
	
	private static final Log log = LogFactory.getLog(FederatedQueryAuditLogger.class);
	
	public void auditSearchParameters(@Header("tokenID") String tokenID)
	{
		//Get SAML token from hashmap
		Element assertion = (Element)OJBSamlMap.getToken(tokenID);

		if (assertion == null)
		{
			throw new IllegalStateException("No SAML token provided to log audit trail.");
		}
		
		StringBuffer sb = new StringBuffer();
		
		String fullAssertion = getStringFromDocument(assertion.getOwnerDocument());
		
		sb.append("Audit Message: " + fullAssertion);
		
		log.info(sb.toString());
		
	}

	public OJBSamlMap getOJBSamlMap() {
		return OJBSamlMap;
	}

	public void setOJBSamlMap(OJBSamlMap oJBSamlMap) {
		OJBSamlMap = oJBSamlMap;
	}
	
	//method to convert Document to String
	private String getStringFromDocument(Document doc)
	{
	    try
	    {
	       DOMSource domSource = new DOMSource(doc);
	       StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
		   transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		   transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "4");
		   transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	       transformer.transform(domSource, result);
	       return writer.toString();
	    }
	    catch(TransformerException ex)
	    {
	       ex.printStackTrace();
	       return null;
	    }
	} 
}
