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
package org.ojbc.intermediaries.sn.subscription;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapFault;
import org.ojbc.intermediaries.sn.exception.InvalidEmailAddressesException;
import org.ojbc.intermediaries.sn.exception.InvalidSAMLTokenException;
import org.ojbc.intermediaries.sn.exception.InvalidTopicException;
import org.ojbc.intermediaries.sn.util.FaultMessageBuilderUtil;
import org.ojbc.util.camel.helper.OJBUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This fault message processor will create a fault for operations that are not supported
 * by this implementation.
 * 
 * @author yogeshchawla
 *
 */

public class FaultMessageProcessor {

	/**
	 * logger
	 */
	private static final Log log = LogFactory.getLog( FaultMessageProcessor.class );
	
	public void createFault(Exchange exchange) throws Exception{
		
		log.debug("Entering generic fault processor to create a soap fault response");
	
		//Get operation name from Camel header
		String operationName = (String)exchange.getIn().getHeader("operationName");
		
		//Use the out message so we have a clean message
		Message outMessage = exchange.getOut();
		String returnValue = "";
		
		Document detailPayload = null;
		
		//Check the operation name and then set the 500 fault code and create a fault message
		if (operationName.equals("GetCurrentMessage"))
		{
			returnValue = FaultMessageBuilderUtil.createFault("ResourceUnknownFault", "http://docs.oasis-open.org/wsrf/r-2");			
		}	
		if (operationName.equals("RegisterPublisher"))
		{
			returnValue = FaultMessageBuilderUtil.createFault("PublisherRegistrationRejectedFault","http://docs.oasis-open.org/wsn/br-2");
		}	

		if (operationName.equals("Unsubscribe"))
		{
			Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
			
			if (cause instanceof InvalidSAMLTokenException)
			{
				returnValue = FaultMessageBuilderUtil.createUnableToDestorySubscriptionFaultAccessDenialResponse(cause.getMessage());
			}
			else
			{
				returnValue = FaultMessageBuilderUtil.createUnableToDestorySubscriptionFault();
			}	
		}	

		if (operationName.equals("Subscribe"))
		{
			Exception cause = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
			
			if (cause instanceof InvalidEmailAddressesException)
			{
				returnValue = FaultMessageBuilderUtil.createSubscribeCreationFailedFaultInvalidEmailResponse(((InvalidEmailAddressesException) cause).getInvalidEmailAddresses());
				
			} else if (cause instanceof InvalidSAMLTokenException)
			{
				returnValue = FaultMessageBuilderUtil.createSubscribeCreationFailedFaultInvalidTokenResponse(cause.getMessage());
				
			} else if (cause instanceof InvalidTopicException)
			{
				returnValue = FaultMessageBuilderUtil.createSubscribeCreationFailedFaultErrorResponse(cause.getMessage());
				
			} else if (cause != null) {
				
				returnValue = FaultMessageBuilderUtil.createSubscribeCreationFailedFaultErrorResponse(cause.getMessage());
			}
				
		}	

		if (operationName.equals("Validate"))
		{
			detailPayload = FaultMessageBuilderUtil.createUnableToValidateSubscriptionFault((String)exchange.getIn().getHeader("subscriptionID"));
		}	

	    SoapFault fault = new SoapFault("unable to process request", SoapFault.FAULT_CODE_SERVER);
	    Element detail = fault.getOrCreateDetail();
	    
	    if (detailPayload == null)
	    {	
	    	detailPayload = OJBUtils.loadXMLFromString(returnValue);
	    }	
	    
	    detail.appendChild(detail.getOwnerDocument().importNode(detailPayload.getDocumentElement(), true));
	    
		outMessage.setHeader(org.apache.cxf.message.Message.RESPONSE_CODE, new Integer(500));
		outMessage.setFault(true);
		
		outMessage.setBody(fault);
	}
	
	
}
