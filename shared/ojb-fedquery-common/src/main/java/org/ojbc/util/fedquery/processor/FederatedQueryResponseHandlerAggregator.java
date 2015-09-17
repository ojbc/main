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
package org.ojbc.util.fedquery.processor;

import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.CxfPayload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.fedquery.FederatedQueryProfile;
import org.w3c.dom.Element;

public class FederatedQueryResponseHandlerAggregator {

	private static final Log log = LogFactory.getLog( FederatedQueryResponseHandlerAggregator.class );
	private Map<String, List<FederatedQueryProfile>> federatedQueryManager;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void aggregateGroupMessagesString(Exchange groupedExchange)
	{
		List<Exchange> grouped = groupedExchange.getProperty(Exchange.GROUPED_EXCHANGE, List.class);
		
        StringBuffer sb = null;
        
		for (Exchange exchange : grouped)
		{
		
			//This exchange is the message sent to start the federated query timer, it is an exchange with a string that says 'START_QUERY_TIMER'	
			if (exchange.getIn().getBody().getClass().getName().equals("java.lang.String"))
			{
				 String startMessage = exchange.getIn().getBody(String.class); 
				 
				 String messageID = (String)exchange.getIn().getHeader("federatedQueryRequestGUID");

				 //Clear the request out of the hashmap by using the message ID
				 federatedQueryManager.remove(messageID);

				 //The new grouped exchange does not get the message headers from the original exchange so we manually copy the message ID
				 groupedExchange.getIn().setHeader("federatedQueryRequestGUID", messageID);
				 
				 //Set the operation name here in case no other responses were received so the message can be properly processed.
				 //This will be overwritten if we actually got responses
				 groupedExchange.getIn().setHeader("operationName", exchange.getIn().getHeader("operationName"));

				 
				 log.debug("Processing aggregator start message: " + startMessage);
			}	
			
			if (exchange.getIn().getBody().getClass().getName().equals("org.apache.camel.component.cxf.CxfPayload"))
			{
				//This is the first exchange we are processing
				if (sb == null)
				{
					//Create the XML wrapper start tag
					sb = new StringBuffer();
					sb.append("<OJBAggregateResponseWrapper>");
					
				}	
				
				//Uncomment the line below to see the individual aggregated message
				//log.debug("This is the body of the exchange in the exchange group: " + exchange.getIn().getBody());
				
				CxfPayload cxfPayload = (CxfPayload)exchange.getIn().getBody();
				List<Element> elementList = cxfPayload.getBody();
				
		        String bodyAsString = OJBUtils.getStringFromDocument(elementList.get(0).getOwnerDocument());
		        sb.append(bodyAsString);
		        groupedExchange.getIn().getAttachments().putAll(exchange.getIn().getAttachments());
			}	
			
		}	
		
		if (sb != null)
		{
			sb.append("</OJBAggregateResponseWrapper>");
			groupedExchange.getIn().setBody(sb.toString());
			
			//Copy the operation name from the last exchange to the grouped exchange.  
			//We can copy the operation name from any exchange but the first exchange is the 'start timer' message.
			Exchange lastExchange = grouped.get(grouped.size() -1);
			groupedExchange.getIn().setHeader("operationName", lastExchange.getIn().getHeader("operationName"));
		}	
		else
		{
			groupedExchange.getIn().setBody("<OJBAggregateResponseWrapper></OJBAggregateResponseWrapper>");
		}	
	    
		
	}
	

	public Map<String, List<FederatedQueryProfile>> getFederatedQueryManager() {
		return federatedQueryManager;
	}

	public void setFederatedQueryManager(
			Map<String, List<FederatedQueryProfile>> federatedQueryManager) {
		this.federatedQueryManager = federatedQueryManager;
	} 
	

}
