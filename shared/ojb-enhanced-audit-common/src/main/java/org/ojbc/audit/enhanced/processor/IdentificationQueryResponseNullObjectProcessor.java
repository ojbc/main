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

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.IdentificationQueryResponse;
import org.w3c.dom.Document;

public class IdentificationQueryResponseNullObjectProcessor extends AbstractIdentificationQueryResponseProcessor {

	private static final Log log = LogFactory.getLog(IdentificationQueryResponseNullObjectProcessor.class);
	
	public void auditIdentificationQueryResponse(Document document, String messageID)
	{
		try {

			IdentificationQueryResponse identificationQueryResponse = processIdentificationQueryResponse(document);
			
			log.info("Message ID: " + messageID);		
			log.info(identificationQueryResponse.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Unable to audit identification search response: " + ExceptionUtils.getStackTrace(e));
		}
		
	}
		
}
