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
import org.apache.camel.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.bundles.prototypes.shared.interfaces.LogMessageHelper;

public class BundleABean1 {
	
	private static final Log LOGGER = LogFactory.getLog(BundleABean1.class);
	
	private LogMessageHelper logMessageHelper;
	
	private String message1;
	private String message2;
	private String message3;
	private String message4;
	private String message5;
	private String message6;
	
	public void setMessage5(String message5) {
		this.message5 = message5;
	}

	public void setMessage6(String message6) {
		this.message6 = message6;
	}

	public void setMessage2(String message2) {
		this.message2 = message2;
	}

	public void setMessage3(String message3) {
		this.message3 = message3;
	}

	public void setMessage4(String message4) {
		this.message4 = message4;
	}

	public void setMessage1(String message) {
		this.message1 = message;
	}

	public LogMessageHelper getLogMessageHelper() {
		return logMessageHelper;
	}

	public void setLogMessageHelper(LogMessageHelper logMessageHelper) {
		this.logMessageHelper = logMessageHelper;
	}
	
	public void processExchangeWithHelp(Exchange e) throws Exception {
		LOGGER.info("BundleABean1 using helper to write message");
		logMessageHelper.writeLogMessage(e, "Message via helper");
	}

	public void processExchange(Exchange e) throws Exception {
		LOGGER.info("BundleABean1 message");
		LOGGER.info("Bundle A Bean 1 Exchange=" + e);
		LOGGER.info("Bundle A Bean 1 message1=" + message1);
		LOGGER.info("Bundle A Bean 1 message2=" + message2);
		LOGGER.info("Bundle A Bean 1 message3=" + message3);
		LOGGER.info("Bundle A Bean 1 message4=" + message4);
		LOGGER.info("Bundle A Bean 1 message5=" + message5);
		LOGGER.info("Bundle A Bean 1 message6=" + message6);
		Message m = e.getIn();
		String body = m.getBody(String.class);
		StringBuffer messages = new StringBuffer();
		messages.append("1=").append(message1).append("|");
		messages.append("2=").append(message2).append("|");
		messages.append("3=").append(message3).append("|");
		messages.append("4=").append(message4).append("|");
		messages.append("5=").append(message5).append("|");
		messages.append("6=").append(message6).append("|");
		m.setBody(body + "\nBundle A Bean 1:" + messages);
	}

}
