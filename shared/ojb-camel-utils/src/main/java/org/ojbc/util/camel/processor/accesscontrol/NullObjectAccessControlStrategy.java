package org.ojbc.util.camel.processor.accesscontrol;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.model.accesscontrol.AccessControlResponse;

public class NullObjectAccessControlStrategy implements AccessControlStrategy {

	private static final Log log = LogFactory.getLog(NullObjectAccessControlStrategy.class);
	
	@Override
	public final AccessControlResponse authorize(Exchange ex) throws Exception {
		
		AccessControlResponse accessControlResponse = new AccessControlResponse();
		
		log.info("Entering Null Object Access Control Strategy.  Will always return true.");
		
		accessControlResponse.setAuthorized(true);
		accessControlResponse.setAccessControlResponseMessage("Null Object Strategy. This strategy will always authorize the exchange.");
		
		return accessControlResponse;
	}

}
