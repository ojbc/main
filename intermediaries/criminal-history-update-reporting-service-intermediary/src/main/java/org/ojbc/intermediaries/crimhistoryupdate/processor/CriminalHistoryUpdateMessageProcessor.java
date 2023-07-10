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
package org.ojbc.intermediaries.crimhistoryupdate.processor;

import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.ojbc.util.camel.processor.MessageProcessor;

public class CriminalHistoryUpdateMessageProcessor {

	private MessageProcessor messageProcessor;

	public void processRequestPayload(Exchange exchange) throws Exception{
		
		
		try {
			messageProcessor.processRequestPayload(exchange);
		} catch (Exception e) {
			
			String requestID = (String) exchange.getIn().getHeader("federatedQueryRequestGUID");
			
			if (StringUtils.isBlank(requestID)){
				
				requestID = UUID.randomUUID().toString().replaceAll("-", "");
				
				exchange.getIn().setHeader("federatedQueryRequestGUID", requestID);
				exchange.getIn().setHeader("platformSafeFileName", requestID);
			}
			
		}
		
	}
	
	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}

	public void setMessageProcessor(MessageProcessor messageProcessor) {
		this.messageProcessor = messageProcessor;
	}
	
}
