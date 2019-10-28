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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.FirearmsSearchRequest;
import org.w3c.dom.Document;

public class FirearmsSearchRequestSQLProcessor extends AbstractFirearmsSearchRequestProcessor {

	private static final Log log = LogFactory.getLog(FirearmsSearchRequestSQLProcessor.class);
	
	private EnhancedAuditDAO enhancedAuditDAO;
	
	private UserInfoSQLProcessor userInfoSQLProcessor;
	
	public void auditFirearmsSearchRequest(Exchange exchange, @Body Document document)
	{
		try {
			
			Integer userInfoPk = userInfoSQLProcessor.auditUserInfo(exchange);
			
			log.debug("User Info FK: " + userInfoPk);
			
			FirearmsSearchRequest firearmsSearchRequest = processFirearmsSearchRequest(document);
			
			log.info("Firearms Search Request: " + firearmsSearchRequest);
			
			firearmsSearchRequest.setUserInfofk(userInfoPk);
			
            if (StringUtils.isNotEmpty(firearmsSearchRequest.getSerialNumberQualifierCode())) {
            	
            	firearmsSearchRequest.setSerialNumberQualifierCodeId(enhancedAuditDAO.retrieveSearchQualifierCodeIDfromCodeName(firearmsSearchRequest.getSerialNumberQualifierCode()));
            }

            String messageId = (String) exchange.getIn().getHeader("federatedQueryRequestGUID");
            firearmsSearchRequest.setMessageId(messageId);
            
			Integer firearmsSearchRequestPK = enhancedAuditDAO.saveFirearmsSearchRequest(firearmsSearchRequest);
			
			for (String systemToSearch : firearmsSearchRequest.getSystemsToSearch())
			{
				Integer systemToSearchPK = enhancedAuditDAO.retrieveSystemToSearchIDFromURI(systemToSearch);
				
				if (firearmsSearchRequestPK != null && systemToSearchPK != null)
				{
					enhancedAuditDAO.saveFirearmsSystemToSearch(firearmsSearchRequestPK, systemToSearchPK);
				}	
				
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Unable to audit firearms search request: " + ExceptionUtils.getStackTrace(e));
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
