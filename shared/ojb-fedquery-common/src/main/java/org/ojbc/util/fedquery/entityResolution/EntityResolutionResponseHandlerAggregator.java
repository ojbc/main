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
package org.ojbc.util.fedquery.entityResolution;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.CxfPayload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.fedquery.error.MergeNotificationErrorProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This aggregator accepts two exchanges in a list: the Aggregated Person Search Results, the merged results after ER.
 * 
 * It will grab the federated query request GUID (message ID) from the the original exchange to persist to future messages.
 * It then checks to see if the original response has errors in it (a header is set in the camel route earlier).
 * If errors are present in the original aggregated results, it splices them into the merge response document.
 * 
 * @author yogeshchawla
 *
 */
public class EntityResolutionResponseHandlerAggregator {

	private static final Log log = LogFactory.getLog( EntityResolutionResponseHandlerAggregator.class );
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void aggregateMergedMessageWithErrorResponses(Exchange groupedExchange) throws Exception
	{
		//Get the grouped exchanged consisting of the aggregated search results and merged results
		List<Exchange> grouped = groupedExchange.getProperty(Exchange.GROUPED_EXCHANGE, List.class);
        
		boolean responseHasErrors = false;
		Document erResponseBodyDocument = null;
		Document psResultsBeforeER = null;
		
		for (Exchange exchange : grouped)
		{
			//This is the original exchange, it contains the aggregated response message before the Person Search to ER XSLT
			if (grouped.indexOf(exchange) == 0)
			{	
			 String messageID = (String)exchange.getIn().getHeader("federatedQueryRequestGUID");

			 //The new grouped exchange does not get the message headers from the original exchange so we manually copy the message ID
			 groupedExchange.getIn().setHeader("federatedQueryRequestGUID", messageID);
			 
			 //Get header to see if we have error nodes.  This is the exchange before calling ER so it has these headers, subsequent exchanges do not.
			 String errorResponseNodeCountString = (String)exchange.getIn().getHeader("errorResponseNodeCount");
			 
			 Integer errorResponseNodeCount = null;
			 
			 if (errorResponseNodeCountString != null)
			 {	 
				 errorResponseNodeCount = Integer.valueOf(errorResponseNodeCountString);
			 }
			 else
			 {
				 errorResponseNodeCount = 0;
			 }	 
				 
			 if (errorResponseNodeCount > 0)
			 {
				 responseHasErrors = true;
				 
				 if (exchange.getIn().getBody().getClass().equals("java.lang.String"))
				 {
					 String aggregatedResponse = (String)exchange.getIn().getBody();
					 
					 //Load up the aggregated results into a document
					 psResultsBeforeER = OJBUtils.loadXMLFromString(aggregatedResponse);
					 
				 }	 
				 
				 //Load up the aggregated results into a document
				 psResultsBeforeER = exchange.getIn().getBody(Document.class);
			 }	 
			 
			}
			
			//This is the actual response from the ER service, it will always be exchange indexed at position 1
			else
			{
				//Uncomment the line below to see the individual aggregated message
				//log.debug("This is the body of the exchange in the exchange group: " + exchange.getIn().getBody());
				
				CxfPayload cxfPayload = (CxfPayload)exchange.getIn().getBody();
				List<Element> elementList = cxfPayload.getBody();
				
		        erResponseBodyDocument = elementList.get(0).getOwnerDocument();
			}	
			
		}	
		
		//The ER service did not return with an actual response, it is down or has timed out.  Set a static error response and return.
		if (erResponseBodyDocument == null)
		{
			String returnMessage = MergeNotificationErrorProcessor.returnMergeNotificationErrorMessageEntityResolution();
			groupedExchange.getIn().setBody(returnMessage);
			return;
		}	
		
		//If we have errors, splice them into the response
		if (responseHasErrors)
		{
			log.debug("Response has errors, splice them in here");
			
			NodeList list = psResultsBeforeER.getElementsByTagNameNS("http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0","SearchResultsMetadata");
			Element searchResultsMetadataElement = (Element)erResponseBodyDocument.importNode(list.item(0),true);
			
			Element searchResultsMetadataCollectionElement = erResponseBodyDocument.createElementNS("http://nij.gov/IEPD/Exchange/EntityMergeResultMessage/1.0", "SearchResultsMetadataCollection");
			searchResultsMetadataCollectionElement.appendChild(searchResultsMetadataElement);
			
			erResponseBodyDocument.getFirstChild().appendChild(searchResultsMetadataCollectionElement);
			
		}	
		
		//Set the response
		groupedExchange.getIn().setBody(erResponseBodyDocument);
		
	}

	
}
