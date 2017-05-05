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
package org.ojbc.bundles.adapters.fbi.ebts.processor;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class NgiResponseProcessor {
	
	private Logger logger = Logger.getLogger(NgiResponseProcessor.class);
	
	public void sendVoidResponse(Exchange camelExchange) throws Exception{
		
		String voidResponse = getVoidResponse();
		
		camelExchange.getOut().setBody(voidResponse);		
	}
	
	String getVoidResponse() throws Exception{
				
		Document doc = OJBCXMLUtils.createDocument();		
				
		Element rootVoidElement = doc.createElementNS("http://ws.cjis.gov/2014/08/01/ngi/core/xsd", "VOID");
		
		doc.appendChild(rootVoidElement);
		
		String voidResponse = OJBUtils.getStringFromDocument(doc);
		
		logger.info("\n\n returning void response: \n" + voidResponse + "\n\n");				
		
		return voidResponse;
	}

}
