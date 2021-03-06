/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
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
