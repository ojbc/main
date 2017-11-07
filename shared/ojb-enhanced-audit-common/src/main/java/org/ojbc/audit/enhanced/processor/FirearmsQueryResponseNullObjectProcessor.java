package org.ojbc.audit.enhanced.processor;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.FirearmsQueryResponse;
import org.w3c.dom.Document;

public class FirearmsQueryResponseNullObjectProcessor extends AbstractFirearmsQueryResponseProcessor{

	private static final Log log = LogFactory.getLog(FirearmsQueryResponseNullObjectProcessor.class);
	
	@Override
	public void auditFirearmsQueryResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID) {
		
		try {
			
			FirearmsQueryResponse firearmsQueryResponse = new FirearmsQueryResponse();
			
			firearmsQueryResponse = processFirearmsQueryResponse(document);
			
			log.info("Message ID: " + messageID);		
			log.info(firearmsQueryResponse.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Unable to audit person query response: " + ExceptionUtils.getStackTrace(e));
		}
		
		
	}

	
}
