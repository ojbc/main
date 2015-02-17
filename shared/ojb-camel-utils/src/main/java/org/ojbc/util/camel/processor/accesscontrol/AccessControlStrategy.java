package org.ojbc.util.camel.processor.accesscontrol;

import org.apache.camel.Exchange;
import org.ojbc.util.model.accesscontrol.AccessControlResponse;

public interface AccessControlStrategy {

	public AccessControlResponse authorize(Exchange ex) throws Exception; 
	
}