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

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.IdentificationSearchRequest;
import org.w3c.dom.Document;

public class IdentificationSearchRequestSQLProcessor extends AbstractIdentificationSearchRequestProcessor {

	private static final Log log = LogFactory.getLog(IdentificationSearchRequestSQLProcessor.class);
	
	private EnhancedAuditDAO enhancedAuditDAO;
	
	private UserInfoSQLProcessor userInfoSQLProcessor;
	
	public void auditIdentificationSearchRequest(Exchange exchange, @Body Document document)
	{
		try {
			
			Integer userInfoPk = userInfoSQLProcessor.auditUserInfo(exchange);
			
			log.debug("User Info FK: " + userInfoPk);
			
			IdentificationSearchRequest identificationSearchRequest = processIdentificationSearchRequest(document);
			
			identificationSearchRequest.setUserInfoId(userInfoPk);
			
            String messageId = (String) exchange.getIn().getHeader("federatedQueryRequestGUID");
            identificationSearchRequest.setMessageId(messageId);
            
			Integer identificationSearchRequestID = enhancedAuditDAO.saveIdentificationSearchRequest(identificationSearchRequest);
			
			for (String identificationReasonDescription : identificationSearchRequest.getReasonCode())
			{
				Integer identificationSearchReasonCode = enhancedAuditDAO.retrieveIdentificationReasonCodeFromDescription(identificationReasonDescription);
				
				if (identificationSearchRequestID != null && identificationSearchReasonCode != null)
				{
					enhancedAuditDAO.saveIdentificationReasonCode(identificationSearchReasonCode, identificationSearchRequestID);
				}	
				
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Unable to audit person search request: " + ExceptionUtils.getStackTrace(e));
		}
		
	}
	
	public EnhancedAuditDAO getEnhancedAuditDAO() {
		return enhancedAuditDAO;
	}

	public void setEnhancedAuditDAO(EnhancedAuditDAO enhancedAuditDAO) {
		this.enhancedAuditDAO = enhancedAuditDAO;
	}

	public UserInfoSQLProcessor getUserInfoSQLProcessor() {
		return userInfoSQLProcessor;
	}

	public void setUserInfoSQLProcessor(UserInfoSQLProcessor userInfoSQLProcessor) {
		this.userInfoSQLProcessor = userInfoSQLProcessor;
	}

	
}
