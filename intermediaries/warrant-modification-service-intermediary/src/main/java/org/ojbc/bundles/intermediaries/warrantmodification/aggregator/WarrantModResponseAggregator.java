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
package org.ojbc.bundles.intermediaries.warrantmodification.aggregator;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WarrantModResponseAggregator {

	private static final Log log = LogFactory.getLog(WarrantModResponseAggregator.class);
	
	@SuppressWarnings("unchecked")
	public void prepareResponseExchange(Exchange groupedExchange){
		
		List<Exchange> groupExchList = groupedExchange.getIn().getBody(List.class);
		        					
		if(groupExchList.isEmpty() || groupExchList.size() != 2){
			
			log.error("\n\n\n Missing exchange, Stopping route. \n\n\n");
			
			groupedExchange.setRouteStop(true);
			
			return;
		}
				
		Exchange timerExchange = groupExchList.get(0);	
		
		Exchange warantRespExchange = groupExchList.get(1);
		
		
		groupedExchange.getIn().setBody(warantRespExchange.getIn().getBody(String.class));
		
		// grouped exchange doesn't get message headers from 1st timer exchange so manually copy them
		groupedExchange.getIn().setHeader("federatedQueryRequestGUID", timerExchange.getIn().getHeader("federatedQueryRequestGUID"));
												
		groupedExchange.getIn().setHeader("operationName", warantRespExchange.getIn().getHeader("operationName"));

		log.info("\n\n\n Successfully prepared Warrant Mod Response in group exchange \n\n\n");
	}
	
}
