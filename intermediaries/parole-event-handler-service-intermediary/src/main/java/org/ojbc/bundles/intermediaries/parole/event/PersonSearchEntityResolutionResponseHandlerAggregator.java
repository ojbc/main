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
package org.ojbc.bundles.intermediaries.parole.event;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;
import org.ojbc.util.fedquery.error.MergeNotificationErrorProcessor;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class PersonSearchEntityResolutionResponseHandlerAggregator {
	
	private static final Logger logger = Logger.getLogger(PersonSearchEntityResolutionResponseHandlerAggregator.class);
		
	
	@SuppressWarnings({ "unchecked" })
	public void addFbiIdToSubscribeMessage(Exchange groupedExchange) throws Exception{
		
		List<Exchange> groupedExchangeList = groupedExchange.getProperty(Exchange.GROUPED_EXCHANGE, List.class);				

		Exchange caseInitExchange = groupedExchangeList.get(0);		
		copyInMessageId(groupedExchange, caseInitExchange);		
		Document caseInitMessageDoc = caseInitExchange == null ? null : caseInitExchange.getIn().getBody(Document.class);
								
		Exchange prsnSrchResEntResExchange = groupedExchangeList.get(1);		
		Document persSrchResDoc = prsnSrchResEntResExchange == null ? null : prsnSrchResEntResExchange.getIn().getBody(Document.class);
		
		
		if(caseInitMessageDoc != null && persSrchResDoc != null){
			
			String fbiId = XmlUtils.xPathStringSearch(persSrchResDoc, 
					"//psres:PersonSearchResult/emrm-ext:Person/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");
			
			logger.info("\n\n\n Using fbiId: " + fbiId +"\n\n\n");
			
			Node caseInitDocFbiNode = XmlUtils.xPathNodeSearch(caseInitMessageDoc, " TODO ");
			
			caseInitDocFbiNode.setTextContent(fbiId);
						
			groupedExchange.getIn().setBody(caseInitMessageDoc);	
			
		}else if(persSrchResDoc == null){
		
			logger.warn("\n\n\n Person Search results were NULL!!(down or timed out)  Can't set fbiId on subscription message. \n\n\n");
			
			String errorMsgDoc = MergeNotificationErrorProcessor.returnMergeNotificationErrorMessageEntityResolution();
			
			groupedExchange.getIn().setBody(errorMsgDoc);
		}				
											
	}
		
	private void copyInMessageId(Exchange groupedExchange, Exchange subscribeMessageExchange){
		
		String personSearchGuidMessageId = (String)subscribeMessageExchange.getIn().getHeader("personSearchGuid");

		// The new grouped exchange does not get the message headers from the original exchange so we manually copy the message ID
		groupedExchange.getIn().setHeader("personSearchGuid", personSearchGuidMessageId);		
	}
	
}

