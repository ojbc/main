package org.ojbc.audit.enhanced.processor;

import org.apache.camel.Body;
import org.apache.camel.Header;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.PersonQueryWarrantResponse;
import org.w3c.dom.Document;

public class PersonQueryWarrantResponseNullObjectProcessor extends AbstractPersonQueryWarrantResponseProcessor{

	private static final Log log = LogFactory.getLog(PersonQueryWarrantResponseNullObjectProcessor.class);
	
	@Override
	public void auditPersonQueryWarrantResponseResponse(@Body Document document,  @Header(value = "federatedQueryRequestGUID")String messageID) {
		
		try {
			
			PersonQueryWarrantResponse personQueryWarrantResponse = new PersonQueryWarrantResponse();
			
			personQueryWarrantResponse = processPersonQueryWarrantResponse(document);
			
			log.info("Message ID: " + messageID);		
			log.info(personQueryWarrantResponse.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Unable to audit person query response: " + ExceptionUtils.getStackTrace(e));
		}
		
		
	}

	
	
}
