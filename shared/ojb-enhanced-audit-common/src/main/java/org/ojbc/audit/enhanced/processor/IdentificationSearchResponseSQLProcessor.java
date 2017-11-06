package org.ojbc.audit.enhanced.processor;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.IdentificationSearchResult;
import org.ojbc.audit.enhanced.dao.model.PersonSearchResult;
import org.w3c.dom.Document;

public class IdentificationSearchResponseSQLProcessor extends AbstractIdentificationSearchResponseProcessor{

	private static final Log log = LogFactory.getLog(IdentificationSearchResponseSQLProcessor.class);
	
	private EnhancedAuditDAO enhancedAuditDAO;
	
	@Override
	public void auditIdentificationSearchResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID) {
		
		try {
			
			IdentificationSearchResult identificationSearchResult = processIdentificationSearchResponse(document);
			
			//TODO: change this method
			Integer identificationSearchRequestId = enhancedAuditDAO.retrievePersonSearchIDfromMessageID(messageID);
			
			if (identificationSearchRequestId != null)
			{
				identificationSearchResult.setIdentificationSearchRequestId(identificationSearchRequestId);
			}	

			enhancedAuditDAO.saveidentificationSearchResponse(identificationSearchResult);
			
			log.info(identificationSearchResult.toString());
			
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
	
	
}
