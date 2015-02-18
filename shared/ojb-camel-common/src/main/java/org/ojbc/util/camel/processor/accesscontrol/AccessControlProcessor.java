package org.ojbc.util.camel.processor.accesscontrol;

import org.apache.camel.Exchange;
import org.ojbc.util.model.accesscontrol.AccessControlResponse;

public class AccessControlProcessor {

	private AccessControlStrategy accessControlStrategy;

	public boolean authorize (Exchange ex) throws Exception {
		
		if (this.accessControlStrategy == null)
		{
			this.accessControlStrategy = new NullObjectAccessControlStrategy();
		}	
		
		AccessControlResponse accessControlResponse = this.accessControlStrategy.authorize(ex);
		
		ex.getIn().setHeader("accessControlIsAuthorized", accessControlResponse.isAuthorized());
		ex.getIn().setHeader("accessControlResponseMessage", accessControlResponse.getAccessControlResponseMessage());
		ex.getIn().setHeader("accessControlResponse", accessControlResponse);
		
		return accessControlResponse.isAuthorized();
	}
	
	public AccessControlStrategy getAccessControlStrategy() {
		return accessControlStrategy;
	}

	public void setAccessControlStrategy(AccessControlStrategy accessControlStrategy) {
		this.accessControlStrategy = accessControlStrategy;
	}
	
}
