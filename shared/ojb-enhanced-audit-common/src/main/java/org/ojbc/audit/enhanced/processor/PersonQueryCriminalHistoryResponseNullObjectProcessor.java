package org.ojbc.audit.enhanced.processor;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.PersonQueryCriminalHistoryResponse;
import org.w3c.dom.Document;

public class PersonQueryCriminalHistoryResponseNullObjectProcessor extends AbstractPersonQueryCriminalHistoryResponseProcessor{

	private static final Log log = LogFactory.getLog(PersonQueryCriminalHistoryResponseNullObjectProcessor.class);
	
	@Override
	public void auditPersonQueryCriminalHistoryResponseResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID) {
		
		try {
			
			PersonQueryCriminalHistoryResponse personQueryCriminalHistoryResponse = new PersonQueryCriminalHistoryResponse();
			
			personQueryCriminalHistoryResponse = processPersonQueryCriminalHistoryResponse(document);
			
			log.info("Message ID: " + messageID);		
			log.info(personQueryCriminalHistoryResponse.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Unable to audit person query response: " + ExceptionUtils.getStackTrace(e));
		}
		
		
	}

	
	
}
