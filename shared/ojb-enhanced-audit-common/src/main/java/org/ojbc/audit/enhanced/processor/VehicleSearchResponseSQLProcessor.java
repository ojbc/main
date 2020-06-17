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
import org.apache.camel.Header;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.VehicleSearchResult;
import org.w3c.dom.Document;

public class VehicleSearchResponseSQLProcessor extends AbstractVehicleSearchResponseProcessor{

	private static final Log log = LogFactory.getLog(VehicleSearchResponseSQLProcessor.class);
	
	private EnhancedAuditDAO enhancedAuditDAO;
	
	@Override
	public void auditVehicleSearchResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID) {
		
		try {
			
			VehicleSearchResult vehicleSearchResult = processVehicleSearchResponse(document);
			
			Integer systemToSearchPK = enhancedAuditDAO.retrieveSystemToSearchIDFromURI(vehicleSearchResult.getSystemSearchResultURI());
			
			if (systemToSearchPK != null)
			{
				vehicleSearchResult.setSystemSearchResultID(systemToSearchPK);
			}	
			else
			{
				systemToSearchPK = enhancedAuditDAO.retrieveSystemToSearchIDFromSystemName(vehicleSearchResult.getSystemName());
				
				if (systemToSearchPK != null)
				{
					vehicleSearchResult.setSystemSearchResultID(systemToSearchPK);
				}	

			}	

			Integer vehicleSearchRequestPK = enhancedAuditDAO.retrieveVehicleSearchIDfromMessageID(messageID);
			
			if (vehicleSearchRequestPK != null)
			{
				vehicleSearchResult.setVehicleSearchRequestId(vehicleSearchRequestPK);
			}	
			
			enhancedAuditDAO.saveVehicleSearchResult(vehicleSearchResult);
			
			log.info(vehicleSearchResult.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Unable to audit vehicle search request: " + ExceptionUtils.getStackTrace(e));
		}
		
		
	}

	public EnhancedAuditDAO getEnhancedAuditDAO() {
		return enhancedAuditDAO;
	}

	public void setEnhancedAuditDAO(EnhancedAuditDAO enhancedAuditDAO) {
		this.enhancedAuditDAO = enhancedAuditDAO;
	}
	
	
}
