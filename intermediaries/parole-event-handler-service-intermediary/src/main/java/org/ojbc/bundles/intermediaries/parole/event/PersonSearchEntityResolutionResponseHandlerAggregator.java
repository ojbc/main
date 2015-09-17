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
import org.w3c.dom.NodeList;


public class PersonSearchEntityResolutionResponseHandlerAggregator {
	
	private static final Logger logger = Logger.getLogger(PersonSearchEntityResolutionResponseHandlerAggregator.class);
		
	
	@SuppressWarnings("unchecked")
	public void addFbiIdToCaseInitMessage(Exchange groupedExchange) throws Exception{
		
		List<Exchange> groupedExchangeList = groupedExchange.getProperty(Exchange.GROUPED_EXCHANGE, List.class);		
		int groupedExchangesCount = groupedExchangeList.size();

		Exchange caseInitExchange = null;
		
		if(groupedExchangesCount > 0){
			// contract: "always receive this fist at index zero"
			caseInitExchange = groupedExchangeList.get(0);
			copyCaseInitExchHeadersToGroupedExchange(groupedExchange, caseInitExchange);	
		}
		
		String caseInitMessage = caseInitExchange == null ? null : (String)caseInitExchange.getIn()
				.getHeader("originalCaseInitArrestMessageBody",String.class);
		
		logger.info("\n\n\n Aggregator using case init message:  \n\n " + caseInitMessage + "\n\n\n");
										
		Document persSrchResDoc = null;
		// contract: "receive this second message at index 1"
		if(groupedExchangesCount > 1){			
			Exchange prsnSrchResEntResExchange = groupedExchangeList.get(1);		
			persSrchResDoc = prsnSrchResEntResExchange == null ? null : prsnSrchResEntResExchange.getIn().getBody(Document.class);			
		}
		
		if(caseInitMessage != null && persSrchResDoc != null){
			
			logger.info("\n\n\n caseInitMessageDoc != null && persSrchResDoc != null.  (Adding fbi id to case init exchange now)... \n\n\n");
					
			groupedExchange.getIn().setBody(caseInitMessage);				
			
			populateGroupExchFbiIdHeaderFromPersonSearchResponse(persSrchResDoc, groupedExchange);
			
		}else if(persSrchResDoc == null){
		
			logger.warn("\n\n\n Person Search results were NULL!!(down or timed out)  Can't set fbiId. Forwarding original CaseInit message... \n\n\n");
			
			groupedExchange.getIn().setBody(caseInitMessage);	
						
		}else if(caseInitMessage == null){
			logger.error("\n\n\n Lost Case Init message!!! It's not being sent. \n\n\n");
			
			String errorMsgDoc = MergeNotificationErrorProcessor.returnMergeNotificationErrorMessageEntityResolution();			
			groupedExchange.getIn().setBody(errorMsgDoc);
		}				
											
	}
		
	
	String populateGroupExchFbiIdHeaderFromPersonSearchResponse(Document personSearchResponse, Exchange groupedExchange) throws Exception{
		
		String fbiId = null;
		
		NodeList responseNodeList = XmlUtils.xPathNodeListSearch(personSearchResponse, "//psres:PersonSearchResult");
				
		if(responseNodeList != null && responseNodeList.getLength() == 1){
			
			fbiId = XmlUtils.xPathStringSearch(personSearchResponse, 
					"//psres:PersonSearchResult/emrm-ext:Person/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");
			
			logger.info("\n\n\n Using fbiId: " + fbiId + "\n\n\n");		
			
			groupedExchange.getIn().setHeader("fbiId", fbiId);
		
		}else{			
			logger.error("\n\n\n WARNING Person Responses NOT = 1, (NOT setting fbi id now)  \n\n\n");
		}	
		
		return fbiId;
	}
	
	// The new grouped exchange does not get the message headers from the original exchange so we manually copy them in
	void copyCaseInitExchHeadersToGroupedExchange(Exchange groupedExchange, Exchange caseInitExchange){
		
		String personSearchGuidMessageId = (String)caseInitExchange.getIn().getHeader("federatedQueryRequestGUID");
		groupedExchange.getIn().setHeader("federatedQueryRequestGUID", personSearchGuidMessageId);		
		
		String topicExpression = (String)caseInitExchange.getIn().getHeader("topicExpression");		
		groupedExchange.getIn().setHeader("topicExpression", topicExpression);
	}
	
}

