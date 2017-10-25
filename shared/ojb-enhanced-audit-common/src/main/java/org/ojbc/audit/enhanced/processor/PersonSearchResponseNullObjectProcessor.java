package org.ojbc.audit.enhanced.processor;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.PersonSearchResult;
import org.w3c.dom.Document;

public class PersonSearchResponseNullObjectProcessor extends AbstractPersonSearchResponseProcessor{

	private static final Log log = LogFactory.getLog(PersonSearchResponseNullObjectProcessor.class);
	
	@Override
	public void auditPersonSearchResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID) {
		
		try {
			
			PersonSearchResult personSearchResult = processPersonSearchResponse(document);
			
			log.info("Message ID: " + messageID);		
			log.info(personSearchResult.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Unable to audit person search request: " + ExceptionUtils.getStackTrace(e));
		}
		
		
	}

	
	
}
