package org.ojbc.audit.enhanced.processor;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.PersonQueryWarrantResponse;
import org.w3c.dom.Document;

public class PersonQueryWarrantResponseSQLProcessor extends AbstractPersonQueryWarrantResponseProcessor {

	private static final Log log = LogFactory.getLog(PersonQueryWarrantResponseSQLProcessor.class);
	
	private EnhancedAuditDAO enhancedAuditDAO;
	
	@Override
	public void auditPersonQueryWarrantResponseResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID) {
		
		try {
			
			PersonQueryWarrantResponse personQueryWarrantResponse = new PersonQueryWarrantResponse();
			
			personQueryWarrantResponse = processPersonQueryWarrantResponse(document);
			
			personQueryWarrantResponse.setMessageId(messageID);
			
			Integer personQueryPk = enhancedAuditDAO.retrievePersonQueryIDfromMessageID(messageID);
			
			if (personQueryPk != null)
			{
				personQueryWarrantResponse.setQueryRequestId(personQueryPk);
			}	
			
			enhancedAuditDAO.savePersonQueryWarrantResponse(personQueryWarrantResponse);
			
			log.info(personQueryWarrantResponse.toString());
			
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
