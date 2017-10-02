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
package org.ojbc.intermediaries.probation;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.ojbc.util.security.XmlHashProcessor;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ProbationHashProcessor {

	private XmlHashProcessor xmlHashProcessor;

	public Document hashSID(@Body Document body, @Header("SID") String SID) throws Exception
	{
		if (StringUtils.isBlank(SID))
		{
			throw new Exception("SID is empty and must be set.");
		}	
		
		String hashedValue = xmlHashProcessor.returnHashedValue("SHA-256", SID);

//		<pc-ext:PersonPersistentIdentification>
//			<nc20:IdentificationID>1234556</nc20:IdentificationID>
//		</pc-ext:PersonPersistentIdentification>
		
		//Update element value with hashed value
		Element personElement = (Element) XmlUtils.xPathNodeSearch(body, "//pcext:ProbationCase/pcext:Supervision/pcext:Probationer");
		
		Element ppi = XmlUtils.appendElement(personElement, OjbcNamespaceContext.NS_PROBATION_EXTENSION, "pcext:PersonPersistentIdentification");
		
		Element identificationID  = XmlUtils.appendElement(ppi, OjbcNamespaceContext.NS_NC, "nc:IdentificationID");
		
		identificationID.setTextContent(hashedValue);
		
		return body;
	}
	
	
	public XmlHashProcessor getXmlHashProcessor() {
		return xmlHashProcessor;
	}

	public void setXmlHashProcessor(XmlHashProcessor xmlHashProcessor) {
		this.xmlHashProcessor = xmlHashProcessor;
	}
	
}
