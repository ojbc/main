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
package org.ojbc.bundles.mockimpl.fbi.ngi;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Exchange;
import org.apache.cxf.helpers.XMLUtils;
import org.apache.log4j.Logger;
import org.ojbc.util.camel.helper.OJBUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FbiNgiResponseProcessor {
	
	private Logger logger = Logger.getLogger(FbiNgiResponseProcessor.class);
	
	
	public String createResponse(Exchange exchange) throws ParserConfigurationException{
		
		String subAckResponse = getSubAckResponse();
		
		logger.info("\n\n Processor returning subsription aknowledgement response: \n\n" + subAckResponse + "\n\n");
				
		return subAckResponse;
	}
	
	
	String getSubAckResponse() throws ParserConfigurationException{
		
		Document doc = XMLUtils.newDocument();
		
		Element rootElement = XMLUtils.createElementNS(doc, 
				"http://biometrics.nist.gov/standard/2011", "NISTBiometricInformationExchangePackage");
		
		doc.appendChild(rootElement);
		
		String response = OJBUtils.getStringFromDocument(doc);		
		
		return response;
	}

}
