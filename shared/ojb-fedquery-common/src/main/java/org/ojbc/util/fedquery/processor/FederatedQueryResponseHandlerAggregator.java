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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.CxfPayload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.fedquery.FederatedQueryProfile;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FederatedQueryResponseHandlerAggregator {

	private static final Log log = LogFactory.getLog( FederatedQueryResponseHandlerAggregator.class );
	private Map<String, List<FederatedQueryProfile>> federatedQueryManager;
	
	private Map<String, String> addressToAdapterURIMap;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void aggregateGroupMessagesString(Exchange groupedExchange)
	{
		List<Exchange> grouped = groupedExchange.getProperty(Exchange.GROUPED_EXCHANGE, List.class);
		
		StringBuffer sb = new StringBuffer();
		sb.append("<OJBAggregateResponseWrapper>");
        
        List<String> endpointsThatDidNotRespond = new ArrayList<String>();
        
		for (Exchange exchange : grouped)
		{
		
			//This exchange is the message sent to start the federated query timer, it is an exchange with a string that says 'START_QUERY_TIMER'	
			if (exchange.getIn().getBody().getClass().getName().equals("java.lang.String") && exchange.getIn().getBody().equals("START_QUERY_TIMER"))
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
				 
				 continue; 
			}
			
			if (exchange.getIn().getBody().getClass().getName().equals("org.apache.camel.component.cxf.CxfPayload"))
			{
				
				//Uncomment the line below to see the individual aggregated message
				//log.debug("This is the body of the exchange in the exchange group: " + exchange.getIn().getBody());
				
				CxfPayload cxfPayload = (CxfPayload)exchange.getIn().getBody();
				List<Element> elementList = cxfPayload.getBody();
				
		        String bodyAsString = OJBUtils.getStringFromDocument(elementList.get(0).getOwnerDocument());
		        sb.append(bodyAsString);
		        groupedExchange.getIn().getAttachments().putAll(exchange.getIn().getAttachments());
		        continue; 
			}	
			
				//Uncomment the line below to see the individual aggregated message
				//log.debug("This is the body of the exchange in the exchange group: " + exchange.getIn().getBody());
				
			String response = exchange.getIn().getBody(String.class);
			
			if (StringUtils.isNotBlank(response));
				sb.append(response);
			
		}	
		
		String aggregatedCompletedBy = (String)groupedExchange.getProperty(Exchange.AGGREGATED_COMPLETED_BY);
		
		//When there is a timeout, find out which endpoint timed out
		if (aggregatedCompletedBy != null && aggregatedCompletedBy.equals("timeout"))
		{
			log.info("Federated Query Completed by timeout.");
			
			Map<String, Boolean> endpointsCalled = null;
			
			Exchange timerExchange = grouped.get(0);
			
			//Look in the timer exchange for the original endpoints called
			if (timerExchange.getIn().getBody().getClass().getName().equals("java.lang.String") && timerExchange.getIn().getBody().equals("START_QUERY_TIMER"))
			{
				endpointsCalled = returnEndpointsCalled(timerExchange);   
			}	
			
			//Iterate through the exchanges and mark in the map which queries returned a response
			if (endpointsCalled != null)
			{
				for (Exchange exchange : grouped)
				{
					if (exchange.getIn().getBody().getClass().getName().equals("org.apache.camel.component.cxf.CxfPayload"))
					{
						String searchProfileInResponseExchange = (String) exchange.getIn().getHeader("searchProfile");
						
						if (StringUtils.isEmpty(searchProfileInResponseExchange))
						{
							log.info("No search profile in message, try retrieving search profile from WS-Addressing 'From' Address");

							HashMap<String, String> wsAddressingHeadersMap = OJBUtils.returnWSAddressingHeadersFromCamelSoapHeaders(exchange);
							String wsAddressingFrom = wsAddressingHeadersMap.get("From");
							
							log.info("WS-Addressing 'From' Address" + wsAddressingFrom);	

							if (addressToAdapterURIMap != null)
							{
								searchProfileInResponseExchange = addressToAdapterURIMap.get(wsAddressingFrom);
							}	
							
						}	
						
						log.info("Response Recieved from: " + searchProfileInResponseExchange);
						
						//We put the key/value in the hashmap and it will indicate that we received a response
						//It will overwrite the existing entry in the map
						endpointsCalled.put(searchProfileInResponseExchange, true);
					}	
				}	
			}
			
			//We add the endpoints that did not respond to a list
			//The list is then available as a Camel header
			for(Entry entry: endpointsCalled.entrySet()) 
			{
				  // get key
				  String searchProfileInResponseExchange = (String)entry.getKey();
				  // get value
				  Boolean responseReceived = (Boolean) entry.getValue();
				  
				  if (!responseReceived)
				  {
					  log.info(searchProfileInResponseExchange + " did not return a response.");
					  endpointsThatDidNotRespond.add(searchProfileInResponseExchange);
				  }	  
			}
			
			if (!endpointsThatDidNotRespond.isEmpty())
			{
				groupedExchange.getIn().setHeader("endpointsThatDidNotRespond", endpointsThatDidNotRespond);
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

	/**
	 * This method will return a map containing the endpoints called.  It will set the boolean value
	 * to false indicating that a response has not been received for them.  We will later iterate
	 * through the response exchanges to see which endpoints did return responses.
	 * 
	 * @param exchange
	 * @return
	 */

	protected Map<String, Boolean> returnEndpointsCalled(Exchange exchange) {
		NodeList federatedQueryEndpointsNodeList = (NodeList)exchange.getIn().getHeader("federatedQueryEndpointsNodeList");
		
		Map<String, Boolean> endpointsCalled = new HashMap<String, Boolean>();
		
		if (federatedQueryEndpointsNodeList != null && federatedQueryEndpointsNodeList.getLength() > 0) 
		{
		    for (int i = 0; i < federatedQueryEndpointsNodeList.getLength(); i++) 
		    {
		        if (federatedQueryEndpointsNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) 
		        {
		            Element el = (Element) federatedQueryEndpointsNodeList.item(i);
		            log.info("Endpoint in original request: " + el.getTextContent());
		            
		            endpointsCalled.put(el.getTextContent(), false);
		        }
		    }
		}
		
		return endpointsCalled;
	}
	

	public Map<String, List<FederatedQueryProfile>> getFederatedQueryManager() {
		return federatedQueryManager;
	}

	public void setFederatedQueryManager(
			Map<String, List<FederatedQueryProfile>> federatedQueryManager) {
		this.federatedQueryManager = federatedQueryManager;
	}

	public Map<String, String> getAddressToAdapterURIMap() {
		return addressToAdapterURIMap;
	}

	public void setAddressToAdapterURIMap(Map<String, String> addressToAdapterURIMap) {
		this.addressToAdapterURIMap = addressToAdapterURIMap;
	}

	

}
