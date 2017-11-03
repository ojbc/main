package org.ojbc.audit.enhanced.processor;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.PersonQueryCriminalHistoryResponse;
import org.w3c.dom.Document;

public class PersonQueryCriminalHistoryResponseSQLProcessor extends AbstractPersonQueryCriminalHistoryResponseProcessor{

	private static final Log log = LogFactory.getLog(PersonQueryCriminalHistoryResponseSQLProcessor.class);
	
	private EnhancedAuditDAO enhancedAuditDAO;
	
	@Override
	public void auditPersonQueryCriminalHistoryResponseResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID) {
		
		try {
			
			PersonQueryCriminalHistoryResponse personQueryCriminalHistoryResponse = new PersonQueryCriminalHistoryResponse();
			
			personQueryCriminalHistoryResponse = processPersonQueryCriminalHistoryResponse(document);
			
			personQueryCriminalHistoryResponse.setMessageId(messageID);
			
			Integer personQueryPk = enhancedAuditDAO.retrievePersonQueryIDfromMessageID(messageID);
			
			if (personQueryPk != null)
			{
				personQueryCriminalHistoryResponse.setQueryRequestId(personQueryPk);
			}	
			
			enhancedAuditDAO.savePersonQueryCriminalHistoryResponse(personQueryCriminalHistoryResponse);
			
			log.info(personQueryCriminalHistoryResponse.toString());
			
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
