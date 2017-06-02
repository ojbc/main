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
package org.ojbc.bundles.prototypes.bundlea.beans;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.bundles.prototypes.shared.interfaces.LogMessageHelper;

public class BundleABean2 {
	
	private static final Log LOGGER = LogFactory.getLog(BundleABean2.class);
	
	private LogMessageHelper logMessageHelper;
	
	public LogMessageHelper getLogMessageHelper() {
		return logMessageHelper;
	}

	public void setLogMessageHelper(LogMessageHelper logMessageHelper) {
		this.logMessageHelper = logMessageHelper;
	}
	
	public void processExchangeWithHelp(Exchange e) throws Exception {
		LOGGER.info("BundleABean2 using helper to write message");
		logMessageHelper.writeLogMessage(e, "Message via helper");
	}

	public void processExchange(Exchange e) throws Exception {
		LOGGER.info("BundleABean2 message");
	}


}
