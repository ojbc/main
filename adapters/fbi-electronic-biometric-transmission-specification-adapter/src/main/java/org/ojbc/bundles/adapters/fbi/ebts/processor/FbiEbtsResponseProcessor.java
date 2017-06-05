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

import java.util.logging.Logger;

import org.apache.camel.Exchange;
import org.ojbc.intermediaries.sn.util.SubscriptionResponseBuilderUtil;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FbiEbtsResponseProcessor {
	
	private static final Logger logger = Logger.getLogger(FbiEbtsResponseProcessor.class.getName());
	
	
	public void sendFbiSubscribeResponse(Exchange exchange) throws Exception{
		
		Document responseDoc = SubscriptionResponseBuilderUtil.createSubscribeResponse();
				
		logger.info("\n\n\n Returning response:... \n");
		
		exchange.getOut().setBody(responseDoc);		
	}
		
	public void sendFbiSubscribeManagerResponse(Exchange exchange) throws Exception{
		
		Document responseDoc = null;
		
		String operation = exchange.getIn().getHeader("originalSubscriptionOperation", String.class);
		
		if("Modify".equals(operation)){
			
			responseDoc = getModifyResponseDoc();
			
		}else if("Unsubscribe".equals(operation)){
			
			responseDoc = SubscriptionResponseBuilderUtil.createUnsubscribeResponse();
			
		}else{
			logger.severe("Unknown operation, can't send correct response");
		}
		
		exchange.getOut().setBody(responseDoc);
	}

	
	public Document getModifyResponseDoc() throws Exception{
		
		Document doc = OJBCXMLUtils.createDocument();
		
		Element root = doc.createElementNS(OjbcNamespaceContext.NS_B2, "ModifyResponse");
		root.setPrefix(OjbcNamespaceContext.NS_PREFIX_B2);
		doc.appendChild(root);		
				
		Element subModRespMsgElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_SUBSCRIPTION_MODIFICATION_RESPONSE_EXCH, 
				"SubscriptionModificationResponseMessage");
						
		Element subModIndElement = XmlUtils.appendElement(subModRespMsgElement, 
				OjbcNamespaceContext.NS_SUBSCRIPTION_RESPONSE_EXT, "SubscriptionModifiedIndicator");
		
		//TODO make value dynamic
		subModIndElement.setTextContent("true");		
		
		return doc;
	}
	
}
