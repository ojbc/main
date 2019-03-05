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
package org.ojbc.audit.enhanced.processor;

import java.time.LocalDate;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.ValidationRequest;
import org.w3c.dom.Document;

public abstract class AbstractValidationRequestProcessor {

	private static final Log log = LogFactory.getLog(AbstractValidationRequestProcessor.class);
	
	public abstract void auditValidationRequest(Exchange exchange);
	
	ValidationRequest processValidationRequest(@Header("subscriptionId") String subscriptionId, @Header("validationDueDateString") String validationDueDateString) throws Exception
	{
		ValidationRequest validationRequest = new ValidationRequest();

		validationRequest.setStateSubscriptionId(subscriptionId);
		
		//example: 2024-02-05
		if (StringUtils.isNotBlank(validationDueDateString))
		{	
			LocalDate validationDueDate = LocalDate.parse(validationDueDateString);
			validationRequest.setValidationDueDate(validationDueDate);
		}
		
        log.debug("Validation Request: " + validationRequest.toString());

        return validationRequest;
	}

}
