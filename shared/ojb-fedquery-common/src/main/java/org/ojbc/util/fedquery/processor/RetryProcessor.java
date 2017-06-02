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
package org.ojbc.util.fedquery.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.fedquery.processor.FederatedQueryResponseHandlerAggregator;

public class RetryProcessor {

	private Map<String, Integer> retryMap = new HashMap<String, Integer>();
	private int maxRetries;

	private static final Log log = LogFactory.getLog( FederatedQueryResponseHandlerAggregator.class );
	
	@SuppressWarnings("unchecked")
	public boolean retryExchange(Exchange groupedExchange)
	{
		List<Exchange> exchangeList = groupedExchange.getProperty(Exchange.GROUPED_EXCHANGE, List.class);
		
		if (exchangeList.size() == 1)
		{
			Exchange orignalBookingExchange = exchangeList.get(0);
			
			String federatedQueryGUID = (String) orignalBookingExchange.getIn().getHeader("federatedQueryRequestGUID");
			log.info("Check guid for retry eligiblity: " + federatedQueryGUID);
			
			if (StringUtils.isNotBlank(federatedQueryGUID))
			{
				Integer retries = retryMap.get(federatedQueryGUID);
				
				if (retries == null)
				{
					log.info("No Retries thus far.  Resending message with GUID: " + federatedQueryGUID);
					retries = 0;
				}	
				
				if (retries <= maxRetries)
				{
					log.info("Retries: " + retries + " is less than max retries: " + maxRetries + ".  Resending message with GUID: " + federatedQueryGUID);
					
					retries = retries + 1;
					
					retryMap.put(federatedQueryGUID, retries);
					return true;
				}	
				else
				{
					//We have hit the max retries
					return false;
				}	
				
			}	
			
		}	
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public void updateExchange(Exchange groupedExchange)
	{		
		List<Exchange> exchangeList = groupedExchange.getProperty(Exchange.GROUPED_EXCHANGE, List.class);
	
		if (exchangeList.size() == 1)
		{
			Exchange orignalBookingExchange = exchangeList.get(0);
			
			String federatedQueryGUID = (String) orignalBookingExchange.getIn().getHeader("federatedQueryRequestGUID");

			groupedExchange.getIn().setBody(orignalBookingExchange.getIn().getHeader("requestMessageBody"));
			groupedExchange.getIn().setHeader("federatedQueryRequestGUID", federatedQueryGUID);
		}	

	}
	
	public int getMaxRetries() {
		return maxRetries;
	}

	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}
	
}
